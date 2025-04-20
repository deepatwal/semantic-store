# Java API Service Starter

This is a minimal Java API service starter based on [Google Cloud Run Quickstart](https://cloud.google.com/run/docs/quickstarts/build-and-deploy/deploy-java-service).

## Getting Started

**Docker**

The following commands are for building and running the application using Docker.

**Build the Docker image:**

docker build -t demo-app .
docker run -p 8080:8080 demo-app

**Build the application:**

Server should run automatically when starting a workspace. To run manually, run:
```sh
mvn spring-boot:run
```