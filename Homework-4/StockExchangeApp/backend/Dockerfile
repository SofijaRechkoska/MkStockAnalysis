FROM openjdk:17


COPY target/*.jar /app/stockexchangeapp.jar
COPY .env /app/.env
COPY src/main/resources/scripts/python/demo/Filter3.py /app/scripts/python/demo/Filter3.py


ENV DOTENV_PATH=/app

EXPOSE 5432

ENTRYPOINT ["java", "-jar", "/app/stockexchangeapp.jar"]

