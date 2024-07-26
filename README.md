# Blog API

This project is a RESTful API for a simple blog application. The API handles CRUD operations for posts, comments, and users, along with user authentication and authorization. The application is built using Spring Boot and MySQL, and it is Dockerized for easy setup and deployment.

## Features

- User registration and login with JWT authentication
- CRUD operations for posts and comments
- Pagination for posts and comments
- Role-based authorization
- Dockerized for easy setup and deployment
- Automated tests and CI/CD pipeline

## Technologies Used

- Java 17
- Spring Boot 3
- Spring Data JPA
- Spring Security
- MySQL
- Docker
- Maven

## Getting Started

### Prerequisites

- Docker and Docker Compose installed
- Java 17 installed
- Maven installed

### Clone the Repository

```bash
git clone https://github.com/InnocentOmenka/blog-api.git
cd blog-api

Environment Variables
SPRING_DATASOURCE_URL=jdbc:mysql://db:3306/blog-api_db
SPRING_DATASOURCE_USERNAME=root
SPRING_DATASOURCE_PASSWORD=mypassword(change to your own mysql password)
SPRING_JPA_HIBERNATE_DDL_AUTO=update
APP_SECURITY_JWT_SECRET_KEY=9367566B59703373367639792F423F4528482B4D6251655468576D5A71349435



Build and Run the Application
Use Docker Compose to build and run the application:

docker-compose up --build

This will start two services: app and db. The app service runs the Spring Boot application, and the db service runs a MySQL database.

API Documentation
User Endpoints
POST /api/users/register - Register a new user
POST /api/users/login - Authenticate user and return a token
GET /api/users/profile - Get user profile (Authenticated)
Post Endpoints
GET /api/posts - Retrieve all posts (Paginated)
GET /api/posts/{id} - Retrieve a single post by ID
POST /api/posts - Create a new post (Authenticated)
PUT /api/posts/{id} - Update a post by ID (Authenticated & Author only)
DELETE /api/posts/{id} - Delete a post by ID (Authenticated & Author only)
Comment Endpoints
GET /api/posts/{postId}/comments - Retrieve all comments for a post (Paginated)
POST /api/posts/{postId}/comments - Create a new comment on a post (Authenticated)
PUT /api/comments/{id} - Update a comment by ID (Authenticated & Author only)
DELETE /api/comments/{id} - Delete a comment by ID (Authenticated & Author only)
Running Tests
To run the tests, use the following command:
mvn test


CI/CD Pipeline
The project includes a CI/CD pipeline configured with GitHub Actions. The pipeline performs the following tasks:

Build the project
Run tests
Build Docker image
Deploy the application
To set up the CI/CD pipeline, ensure your GitHub repository is configured with the necessary secrets for Docker Hub and any deployment targets you are using.

Postman Collection with sample data:
https://api.postman.com/collections/24317753-06722d41-dd34-4cd0-bf01-7a0ef990641e?access_key=PMAT-01J3PCV9C05MKZR26C88PS9MA5
