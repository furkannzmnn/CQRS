# POWERED BY FURKAN OZMEN
FROM openjdk:18
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} CQRS-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/CQRS-0.0.1-SNAPSHOT.jar"]
