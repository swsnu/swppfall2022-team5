FROM openjdk

RUN microdnf install findutils

WORKDIR /home

COPY ./ /home

EXPOSE 8080

RUN ./gradlew clean bootJar -x test

ENTRYPOINT java -jar build/libs/*.jar --spring.profiles.active=prod