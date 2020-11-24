FROM openjdk:8
ADD target/geoly-backend.jar geoly-backend.jar
ENTRYPOINT ["java","-jar","geoly-backend.jar"]
