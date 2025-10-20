package com.ats.project.view;

import com.ats.project.exceptions.EnrollmentAlreadyExistsException;
import com.ats.project.exceptions.EnrollmentValidationException;
import com.ats.project.model.*;
import com.ats.project.repository.EnrollmentRepo;
import com.ats.project.service.CoursesService;
import com.ats.project.service.EnrollmentService;
import com.ats.project.service.StudentsService;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.faces.model.SelectItem;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Named("enrollmentBean")
@ViewScoped
@Getter
@Setter
@NoArgsConstructor
public class EnrollmentBean implements Serializable {

    private List<Enrollment> enrollments;
    private List<Semester> semesters;
    private List<Grade> grades;
    private List<EnrollmentStatus> enrollmentStatuses;
    private List <SelectItem> statusSelections;
    private List <SelectItem> activeSelections;
    private Enrollment selectedEnrollment;
    private int page = 0;
    private int pageSize = 5;
    private long totalRecords;

    private Long studentId;
    private Long courseId;
    private Semester selectedSemester;
    private Boolean activeFilter;
    private LazyDataModel<Enrollment> lazyEnrollments;


    @Autowired
    private StudentsService studentsService;
    @Autowired
    private CoursesService coursesService;
    @Autowired
    private EnrollmentRepo enrollmentRepo;
    @Autowired
    private EnrollmentService enrollmentService;

    @Autowired
    public EnrollmentBean(StudentsService studentsService, CoursesService coursesService,
                          EnrollmentRepo enrollmentRepo, EnrollmentService enrollmentService) {
        this.studentsService = studentsService;
        this.coursesService = coursesService;
        this.enrollmentRepo = enrollmentRepo;
        this.enrollmentService = enrollmentService;
    }

    @PostConstruct
    public void init() {
        try {
            resetForm();
            refreshEnrollments();
            semesters = List.of(Semester.values());
            grades = List.of(Grade.values());
            enrollmentStatuses = List.of(EnrollmentStatus.values());

//            statusSelections = enrollmentStatuses.stream()
//                    .map(s -> new SelectItem(s.name(), s.name()))
//                    .collect(Collectors.toList());
//            statusSelections.add(new SelectItem("", "All Statuses"));
            statusSelections = enrollmentStatuses.stream()
                    .map(s -> new SelectItem(s.name(), s.name())) // value = string name
                    .collect(Collectors.toList());
            statusSelections.add(0, new SelectItem("", "All Statuses"));
            activeSelections =  List.of(
                    new SelectItem("", "All"),
                    new SelectItem("true", "Active"),
                    new SelectItem("false", "Inactive")
            );

            loadLazySetup();
        } catch (Exception e) {
            e.printStackTrace();
            addErrorMessage("Error initializing enrollment bean: " + e.getMessage());
        }
    }

    private String sortField, sortOrder = "asc";
    private String globalFilter;


    private void loadLazySetup(){
        lazyEnrollments = new  LazyDataModel<Enrollment>() {

            @Override
            public int count(Map<String, FilterMeta> filterBy) {
                return (int)    enrollmentService.count();
            }

            @Override
            public List<Enrollment> load(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {
                int pageNumber = first / pageSize;


                String sortFieldToUse = (sortField != null && !sortField.isEmpty() && !sortField.equals(""))
                        ? sortField: "student.id";
                Sort.Direction direction = "desc".equalsIgnoreCase(sortOrder) ?Sort.Direction.DESC : Sort.Direction.ASC;

                Map<String, Object> filters = filterBy.entrySet().stream()
                        .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().getFilterValue()));

                System.out.println("=====Filters=====");
                filters.forEach((k,v) -> System.out.println(k + " -> " + v));

                var pageResult = enrollmentService.getEnrollments(pageNumber, pageSize, sortField, direction, filters);
                setRowCount((int) pageResult.getTotalElements());
                return pageResult.getContent();

            }

        };
    }



    public void sortEnrollments() {
        if (sortField == null  || sortOrder == null) {
            return;
        }
        try{
            loadLazySetup();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Sort Applied"));
        }catch(Exception e){
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Error sorting enrollments: " + e.getMessage()));
        }


    }


    public void resetForm() {
        selectedEnrollment = new Enrollment();
        selectedEnrollment.setId(new EnrollmentId());
        selectedEnrollment.setEnrollmentDate(LocalDate.now());
        selectedEnrollment.setActive(true);
        selectedEnrollment.setStatus(EnrollmentStatus.ENROLLED);
        courseId = null;
        studentId = null;
        selectedSemester = null;
    }

    public void refreshEnrollments() {
        var pageResult = enrollmentService.getEnrollments(page, pageSize);
        enrollments = pageResult.getContent();
        totalRecords = pageResult.getTotalElements();
    }

    public void setRelations() {
        try {
            if (studentId == null || courseId == null) {
                throw new EnrollmentValidationException("Student ID and Course ID are required");
            }

            var student = studentsService.findById(studentId);
            var course = coursesService.findById(courseId);

            if (student.isEmpty()) {
                throw new EnrollmentValidationException("Student not found with ID: " + studentId);
            }
            if (course.isEmpty()) {
                throw new EnrollmentValidationException("Course not found with ID: " + courseId);
            }

            selectedEnrollment.setStudent(student.get());
            selectedEnrollment.setCourse(course.get());
        } catch (Exception e) {
            addErrorMessage("Error setting relations: " + e.getMessage());
            throw e;
        }
    }

    public void saveEnrollment() {
        try {
            // Validate required fields
            if (selectedSemester == null) {
                addErrorMessage("Please select a semester");
                return;
            }

            // Set relations first
            setRelations();

            // Create enrollment ID
            EnrollmentId enrollmentId = new EnrollmentId();
            enrollmentId.setStudentId(studentId);
            enrollmentId.setCourseId(courseId);
            enrollmentId.setSemester(selectedSemester);

            selectedEnrollment.setId(enrollmentId);

            // Check if this is an update or new enrollment
            if (enrollmentRepo.existsById(enrollmentId)) {
                enrollmentService.updateEnrollment(selectedEnrollment);
                addSuccessMessage("Enrollment updated successfully");
            } else {
                enrollmentService.addEnrollment(selectedEnrollment);
                addSuccessMessage("Enrollment added successfully");
            }

            refreshEnrollments();
            resetForm();

        } catch (EnrollmentAlreadyExistsException e) {
            addErrorMessage("Enrollment already exists: " + e.getMessage());
        } catch (EnrollmentValidationException e) {
            addErrorMessage("Validation error: " + e.getMessage());
        } catch (Exception e) {
            addErrorMessage("Error saving enrollment: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void deleteEnrollment(Enrollment enrollment) {
        try {
            enrollmentService.deleteEnrollmentById(enrollment.getId());
            refreshEnrollments();
            addSuccessMessage("Enrollment deleted successfully");
        } catch (Exception e) {
            addErrorMessage("Error deleting enrollment: " + e.getMessage());
        }
    }

    public void setSelectedEnrollment(Enrollment enrollment) {
        try {
            this.selectedEnrollment = new Enrollment();

            // Copy the enrollment data
            this.selectedEnrollment.setId(enrollment.getId());
            this.selectedEnrollment.setStatus(enrollment.getStatus());
            this.selectedEnrollment.setGrade(enrollment.getGrade());
            this.selectedEnrollment.setEnrollmentDate(enrollment.getEnrollmentDate());
            this.selectedEnrollment.setActive(enrollment.isActive());
            this.selectedEnrollment.setStudent(enrollment.getStudent());
            this.selectedEnrollment.setCourse(enrollment.getCourse());

            // Set form fields
            if (enrollment.getCourse() != null) {
                this.courseId = enrollment.getCourse().getId();
            }
            if (enrollment.getStudent() != null) {
                this.studentId = enrollment.getStudent().getId();
            }
            if (enrollment.getId() != null) {
                this.selectedSemester = enrollment.getId().getSemester();
            }
        } catch (Exception e) {
            addErrorMessage("Error setting enrollment for edit: " + e.getMessage());
        }
    }


    private void addSuccessMessage(String message) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", message));
    }

    private void addErrorMessage(String message) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", message));
    }


    public void exportToExcel() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Enrollments");

            // Header
            Row header = sheet.createRow(0);
            String[] headers = {"Student ID", "Student Name", "Course ID", "Course Name", "Semester", "Status", "Grade", "Active", "Enrollment Date"};
            for (int i = 0; i < headers.length; i++) {
                header.createCell(i).setCellValue(headers[i]);
            }

            // FIXED: Pass empty filters map instead of undefined variable
            Map<String, Object> emptyFilters = new HashMap<>();
            List<Enrollment> enrollments = enrollmentService.getAllFilteredEnrollments(emptyFilters, "id", Sort.Direction.ASC);

            int rowIdx = 1;
            for (Enrollment e : enrollments) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(e.getStudent().getId());
                row.createCell(1).setCellValue(e.getStudent().getName());
                row.createCell(2).setCellValue(e.getCourse().getId());
                row.createCell(3).setCellValue(e.getCourse().getName());
                row.createCell(4).setCellValue(e.getId().getSemester().toString());
                row.createCell(5).setCellValue(e.getStatus().toString());
                row.createCell(6).setCellValue(e.getGrade() != null ? e.getGrade().toString() : "N/A");
                row.createCell(7).setCellValue(e.isActive() ? "Yes" : "No");
                row.createCell(8).setCellValue(e.getEnrollmentDate().toString());
            }

            // Auto-size columns
            for (int i = 0; i < headers.length; i++) sheet.autoSizeColumn(i);

            // Response
            externalContext.setResponseContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            externalContext.setResponseHeader("Content-Disposition", "attachment; filename=\"enrollments.xlsx\"");
            workbook.write(externalContext.getResponseOutputStream());
            facesContext.responseComplete();

        } catch (Exception e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Export Error", e.getMessage()));
        }
    }
}


/**/