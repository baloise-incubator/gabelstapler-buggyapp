FROM eclipse-temurin:17-jdk
VOLUME /tmp
COPY target/*.jar app.jar
COPY target/lib/agent-*.jar pyroscope.jar
#ENTRYPOINT ["java","-javaagent:pyroscope.jar", "-jar","/app.jar"]
ENTRYPOINT ["java", "-jar","/app.jar"]