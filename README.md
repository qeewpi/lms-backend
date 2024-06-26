# Library Management System

## Description

The Library Management System is a full-stack web application developed using Spring Boot, MySQL, and React. It is designed to manage the basic operations in a library like adding new books, updating book details, tracking book orders, and managing users.

This repository contains the backend of the Library Management System. The frontend can be found [here](https://github.com/qeewpi/lms-frontend).

## Prerequisites

Before you begin, ensure you have met the following requirements:

- You have installed the latest version of:
    - [Java](https://www.oracle.com/java/technologies/downloads/#java22)
    - [Maven](https://maven.apache.org/download.cgi)
    - [IntelliJ IDEA](https://www.jetbrains.com/idea/download/)
    - [mySQL](https://dev.mysql.com/downloads/mysql/)

## Installation

Follow the steps below to set up the project on your local machine:

1. Clone the repository to your local machine using the following command:

```bash
git clone https://github.com/qeewpi/lms-backend
```

2. Navigate to the project directory:

```bash
cd your-project-directory
```

3. Open the project in IntelliJ IDEA.

4. Install the Lombok plugin in IntelliJ IDEA:

    - Go to `File > Settings > Plugins`.
    - Click on `Marketplace` and search for `Lombok`.
    - Click on `Install` and restart IntelliJ IDEA.

5. Enable annotation processing in IntelliJ IDEA:

    - Go to `File > Settings > Build, Execution, Deployment > Compiler > Annotation Processors`.
    - Check `Enable annotation processing`.

6. Create the librarymanagementsystem database in your local database server (mySQL). 

7. Update the application.properties file located in src/main/resources with your specific configurations, including the database connection and email details.

8. Update the `IMAGE_PATH` in the `StorageService` class located in `src/main/java/com/it120p/librarymanagementsystem/service/StorageService.java` with the path to your desired directory.

9. Build the project:

```bash
mvn clean install
```

9. Run the project:

```bash
mvn spring-boot:run
```

10. After running the project for the first time, make sure to  insert these rows into the 'roles' table:

```sql
INSERT INTO roles(name) VALUES('ROLE_USER');
INSERT INTO roles(name) VALUES('ROLE_ADMIN');
```
