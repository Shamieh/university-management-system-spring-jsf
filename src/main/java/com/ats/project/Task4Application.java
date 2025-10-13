package com.ats.project;

import com.ats.project.model.*;
import com.ats.project.repository.EnrollmentRepo;
import com.ats.project.service.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class Task4Application implements CommandLineRunner {

	private final StudentsService studentsService;
	private final CoursesService coursesService;
	private final UserService userService;


	public Task4Application(StudentsService studentsService, CoursesService coursesService, UserService userService) {
		this.studentsService = studentsService;
		this.coursesService = coursesService;
		this.userService = userService;
	}

	@Override
	public void run(String... args) throws Exception {


		List <Student> students  = studentsService.findAll();
		System.out.println("All Students in DB:");
		students.forEach(s ->
				System.out.println(" - " + s.getName() + " | " + s.getEmail()));

//
		List<Course> courses = coursesService.findAll();
		System.out.println("All Courses In DB:");
		courses.forEach(c->System.out.println(" - " + c.getName() + " | " + c.getId()));



		System.out.println("====================================================================================================");

//		userService.linkUserToStudent("student1", 2L);

	}


	public static void main(String[] args) {

		SpringApplication.run(Task4Application.class, args);
	}

}
