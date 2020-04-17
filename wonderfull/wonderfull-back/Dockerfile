FROM adoptopenjdk/openjdk11:alpine-jre
WORKDIR /app
COPY /build/libs/iaww.jar /application-docker.yaml /app/
ENV SPRING_PROFILES_ACTIVE=docker
EXPOSE 80
ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-XX:MaxRAMPercentage=90.0", "-jar", "/app/iaww.jar"]
