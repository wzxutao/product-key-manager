FROM adoptopenjdk/maven-openjdk11:latest
COPY src /opt/pkm/src
COPY resources /opt/pkm/resources
COPY pom.xml /opt/pkm/pom.xml
COPY m2Settings.xml /root/.m2/settings.xml
COPY m2Settings.xml /usr/share/maven/ref/settings.xml
EXPOSE 8081
WORKDIR /opt/pkm/
RUN mvn clean install -DskipTests
ENTRYPOINT cd /opt/pkm; mvn spring-boot:run