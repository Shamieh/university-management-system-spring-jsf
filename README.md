# University Management System - Spring Boot, JSF & Spring Security

A web-based University Management System built with **Java Spring Boot**, **JSF (Jakarta Server Faces)**, **PrimeFaces**, and **Spring Security**. The system allows managing students, courses, faculties, and enrollments, with CRUD operations, filtering, sorting, Excel export, and role-based access control.

---

## Features

### Authentication & Authorization
- **Login/Logout** functionality using Spring Security.
- Role-based access:
  - **Admin**: Full access to manage students, courses, faculties, and enrollments.
  - **Student**: View courses, enrollments, and their own data only.
- Passwords are securely stored using BCrypt hashing.

### Faculty Management (Admin Only)
- Add, update, delete faculties.
- Paginated, sortable, and filterable faculty table.
- Success and error notifications.

### Student Management
- Admin can add, update, delete students.
- Students can view their own profile.
- Validation for required fields and unique constraints.

### Course Management
- Admin can add, edit, delete courses.
- Assign courses to faculties.
- Sort and filter courses.
- Students can view available courses.

### Enrollment Management
- Admin can enroll students, update status/grades, delete enrollments.
- Students can view their own enrollments.
- Global search, filtering, and sorting.
- Export enrollment data to Excel.

### UI Features
- JSF with PrimeFaces components.
- Dialogs for add/edit operations.
- Messages for success/error notifications.
- Responsive tables with pagination and sorting.

---

## User Roles

| Role   | Permissions                                                                 |
|--------|----------------------------------------------------------------------------|
| Admin  | Full CRUD on Students, Courses, Faculties, Enrollments. Access to all pages. |
| Student| View own profile, view own enrollments, view available courses.           |

- Default accounts can be seeded for testing (or manually created in the database):


---

## Technologies Used

- **Java 17+**
- **Spring Boot**
- **Spring Security**
- **JSF (Jakarta Server Faces)**
- **PrimeFaces**
- **Lombok**
- **MySQL**
- **Maven**
- **Apache POI**
- **CSS**

---

## Project Structure

```

src/main/java
├─ com.ats.project.model       -> Entities: Student, Faculty, Course, Enrollment, User, Role
├─ com.ats.project.view        -> JSF Managed Beans
├─ com.ats.project.service     -> Business logic & service layer
├─ com.ats.project.repository  -> Spring Data JPA Repositories
├─ com.ats.project.security    -> Spring Security UserDetailsService
├─ com.ats.project.config    -> Spring Security configuration
└─ com.ats.project.exceptions  -> Custom exceptions

```
```

src/main/resources
├─ application.properties      -> Database & Spring Boot configuration
├─ templates/                  -> JSF templates (Header, Footer)
├─ css/                        -> Stylesheets
└─ xhtml files                 -> JSF pages (students, courses, faculties, enrollments, login)

````

---

## Setup & Installation

1. **Clone the repository**
```bash
git clone https://github.com/Shamieh/university-management-system-spring-jsf.git
cd university-management-system-spring-jsf
````

2. **Configure Database**

* Update `application.properties` with your MySQL credentials.

3. **Run the Project**

```bash
mvn clean install
mvn spring-boot:run
```

4. **Access the Application**

* Open a browser and navigate to: `http://localhost:7080/`

5. **Login**

* Create new users using DB or createUser method in UserService.java.

---

## Security Notes

* Users are stored in the database with roles (`ADMIN`, `STUDENT`).
* Authentication uses **Spring Security with BCrypt password hashing**.
* Pages are secured using Spring Security expressions and JSF integration.
* Unauthorized access redirects users to the login page.

---

## Screenshots

**Login Page**
<img width="1098" height="754" alt="image" src="https://github.com/user-attachments/assets/44736e57-669f-4c23-a4c2-dbbda36ceff2" />


**Enrollment Add/Edit Page**
<img width="1897" height="874" alt="image" src="https://github.com/user-attachments/assets/a2dc8540-d20b-458d-82ce-a659ebfd1099" />

**Student Dashboard (ROLE_STUDENT)**
<img width="1912" height="884" alt="image" src="https://github.com/user-attachments/assets/829a456f-0501-4ae6-ae33-cef68ec57f85" />


