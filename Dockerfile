FROM maven:3.8.3-openjdk-17
ADD src src
ADD pom.xml pom.xml
RUN mvn clean -DskipTests=true package
EXPOSE 4005
ENTRYPOINT ["java", "-jar","target/screening.jar"]