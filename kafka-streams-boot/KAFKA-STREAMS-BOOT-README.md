# NOTES:

## Error when running in IntelliJ

```
org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'jmxMBeanExporter' defined in class path resource [org/springframework/boot/actuate/autoconfigure/endpoint/jmx/JmxEndpointAutoConfiguration.class]: Bean instantiation via factory method failed; nested exception is org.springframework.beans.BeanInstantiationException: Failed to instantiate [org.springframework.boot.actuate.endpoint.jmx.JmxEndpointExporter]: Factory method 'jmxMBeanExporter' threw exception; nested exception is java.lang.IllegalStateException: Unable to map duplicate endpoint operations: [MBean call 'kafkaStreamsTopology'] to topologyEndpoint
	at org.springframework.beans.factory.support.ConstructorResolver.instantiate(ConstructorResolver.java:655) ~[spring-beans-5.2.9.RELEASE.jar:5.2.9.RELEASE]
...
Caused by: org.springframework.beans.BeanInstantiationException: Failed to instantiate [org.springframework.boot.actuate.endpoint.jmx.JmxEndpointExporter]: Factory method 'jmxMBeanExporter' threw exception; nested exception is java.lang.IllegalStateException: Unable to map duplicate endpoint operations: [MBean call 'kafkaStreamsTopology'] to topologyEndpoint
	at org.springframework.beans.factory.support.SimpleInstantiationStrategy.instantiate(SimpleInstantiationStrategy.java:185) ~[spring-beans-5.2.9.RELEASE.jar:5.2.9.RELEASE]
	at org.springframework.beans.factory.support.ConstructorResolver.instantiate(ConstructorResolver.java:650) ~[spring-beans-5.2.9.RELEASE.jar:5.2.9.RELEASE]
	... 20 common frames omitted
Caused by: java.lang.IllegalStateException: Unable to map duplicate endpoint operations: [MBean call 'kafkaStreamsTopology'] to topologyEndpoint
	at org.springframework.boot.actuate.endpoint.annotation.EndpointDiscoverer.assertNoDuplicateOperations(EndpointDiscoverer.java:234) ~[spring-boot-actuator-2.3.4.RELEASE.jar:2.3.4.RELEASE]
...
	... 21 common frames omitted

```

Edit configurations... -> APP -> uncheck Enable JMX agent in Project Configurations

## Run the APP

```
$ echo -e "all streams lead to kafka\nhello kafka streams\njoin kafka summit" > /tmp/file-input.txt

$ cat /tmp/file-input.txt | ./bin/kafka-console-producer --broker-list localhost:9092 --topic cloudkafkastreams-input-topic

$ ./bin/kafka-console-consumer --bootstrap-server localhost:9092 --topic cloudkafkastreams-words-topic --from-beginning
```