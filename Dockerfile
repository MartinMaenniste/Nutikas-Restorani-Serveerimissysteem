FROM maven:3.9-eclipse-temurin-25

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src

CMD ["mvn", "spring-boot:run"]

# Commands from https://www.docker.com/blog/kickstart-your-spring-boot-application-development/
# Changed according to my project
