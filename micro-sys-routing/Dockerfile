FROM openjdk:8-jdk-alpine
VOLUME /tmp
EXPOSE 7777
ARG CONSUL_HOST=http://localhost
ARG CONSUL_PORT=8500
ADD target\sys-routing-*-SNAPSHOT.jar sys-routing.jar
ENTRYPOINT ["java","-Dspring.cloud.consul.host=${CONSUL_HOST}","-Dspring.cloud.consul.port=${CONSUL_PORT}","-jar","/sys-routing.jar"]
