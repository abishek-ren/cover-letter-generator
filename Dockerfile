FROM openjdk:21
EXPOSE 8080
ADD /target/ai1-0.0.1-SNAPSHOT.jar cover-letter-generator.jar
ENTRYPOINT ["java", "-jar", "ai1-0.0.1-SNAPSHOT.jar"]
