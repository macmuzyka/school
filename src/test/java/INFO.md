1. Each new test class in com.school package should be mentioned in AllTestsSuite, so when tests are run from that class, they are nicely grouped
2. First class in AllTestsSuite@SelectClasses must be annotated with KafkaDockerRunExtension, so Kafka message broker properly runs in background for internal communication
3. Last class in AllTestsSuite@SelectClasses must be annotated with KafkaDockerStopExtension, so Kafka message broker container is properly shut down and container removed for re-testing
4. If more run/shutdown conditions will be required in future, implementation of needed extensions is a must
5. If you want to run tests separetly, you need running kafka container instance with port exposed at 9092
6. IDEA: make new test classes that make more sense to trigger upon application launch & shutdown, so they might be associated with run/shutdown extensions e.g. WarmupDatabasePopulation class for run & saving up database backup for shutdown 