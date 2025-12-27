# MEGSI-ITI-SERVICE-FILES

### MARIADB

```
docker exec -it mariadb mariadb -u root -p

CREATE DATABASE ITI;
```

### MIGRATIONS

```
mvn flyway:migrate \
-Dflyway.url=jdbc:mariadb://localhost:3306/ITI \
-Dflyway.user=root \
-Dflyway.password=uminho
```

### MEGSI-ITI-SERVICE-FILES

```
docker build -t "rafaelrpsantos/megsi-iti-service-files:latest" .
```

```
docker run -d \
  --name megsi-iti-service-files \
  --network megsi-net \
  -p 8081:8081 \
  -e SPRING_CLOUD_CONFIG_URI=http://megsi-config-server:8888 \
  -e MARIADB_SERVER_URI=mariadb \
  -e MYSQL_ROOT_PASSWORD=uminho \
  -e KAFKA_SERVER_URI=kafka:29092 \
  -v /Users/rafaelrolimprudentedossantos/work/studies/mestrado/ITI/UMDRIVEFILES:/mnt/NAS \
  rafaelrpsantos/megsi-iti-service-files:latest
```