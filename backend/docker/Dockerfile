FROM --platform=$BUILDPLATFORM maven:3.9-eclipse-temurin-25
WORKDIR /app
COPY ../pom.xml ./pom.xml
COPY ../src/ ./src
CMD ["mvn", "spring-boot:run"]
