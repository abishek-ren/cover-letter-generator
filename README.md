Cover Letter Generator
=======================

An automated cover letter generator built with Spring Boot and Spring AI, utilizing the OpenAI API to generate content and returning it in a Microsoft Word file format. The application is completely Dockerized for easy deployment and includes Swagger for ease of use.

Requirements
------------

* OpenAI API key
* PostgreSQL database for storing resumes

Getting Started
---------------

1. Clone the repository:
```bash
git clone https://github.com/abishek-ren/cover-letter-generator.git
```
2. Build the Docker image:
```
docker build -t cover-letter-generator .
```
3. Run the Docker container, replacing `<API_KEY>` , `<DB_CONNECTION_STRING>`, `<DB_USERNAME>`, `<DB_PASSWORD>` with your OpenAI API key and PostgreSQL connection string, respectively:
```bash
docker run -e OPENAI_API_KEY=<API_KEY>
-e JDBC_DATABASE_URL=<DB_CONNECTION_STRING>
-e JDBC_DATABASE_USERNAME=<DB_USERNAME>
-e JDBC_DATABASE_PASSWORD=<DB_PASSWORD>
-p 8080:8080 cover-letter-generator
```
4. Access the application at `http://localhost:8080`
5. Use Swagger to make API calls to the application at `http://localhost:8080/swagger-ui.html`

API Endpoints
-------------

* `POST /cover-letters/upload`: Upload a resume
* `POST /cover-letters/generate`: Generates a new cover letter based on the provided resume Id and job Description. Given as Request Parameter.

Technologies Used
-----------------

* Spring Boot
* Spring AI
* OpenAI API
* PostgreSQL
* Docker
* Swagger



This project is licensed under the [MIT License](LICENSE)

Author
------

Abishek Rengarajan (https://github.com/abishek-ren)
