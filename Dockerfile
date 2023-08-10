FROM openjdk:13-jdk-alpine
COPY ./build/libs/everest-1.0.war /usr/app/
WORKDIR /usr/app
EXPOSE 8080
ENTRYPOINT ["java", "-Dgrails.env=prod", "-jar", "everest-1.0.war"]