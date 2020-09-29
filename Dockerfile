FROM openjdk:11.0.8-jdk-slim
COPY target/kafka-service-broker-1.0-SNAPSHOT.jar app.jar
RUN apt-get update
RUN apt-get install -y curl
ENV SPRING_PROFILES_ACTIVE jeqo
ENTRYPOINT ["java","-jar","/app.jar"]
