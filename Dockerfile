FROM gradle:jdk17-focal as build
WORKDIR /task-scheduler
COPY /src ./src
COPY /build.gradle .
COPY /settings.gradle .
COPY /core ./core
RUN gradle bootJar

FROM ubuntu/jre:17_edge as java

FROM ubuntu:24.04
LABEL authors="kosvad9"
VOLUME /tmp
WORKDIR /
COPY --from=build /task-scheduler/build/libs/task-scheduler.jar .
COPY --from=java /opt/java/ ./opt/java
ENV JAVA_HOME=/opt/java/
ENV PATH=$PATH:/opt/java/bin:/opt/java/lib
ENV KAFKA_URL=kafka-server:9092
ENV POSTGRES_URL=jdbc:postgresql://some-postgres:5432/postgres
ENV POSTGRES_USERNAME=pgusername
ENV POSTGRES_PASSWORD=pgpassword
RUN echo which bash >> runapp
RUN echo java -jar task-scheduler.jar --spring.kafka.bootstrap-servers='$KAFKA_URL' --spring.datasource.url='$POSTGRES_URL' \\ >> runapp
RUN echo --spring.datasource.username='$POSTGRES_USERNAME' --spring.datasource.password='$POSTGRES_PASSWORD' >> runapp
EXPOSE 8080
CMD ["bash","runapp"]