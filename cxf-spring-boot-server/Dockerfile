FROM openjdk:11-jre-slim
LABEL maintainer="AuthzForce Team"
LABEL org.label-schema.schema-version="1.0"
LABEL org.label-schema.vendor="THALES"
LABEL org.label-schema.name="AuthzForce RESTful PDP"
# LABEL org.label-schema.description=""

# Guidelines: https://spring.io/guides/gs/spring-boot-docker/

VOLUME /tmp

RUN addgroup --system spring && adduser --system --home /home/spring --ingroup spring --disabled-password spring
USER spring:spring
WORKDIR /home/spring
ARG JAR_FILE=target/*-5.0.0.jar
COPY ${JAR_FILE} /app.jar
# COPY extensions /extensions

EXPOSE 8080
EXPOSE 8443

ENV JAVA_OPTS="-Dloader.path=/extensions -Djava.security.egd=file:/dev/./urandom -Djava.awt.headless=true -Djavax.xml.accessExternalSchema=all -Xms1024m -Xmx2048m -server"
CMD java ${JAVA_OPTS} -jar /app.jar --spring.config.location=classpath:/,file:/conf/application.yml