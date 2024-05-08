FROM openjdk:17

ADD . /src

WORKDIR /src

RUN ./mvnw package -DskipTests

EXPOSE 8081

ENTRYPOINT ["java","-jar","target/old_remover-0.0.1-SNAPSHOT.jar"]