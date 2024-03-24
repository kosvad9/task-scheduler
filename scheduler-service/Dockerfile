FROM gradle:jdk17-focal as build
WORKDIR /task-scheduler
COPY /build.gradle .
COPY /settings.gradle .
COPY /core ./core
COPY /scheduler-service ./scheduler-service
RUN gradle :scheduler-service:bootJar

FROM ubuntu/jre:17_edge as java

FROM ubuntu:24.04
LABEL authors="kosvad9"
VOLUME /tmp
WORKDIR /
COPY --from=build /task-scheduler/scheduler-service/build/libs/scheduler-service.jar .
COPY --from=java /opt/java/ ./opt/java
ENV JAVA_HOME=/opt/java/
ENV PATH=$PATH:/opt/java/bin:/opt/java/lib
ENV KAFKA_URL=kafka-server:9092
RUN echo which bash >> runapp
RUN echo java -jar scheduler-service.jar --spring.kafka.bootstrap-servers='$KAFKA_URL' >> runapp
CMD ["bash","runapp"]