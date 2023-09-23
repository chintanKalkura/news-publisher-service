docker build -t news-validation-api:0.0.1 -t news-validation-api:latest .

docker run -d -p 3000:3000 news-validation-api:0.0.1

docker run --name mysql-news -p 3306:3306 -e MYSQL_ROOT_PASSWORD=my-secret-pw -e MYSQL_DATABASE=newsValidationDB
-e MYSQL_USER=news-validation-api -e MYSQL_PASSWORD=NewsValidationApiPassword -d mysql:8-oracle

