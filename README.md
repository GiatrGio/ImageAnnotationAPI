# Image Annotation API

Image Annotation API with endpoints for users to upload images, comment and annotate them.

Please take a look on the Notes section for more information about the project.

## Table of Contents

- [How to run the project with Docker](#how-to-run-the-project-with-docker)
    - [Prerequisites](#prerequisites)
    - [Clone the Repository](#clone-the-repository)
    - [Build and run the application](#build-and-run-the-application)
- [How to build and run the project using maven](#how-to-build-and-run-the-project-using-maven)
    - [Prerequisites](#prerequisites-1)
    - [Build the Project](#build-the-project-1)
    - [Start the Docker MySQL Database](#start-the-docker-mysql-database)
    - [Run the Application](#run-the-application-1)
    - [Run all Tests](#run-all-tests)
- [Access the application](#access-the-application)
- [Notes](#notes)
    - [About the project](#about-the-project)
    - [Alternative solutions](#alternative-solutions)


## How to run the project with Docker

### Prerequisites
[Docker](https://www.docker.com/) installed.

### Clone the Repository

```bash
git clone https://github.com/GiatrGio/ImageAnnotationAPI.git
cd imageAnnotationAPI
```

### Build and run the application
Run the following command to build and start the containers:

```bash
docker-compose up --build
```

## How to build and run the project using maven

### Prerequisites

- Java Development Kit (JDK) 8 or higher installed.
- Apache Maven installed (for building the project).
  [Docker](https://www.docker.com/) installed. (For the database)

### Build the Project

```bash
mvn clean install
```
### Start the Docker MySQL Database

```bash
docker run --name mysql-container -e MYSQL_ROOT_PASSWORD=password -e MYSQL_DATABASE=imageAnnotationDB -e MYSQL_USER=admin -e MYSQL_PASSWORD=password -d -p 3306:3306 mysql:latest
```

### Run the Application

```bash
mvn spring-boot:run
```

### Run all Tests
```bash
mvn test
```

## Access the application
The API will be accessible at `http://localhost:8080`.

The Swagger UI will be accessible at `http://localhost:8080/swagger-ui.html`.

The MySQL database will be accessible at `http://localhost:3306`.

## Notes

### About the project

- The application is a Spring boot application with a MySQL database. Both the application and the database are running in separate Docker containers.
- The first 5 requirements of the assignment are implemented. For the rest there was no time but their implementation can be discussed. 

### Alternative solutions
- MySQL was chosen as the database because it is a widely used relational database and it is easy to set up and use. If the requirements were different, 
for instance if we had to store billions of images and users then the JOIN queries in MySQL would become slow and a NoSQL database could be used instead.
For instance a search engine like Elasticsearch could be used to store the images and all their information like comments and annotations in a JSON structure.
This would make the jobs of retrieving image information faster and more efficient.
Or a document database like MongoDB could be used to store the images and their information.
- To simulate the image annotation, I created an async method called generateAndAddAnnotationsToImage in the ImageService class. 
This method generates random annotations for an image and adds them to the database. In a real application the method would probably add the job in a
message broker and the annotations would be generated by a separate service.

