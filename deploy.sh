#!/bin/bash

# Build the application (if needed, e.g., for Spring Boot)
# ./mvnw clean package 

# Build the Docker image
docker build -t dpatwal1991/semantic-store:tagname .

# Push the image to Docker Hub
docker push dpatwal1991/semantic-store:tagname