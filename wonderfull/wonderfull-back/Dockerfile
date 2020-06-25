FROM adoptopenjdk/openjdk11:alpine-jre as builder
WORKDIR /app
COPY /build/libs/*.jar ./
RUN java -Djarmode=layertools -jar *.jar extract

FROM adoptopenjdk/openjdk11:alpine-jre
WORKDIR /app
COPY --from=builder app/dependencies/ ./
COPY --from=builder app/spring-boot-loader/ ./
COPY --from=builder app/snapshot-dependencies/ ./
COPY --from=builder app/application/ ./
COPY /application-docker.yaml ./
EXPOSE 8080
ENV SPRING_PROFILES_ACTIVE=docker
ENTRYPOINT ["java",  "-XX:+UseContainerSupport", "-XX:MaxRAMPercentage=90.0", "org.springframework.boot.loader.JarLauncher"]
