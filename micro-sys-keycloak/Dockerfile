FROM openjdk:13-jdk-alpine
RUN apk add --no-cache bash 
#&& apk add mc
VOLUME ["/tmp", "/ssl"]
EXPOSE 60100
ARG CONSUL_HOST=http://localhost
ARG CONSUL_PORT=8500
#ARG TRUST_STORE=classpath:ssl/root.p12
#ARG TRUST_PSW=$${property.keycloak.truststore-password}
ADD ./sys-keycloak-impl/target/sys-keycloak-impl-*-SNAPSHOT.jar sys-keycloak.jar
ENTRYPOINT ["java","-Xms128m","-Xmx192m","-Xss128m"\
#,"-Dkeycloak.truststore=${TRUST_STORE}"\
#,"-Dkeycloak.truststore-password=${TRUST_PSW}"\
,"-Dspring.cloud.consul.host=${CONSUL_HOST}"\
,"-Dspring.cloud.consul.port=${CONSUL_PORT}"\
,"-jar","/sys-keycloak.jar"]
