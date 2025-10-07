package com.ats.project;

import com.ats.project.model.*;
import com.ats.project.repository.EnrollmentRepo;
import com.ats.project.service.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@SpringBootApplication
public class Task4Application implements CommandLineRunner {

	private final NationalityService nationalityService;
	private final StudentsService studentsService;
	private final MajorService majorService;
	private final FacultyService facultyService;
	private final CoursesService coursesService;
	private final EnrollmentService enrollmentService;
	private final EnrollmentRepo enrollmentRepo;

	public Task4Application(NationalityService nationalityService,
							StudentsService studentsService,
							MajorService majorService,
							FacultyService facultyService,
							CoursesService coursesService,
							EnrollmentService enrollmentService, EnrollmentRepo enrollmentRepo) {
        this.nationalityService = nationalityService;
        this.studentsService = studentsService;
        this.majorService = majorService;
        this.facultyService = facultyService;
		this.coursesService = coursesService;
		this.enrollmentService = enrollmentService;
		this.enrollmentRepo = enrollmentRepo;
	}


    @Override
	public void run(String... args) throws Exception {


//		Nationality nationality = new Nationality();
//		nationality.setName("Palestinian");
//		nationalityService.saveNationality(nationality);
//		Nationality nationality1 = new Nationality();
//		nationality1.setName("Jordanian");
//		nationalityService.saveNationality(nationality1);
//
////----------------------------------------------------------------------------
//		Major major = new Major();
//		major.setName("EE");
//		majorService.saveMajor(major);
////----------------------------------------------------------------------------
//		Major major2 = new Major();
//		major2.setName("IE");
//		majorService.saveMajor(major2);
////----------------------------------------------------------------------------
//		Faculty faculty = new Faculty();
//		faculty.setName("Engineering");
//		faculty = facultyService.saveFaculty(faculty);
//		Faculty faculty2 = new Faculty();
//		faculty2.setName("IT");
//		facultyService.saveFaculty(faculty2);
//
//
//
//
//		Courses c1 = new Courses();
//		c1.setName("Calculus 1");
//		c1.setCreditHours(3);
//		c1.setIsActive(true);
//		c1.setDescription("calculus 1 class");
//		c1.setFaculty(faculty);
//		c1.setMaxCapacity(25);
//		c1.setCurrentEnrollment(20);
//
//		Courses c3 = new Courses();
//		c3.setName("Calculus 2");
//		c3.setCreditHours(3);
//		c3.setIsActive(true);
//		c3.setDescription("calculus 2 class");
//		c3.setFaculty(faculty);
//		c3.setMaxCapacity(25);
//		c3.setCurrentEnrollment(20);
//
//		Courses c2 = new Courses();
//		c2.setName("Fundementals of Computing");
//		c2.setCreditHours(3);
//		c2.setIsActive(true);
//		c2.setDescription("Basics of programming class");
//		c2.setFaculty(faculty2);
//		c2.setMaxCapacity(28);
//		c2.setCurrentEnrollment(2);
//
//		Courses c4 = new Courses();
//		c4.setName("Java");
//		c4.setCreditHours(3);
//		c4.setIsActive(true);
//		c4.setDescription("Java class");
//		c4.setFaculty(faculty2);
//		c4.setMaxCapacity(28);
//		c4.setCurrentEnrollment(2);
//
//		Courses savedC1 = coursesService.CreateCourses(c1);
//		Courses savedC2 = coursesService.CreateCourses(c2);
//
//// Now use the managed entities for prerequisites
//		c3.getPrerequisiteCourses().add(savedC1);
//		c4.getPrerequisiteCourses().add(savedC2);
//
//// Save the courses with prerequisites
//		coursesService.CreateCourses(c3);
//		coursesService.CreateCourses(c4);
//
//		//============================================
//
//
//		Students student = new Students();
//		student.setName("Osaid");
//		student.setNationalId("2110111");
//		student.setGender("Male");
//		student.setBirthDate(LocalDate.of(2000, 11, 30));
//		student.setSchoolAvg(91);
//		student.setMobileNo("0790111112");
//		student.setEmail("osaid@gmail.com");
//		student.setNationality(nationality1);
//		student.setFaculty(faculty);
//		studentsService.createStudent(student);
//
//		//----------------------------------------------------------------------------
//		Students students2 = new Students();
//		students2.setName("Hamza");
//		students2.setNationalId("2111111111");
//		students2.setGender("Male");
//		students2.setBirthDate(LocalDate.of(1998, 11, 30));
//		students2.setSchoolAvg(90);
//		students2.setMobileNo("0790000012");
//		students2.setEmail("Hamzaeiada@gmail.com");
//		students2.setNationality(nationality);
//		students2.setMajor(major2);
//		students2.setFaculty(faculty);
//
//		studentsService.createStudent(students2);
////----------------------------------------------------------------------------
//		Students students3 = new Students();
//		students3.setName("Adle");
//		students3.setNationalId("2222222222");
//		students3.setGender("Male");
//		students3.setBirthDate(LocalDate.of(2003, 10, 1));
//		students3.setSchoolAvg(82);
//		students3.setMobileNo("0793450012");
//		students3.setEmail("Adel@gmail.com");
//		students3.setNationality(nationality);
//		students3.setMajor(major);
//		students3.setFaculty(faculty);
//		studentsService.createStudent(students3);
//
//
////		EnrollmentId enrollmentId = new EnrollmentId(1L,1L, Semester.SUMMER_2025);
////		Enrollment enrollment = enrollmentService.findById(enrollmentId);
////		enrollment.setStatus(EnrollmentStatus.COMPLETED);
////
////		Enrollment enrollment1 = new Enrollment(studentsService.findById(1L).orElse(null),
////				coursesService.findById(3L).orElse(null),
////				true, Grade.A, Semester.SUMMER_2025, EnrollmentStatus.ENROLLED);
//		Courses course = new Courses();
//		course.setIsActive(true);
//		course.setCreditHours(4);
//		course.setName("Entrepreneurship");
//		course.setCurrentEnrollment(9);
//		course.setMaxCapacity(30);
//		course.setDescription("Entrepreneurship course");
//
//
//		coursesService.CreateCourses(course);

//		enrollmentService.addEnrollment(enrollment1);


		List <Students> students  = studentsService.findAll();
		System.out.println("All Students in DB:");
		students.forEach(s ->
				System.out.println(" - " + s.getName() + " | " + s.getEmail()));

//
		List<Courses> courses = coursesService.findAll();
		System.out.println("All Courses In DB:");
		courses.forEach(c->System.out.println(" - " + c.getName() + " | " + c.getId()));



		System.out.println("====================================================================================================");

//
//		try{
//
//		Enrollment enrollment0 = new Enrollment(studentsService.findById(3L).orElse(null),
//				coursesService.findById(5L).orElse(null),true, Grade.C, Semester.SUMMER_2025, EnrollmentStatus.COMPLETED);
//		enrollmentService.addEnrollment(enrollment0);
//		} catch (EnrollmentValidationException e) {
//			System.out.println(e.getMessage());
//		} catch (EnrollmentAlreadyExistsException e){
//			System.out.println(e.getMessage());
//		} catch (Exception e) {
//			System.out.println("Unexpected error: " + e.getMessage());;
//		}



//
//		List <Enrollment> enrollments = enrollmentRepo.findCompletedByStudentId(1L);
//		System.out.println("All completed courses for student with ID 1:");
//		enrollments.forEach(e ->
//			System.out.println(" - " + e.getStudent().getName() + " | " + e.getCourse().getName() +
//					" | " + e.getStatus() +  " | " + e.getGrade().getPoints()  +  " | " + e.getCourse().getCreditHours()));
//

////
//		Students s1 = studentsService.findById(3L).orElse(null);
//		studentsService.addCompletedCourses(s1);
//		studentsService.updateStudent(s1.getId(), s1);
//
//		Courses course = coursesService.findById(2L).orElse(null);


//				Enrollment enrollment1 = new Enrollment(student, course,
//				false, Grade.A, Semester.SUMMER_2025, EnrollmentStatus.COMPLETED);
//				enrollmentService.addEnrollment(enrollment1);
//
//		studentsService.addCompletedCourses(student);

//		System.out.println("=== PREREQUISITE DEBUG ===");
//		System.out.println("Student ID: " + student.getId() + ", Name: " + (student != null ? student.getName() : "NULL"));
//		System.out.println("Course ID: " + 4 + ", Name: " + (course != null ? course.getName() : "NULL"));
//
//		if (student != null) {
//			List<Courses> completed = student.getCompletedCourses().stream().toList();
//			System.out.println("Student completed courses: " + (completed != null ? completed.size() : "NULL"));
//			if (completed != null) {
//				completed.forEach(c -> System.out.println("  - Completed: " + c.getName() + " (ID: " + c.getId() + ")"));
//			}
//		}
//
//		if (course != null) {
//			List<Courses> prerequisites = course.getPrerequisiteCourses();
//			System.out.println("Course prerequisites: " + (prerequisites != null ? prerequisites.size() : "NULL"));
//			if (prerequisites != null) {
//				prerequisites.forEach(p -> System.out.println("  - Required: " + p.getName() + " (ID: " + p.getId() + ")"));
//			}
//		}





	}


	public static void main(String[] args) {

		SpringApplication.run(Task4Application.class, args);
	}

}
