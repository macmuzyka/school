FROM openjdk:17-jdk-slim

WORKDIR /app
RUN mkdir -p logs backup backup_scheduled produced_files seed_performance config
RUN apt-get update && apt-get install -y netcat && rm -rf /var/lib/apt/lists/*

COPY target/*.jar school-application.jar
COPY pom.xml pom.xml
COPY src/main/resources/application-secrets.properties /config/application-secrets.properties
COPY src/main/resources/application.properties /config/application.properties
COPY wait_for_informer.sh wait_for_informer.sh

RUN chmod +x wait_for_informer.sh

EXPOSE 9000
ENTRYPOINT ["./wait_for_informer.sh"]