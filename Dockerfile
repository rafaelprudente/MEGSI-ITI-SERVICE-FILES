FROM bellsoft/liberica-openjre-debian:21

WORKDIR /opt
ENV SERVER_PORT=5081
ENV LOG_LEVEL=INFO

EXPOSE 5081

COPY target/*.jar /opt/app.jar

ENTRYPOINT ["/bin/sh", "-c", "java -jar /opt/app.jar"]