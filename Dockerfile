FROM openjdk:16
ARG JAR_FILE=./target/*.jar
COPY ${JAR_FILE} application.jar
ENTRYPOINT ["java", "-jar", "-Dspring.datasource.url=$DB_URL", "-Dspring.datasource.username=$DB_USERNAME", "-Dspring.datasource.password=$DB_PASSWORD", "-Dapp.redirect_uri=$REDIRECT_URI", "-Dapp.client_id=$CLIENT_ID", "-Dapp.client_secret=$CLIENT_SECRET", "-Dapp.front_login_url=$FRONT_URL_LOGIN", "application.jar"]
