FROM adoptopenjdk/openjdk11:alpine-jre
MAINTAINER alsvietnam.org
WORKDIR /opt/app
COPY alsvietnam-article-service/target/alsvietnam-service.war /opt/app/alsvietnam-service.war
WORKDIR /opt/alsvietnam/uploads
WORKDIR /opt/firebase
COPY alsvietnam-article-service/src/main/resources/data/alsvietnam-firebase.json /opt/firebase/alsvietnam-firebase.json
ENV SPRING_PROFILES_ACTIVE=prod
ENTRYPOINT ["java", "-jar", "/opt/app/alsvietnam-service.war"]