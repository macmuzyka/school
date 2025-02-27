#### School managing application for storing students grouped into classes, adding grades, tracking grades, producing various files with grades & average values

##### Project dependencies:

1. PostgresSQL database
    - default 5432 port, school database
2. Kafka message broker
    - default 9002 port, preferably set up with docker `docker run -p 9092:9092 -d bashj79/kafka-kraft`
3. [OPTIONAL] school-informer module for feedback
   propagation -> [school-informer project](https://github.com/macmuzyka/school-informer)

###### In devel profile h2 memory database is used & grade adding via kafka is omitted

### Application able to run win docker, get school & school informer projects and run docker-compose.yml file from school project:

1. `mvn clean install` for school project, `mvn clean install -DskipTests` as tests are not implemented in informer yet 
2. `docker build -t school .` in school project
3. `docker build -t informer .` in school-informer project
4. `docker-compose up --build` from school project where docker-compose.yml is located 
