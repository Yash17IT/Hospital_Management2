# 🏥 Hospital Management System

A full-stack Hospital Management System built using **React.js, Spring Boot, and MySQL** to simplify hospital operations through a centralized web application.

The application provides authentication, role-based access, patient management, doctor management, appointment scheduling, doctor availability, and dashboard statistics.

## ✨ Features

- **User Authentication** — User registration and login.
- **JWT Security** — Token-based authentication for protected API requests.
- **Role-Based Access Control** — Different access permissions based on user roles.
- **Patient Management** — Manage patient records.
- **Doctor Management** — Maintain doctor information.
- **Appointment Management** — Manage hospital appointments and their statuses.
- **Doctor Availability** — View and manage doctor availability.
- **Dashboard** — Display hospital statistics and appointment summaries.
- **Responsive Navigation** — Navigate between application modules through a shared sidebar.
- **REST APIs** — Communication between the React frontend and Spring Boot backend.

## 🛠️ Tech Stack

| Technology | Purpose |
|---|---|
| React.js | Frontend user interface |
| Java | Backend programming language |
| Spring Boot | REST API development |
| Spring Security | Authentication and authorization |
| JWT | Token-based authentication |
| BCrypt | Password hashing |
| Spring Data JPA / Hibernate | Database persistence |
| MySQL | Relational database |
| Axios | HTTP requests |
| React Router | Frontend navigation |
| Maven | Backend dependency management |
| Vite | Frontend development and build tool |

## 🏗️ Architecture

The application follows a frontend-backend-database architecture.

```text
React.js Frontend
       |
       | HTTP / JSON using Axios
       v
Spring Boot REST APIs
       |
       v
Spring Security + JWT
       |
       v
JPA / Hibernate
       |
       v
MySQL Database
```

### How It Works

1. Users interact with the React frontend.
2. The frontend sends HTTP requests to Spring Boot REST APIs.
3. Spring Security validates authentication and applies authorization rules.
4. Controllers process incoming requests and coordinate application operations.
5. JPA/Hibernate communicates with MySQL through repositories.
6. The backend returns JSON responses to the frontend.

## 👥 Application Modules

### Authentication
- Register a user account.
- Log in using email and password.
- Generate and validate JWTs.
- Protect routes and API endpoints.

### Patient Management
- Access patient records.
- Perform supported patient management operations.

### Doctor Management
- View doctor information.
- Perform supported doctor management operations.

### Appointment Management
- Manage appointment records.
- Track appointment statuses.

### Doctor Availability
- Retrieve doctor availability.
- Manage availability according to configured permissions.

### Dashboard
- View hospital statistics.
- Display patient, doctor, and appointment summaries.

### Role-Based Access

The application uses roles to control access to application features and backend endpoints. Actual permissions depend on the security configuration.

## 📁 Project Structure

The repository contains separate frontend and backend applications.

```text
Hospital-Management-System/
│
├── backend/
│   ├── src/
│   │   └── main/
│   │       ├── java/
│   │       │   └── org/example/aiproject2/
│   │       │       ├── controller/
│   │       │       ├── service/
│   │       │       ├── repository/
│   │       │       ├── model/
│   │       │       └── security/
│   │       └── resources/
│   │           └── application.properties
│   └── pom.xml
│
├── frontend/
│   ├── src/
│   │   ├── auth/
│   │   ├── components/
│   │   ├── pages/
│   │   ├── services/
│   │   ├── App.jsx
│   │   ├── App.css
│   │   └── main.jsx
│   └── package.json
│
└── README.md
```

*Note: Adjust the folder names and package structure above to match your actual GitHub repository.*

## ⚙️ Prerequisites

Install the following before running the application:

- Java version compatible with the backend configuration
- Maven or the included Maven Wrapper
- MySQL Server
- Node.js and npm
- Git

## 🚀 Installation and Setup

### 1. Clone the Repository

```bash
git clone <YOUR_GITHUB_REPOSITORY_URL>
cd Hospital-Management-System
```

Replace the placeholder with your repository's actual URL.

### 2. Configure MySQL

Create the database:

```sql
CREATE DATABASE hospital_db;
```

Configure the database connection in the backend's `application.properties` file or through environment variables.

Example:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/hospital_db
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
```

Configure any additional JPA properties required by your application.

### 3. Configure Environment Variables

Set the required database credentials and JWT signing secret in your environment.

Example variable names:

```text
DB_USERNAME
DB_PASSWORD
APP_JWT_SECRET
```

The JWT secret must be sufficiently strong and compatible with the application's expected configuration. Never commit real database credentials, signing secrets, access tokens, or other sensitive values to GitHub.

### 4. Run the Backend

Open a terminal in the backend project directory:

```bash
mvn spring-boot:run
```

If the project includes the Maven Wrapper, you can use the appropriate wrapper command for your operating system.

The backend should start at:

```text
http://localhost:8080
```

### 5. Run the Frontend

Open another terminal in the frontend directory:

```bash
npm install
npm run dev
```

Open the Vite development URL shown in the terminal. By default, it is:

```text
http://localhost:5173
```

Ensure the frontend API base URL points to the running backend.

## 🔗 API Endpoints

The following are the main API areas used by the application. Verify the exact HTTP methods and paths against the current backend controllers.

| Method | Endpoint | Purpose |
|---|---|---|
| POST | `/api/auth/register` | Register an account |
| POST | `/api/auth/login` | Authenticate a user |
| `/api/patients` | Patient APIs | Patient management |
| `/api/doctors` | Doctor APIs | Doctor management |
| `/api/appointments` | Appointment APIs | Appointment management |
| `/api/dashboard/statistics` | Dashboard statistics | Retrieve summary statistics |
| Availability routes | Doctor availability APIs | View or manage availability |

Protected requests use the Bearer token format:

```http
Authorization: Bearer <JWT_TOKEN>
```

## 🔐 Security

The application uses Spring Security and JWT to secure backend requests.

- Passwords are hashed using BCrypt.
- JWTs are used for authenticated API requests.
- Authorization rules restrict access to protected operations.
- CORS is configured for the frontend development origin.
- Database credentials and JWT secrets should be supplied through environment variables.

**Production consideration:** role-based restrictions must be enforced by the backend. Patient-specific medical records should only be returned to authorized users, and patient ownership must be checked server-side.

## 🧪 Testing

You can test the application by verifying the following workflows:

1. Register a user account.
2. Log in and verify the authentication response.
3. Access protected APIs with a valid JWT.
4. Verify that invalid or expired tokens are rejected.
5. Test patient and doctor management operations.
6. Create and update appointments.
7. Test doctor availability operations.
8. Verify dashboard statistics.
9. Confirm that unauthorized users cannot access restricted resources.

Postman can be used for manual API testing.

## 🔮 Future Improvements

- Patient-specific dashboards and appointment history.
- Stronger ownership-based access control for medical records.
- Appointment conflict and double-booking prevention.
- Automated unit and integration testing.
- Improved validation and centralized exception handling.
- Deployment using Docker and a cloud hosting platform.
- Audit logging for sensitive hospital operations.

## 🎯 Learning Outcomes

This project demonstrates practical experience with:

- Full-stack web application development.
- Java and Spring Boot REST APIs.
- Authentication and authorization using JWT.
- Password hashing and security configuration.
- React components, hooks, routing and context.
- Axios-based frontend-backend integration.
- Relational database design and ORM persistence.
- Environment-based configuration and API testing.

## 👨‍💻 Author

**Your Name**

Information Technology Student

[GitHub Profile](<YOUR_GITHUB_PROFILE_URL>)

---

*This project was developed for learning and demonstration purposes. Before using it with real patient data, implement and verify appropriate privacy, authorization, auditing, and deployment security controls.*
