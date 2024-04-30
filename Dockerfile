FROM openjdk:17
ARG JAR_FILE=C:\Users\lenovo\.jenkins\workspace\test-maven-build\target\Product-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
