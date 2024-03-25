# spring-academy-test
Test task for Spring Academy. CRUD application for a products warehouse

## Instructions for Running the Application

### 🛠 Prerequisites & Requirements

Ensure you have the following installed:
- [![java11](https://img.shields.io/badge/Language-Java%2011-green)](https://www.oracle.com/java/technologies/downloads/#java11)
- [![PostgreSQL](https://img.shields.io/badge/Database-PostgreSQL-red)](https://www.postgresql.org/download/)
- [![Apache Maven](https://img.shields.io/badge/Builder-Apache_Maven-blue)](https://maven.apache.org/download.cgi)

### ⭳ Download

1. Open a terminal or command line.
2. Clone the project:

```bash
  git clone https://github.com/shakowsx/spring-academy-test
```

Or clone project from 'develop' branch if needed:
```bash
git clone -b develop https://github.com/shakowsx/spring-academy-test
```

### &#9881; Environment Setup & Configuration
1. The application is configured to start under a separate <code>dev</code> profile.
The settings are in the <code>application-dev.properties</code> file.
You can change this in your <code>application.properties</code> file using the <code>spring.profiles.active</code> 
setting.
2. The application uses a PostgreSQL database. So you need to configure it to work with the database.
   - Create a new empty database.
   - In the <code>application-dev.properties</code> settings, specify the name of the created database and access
     (username and password) to the database itself (If you are not using a dev profile, then configure file
   "application.properties" instead).
3. The application uses server port <code>8080</code> by default, if you want to use a different port, then change the settings 
in the properties file.

### ᯓ★ Installation & Running the Application
1. Open a terminal or command line.
2. Go to the root directory of the project. Then build the project via Maven using the following command:
```
mvn clean package
```
3. Navigate to the directory where the application is located ("target" folder if you are running the application 
from the project folder).
5. Ensure your PostgreSQL database is running and configured according to the settings.
4. Run the command to start the application:
```
java -jar spring-academy-test-1.0-SNAPSHOT.jar
```
This command will launch your application on the port specified in application.properties (default is 8080).
5. After running the application, you should see output in the console indicating that the application
launched successfully.
6. Now you can send the necessary requests to http://localhost:8080/api/products

### ▶ Using the Application

You can familiarize yourself with the instructions for using this API using the swagger, which will be available
after launching the application via this link http://localhost:8080/swagger-ui/

### ✦ Additional Information

- If you encounter issues while running the application, ensure that all necessary dependencies are installed and
configured correctly.
- This application uses the following dependencies:
  - spring-boot-starter-data-jpa
  - spring-boot-starter-web
  - postgresql
  - lombok
  - jackson-annotations
  - validation-api
  - hibernate-validator
  - junit-jupiter
  - spring-boot-starter-test
  - spring-boot-test-autoconfigure
  - mockito-core
- The Postman collection with queries and tests is located in the project's "resources" folder.

### ✒ Authors

- [Евгений Рябов aka @Shako](https://www.github.com/shakowsx)