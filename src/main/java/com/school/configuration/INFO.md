### Kafka is being introduced to this project only for practice purpose, as producing & consuming messages among one service is an overkill 

#### Kafka message broker used is used in this project to produce message about grade being added to a student
#### Grade added to a student is passed via GUI to a controller, then message is produced by kafka is sent to a topic & then consumed by a consumer
#### Notification about grade added successfully or not is sent via web-socket to frontend
#### Kafka is also being used to pass feedback & feedback provider email to central registry for storage & in future to propagate information among feedback providers about new application version with new functionalities
###### Messaging via kafka is disabled in devel profile