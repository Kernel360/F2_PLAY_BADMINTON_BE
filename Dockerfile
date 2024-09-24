FROM amazoncorretto:17

LABEL authors="Kangmin Lee"

COPY build/libs/badminton-api.jar /docker-springboot.jar

ENTRYPOINT ["java", "-jar", "/docker-springboot.jar"]