FROM openjdk:13-jdk-alpine
# RUN apk add --no-cache bash && apk add mc
VOLUME /tmp
EXPOSE 60100
ARG CONSUL_HOST=http://localhost
ARG CONSUL_PORT=8500
ADD ./sys-keycloak-impl/target/sys-keycloak-impl-*-SNAPSHOT.jar sys-keycloak.jar
ENTRYPOINT ["java","-Xms128m","-Xmx192m","-Xss128m","-Dspring.cloud.consul.host=${CONSUL_HOST}","-Dspring.cloud.consul.port=${CONSUL_PORT}","-jar","/sys-keycloak.jar"]
