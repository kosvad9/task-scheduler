FROM gradle:jdk17-focal as build
WORKDIR /task-scheduler
COPY /build.gradle .
COPY /settings.gradle .
COPY /core ./core
COPY /mail-sender-service ./mail-sender-service
RUN gradle :mail-sender-service:bootJar

FROM ubuntu/jre:17_edge as java

FROM ubuntu:24.04
LABEL authors="kosvad9"
VOLUME /tmp
WORKDIR /
COPY --from=build /task-scheduler/mail-sender-service/build/libs/mail-sender-service.jar .
COPY --from=java /opt/java/ ./opt/java
ENV JAVA_HOME=/opt/java/
ENV PATH=$PATH:/opt/java/bin:/opt/java/lib
ENV KAFKA_URL=kafka-server:9092
ENV SMTP_MAIL_FROM=mail@mail.com
ENV SMTP_LOGIN=login
ENV SMTP_PASSWORD=password
RUN echo which bash >> runapp
RUN echo java -jar mail-sender-service.jar --spring.kafka.bootstrap-servers='$KAFKA_URL' \\ >> runapp
RUN echo --mail.from='$SMTP_MAIL_FROM' --spring.mail.username='$SMTP_LOGIN' ----spring.mail.password='$SMTP_PASSWORD' >> runapp
CMD ["bash","runapp"]