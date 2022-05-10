FROM openjdk:17
ADD target/CurrencyExchange-0.0.1-SNAPSHOT.jar .
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "CurrencyExchange-0.0.1-SNAPSHOT.jar"]