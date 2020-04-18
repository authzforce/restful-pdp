FROM openjdk:8-jre-alpine
LABEL maintainer="AuthzForce Team"
LABEL org.label-schema.schema-version = "1.0"
LABEL org.label-schema.vendor = "THALES"
LABEL org.label-schema.name="AuthzForce RESTful PDP"
# LABEL org.label-schema.description=""

# Guidelines: https://spring.io/guides/gs/spring-boot-docker/

VOLUME /tmp

ARG JAR_VERSION
ENV JAR_URL="https://repo1.maven.org/maven2/org/ow2/authzforce/authzforce-ce-restful-pdp-cxf-spring-boot-server/${JAR_VERSION}/authzforce-ce-restful-pdp-cxf-spring-boot-server-${JAR_VERSION}.jar"

RUN apk --no-cache add curl &&\
    curl --verbose --location $JAR_URL --output /app.jar &&\
    addgroup -S spring && adduser -S spring -G spring
USER spring:spring

COPY extensions /extensions

EXPOSE 8080
EXPOSE 8443

ENV JAVA_OPTS="-Dloader.path=/extensions -Djava.security.egd=file:/dev/./urandom -Djava.awt.headless=true -Djavax.xml.accessExternalSchema=all -Xms1024m -Xmx2048m -XX:+UseConcMarkSweepGC -server"
CMD java ${JAVA_OPTS} -jar /app.jar --spring.config.location=classpath:/,file:/conf/application.yml