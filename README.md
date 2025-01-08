#### School managing application for storing students grouped into classes, adding grades, tracking grades, producing various files with grades & average values
##### Project dependencies:
1. PostgresSQL database
   - default 5432 port, school database
2. Kafka message broker
   - default 9002 port
3. [OPTIONAL] school-informer module for feedback propagation -> [school-informer project](https://github.com/macmuzyka/school-informer)

In devel profile h2 memory database is used, grade adding via kafka is omitted
