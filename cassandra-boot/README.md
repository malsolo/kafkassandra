# Problems

¡Mal emepzamos!

## Java 14

$ mvn verify

```
java.lang.NoClassDefFoundError: Could not initialize class org.codehaus.groovy.vmplugin.v7.Java7
        at org.codehaus.groovy.vmplugin.VMPluginFactory.<clinit>(VMPluginFactory.java:43)
...
        at groovy.lang.GroovySystem.<clinit>(GroovySystem.java:36)
        at org.springframework.beans.factory.groovy.GroovyBeanDefinitionReader.<init>(GroovyBeanDefinitionReader.java:150)
        at org.springframework.boot.BeanDefinitionLoader.<init>(BeanDefinitionLoader.java:85)
        at org.springframework.boot.SpringApplication.createBeanDefinitionLoader(SpringApplication.java:738)
        at org.springframework.boot.SpringApplication.load(SpringApplication.java:681)
        at org.springframework.boot.SpringApplication.prepareContext(SpringApplication.java:392)
        at org.springframework.boot.SpringApplication.run(SpringApplication.java:314)
        at org.springframework.boot.test.context.SpringBootContextLoader.loadContext(SpringBootContextLoader.java:120)
...
        at org.apache.maven.surefire.booter.ForkedBooter.execute(ForkedBooter.java:126)
        at org.apache.maven.surefire.booter.ForkedBooter.main(ForkedBooter.java:418)  
```

## Java 13, empty application.properties

```
2020-10-22 09:00:53.312  INFO 7836 --- [           main] .s.d.r.c.RepositoryConfigurationDelegate : Finished Spring Data repository scanning in 3ms. Found 0 Cassandra repository interfaces.
2020-10-22 09:00:54.606  INFO 7836 --- [           main] c.d.o.d.i.core.DefaultMavenCoordinates   : DataStax Java driver for Apache Cassandra(R) (com.datastax.oss:java-driver-core) version 4.6.1
2020-10-22 09:00:55.509  INFO 7836 --- [     s0-admin-0] c.d.oss.driver.internal.core.time.Clock  : Using native clock for microsecond precision
2020-10-22 09:00:55.785  INFO 7836 --- [        s0-io-0] c.d.o.d.i.core.channel.ChannelFactory    : [s0] Failed to connect with protocol DSE_V2, retrying with DSE_V1
2020-10-22 09:00:55.811  INFO 7836 --- [        s0-io-1] c.d.o.d.i.core.channel.ChannelFactory    : [s0] Failed to connect with protocol DSE_V1, retrying with V4
2020-10-22 09:00:55.854  INFO 7836 --- [        s0-io-2] c.d.oss.driver.api.core.uuid.Uuids       : PID obtained through native call to getpid(): 7836
2020-10-22 09:00:58.272  WARN 7836 --- [           main] r.c.GenericReactiveWebApplicationContext : Exception encountered during context initialization - cancelling refresh attempt: org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'webHandler' defined in class path resource [org/springframework/boot/autoconfigure/web/reactive/WebFluxAutoConfiguration$EnableWebFluxConfiguration.class]: Initialization of bean failed; nested exception is org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'webEndpointReactiveHandlerMapping' defined in class path resource [org/springframework/boot/actuate/autoconfigure/endpoint/web/reactive/WebFluxEndpointManagementContextConfiguration.class]: Bean instantiation via factory method failed; nested exception is org.springframework.beans.BeanInstantiationException: Failed to instantiate [org.springframework.boot.actuate.endpoint.web.reactive.WebFluxEndpointHandlerMapping]: Factory method 'webEndpointReactiveHandlerMapping' threw exception; nested exception is org.springframework.beans.factory.UnsatisfiedDependencyException: Error creating bean with name 'healthEndpoint' defined in class path resource [org/springframework/boot/actuate/autoconfigure/health/HealthEndpointConfiguration.class]: Unsatisfied dependency expressed through method 'healthEndpoint' parameter 0; nested exception is org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'healthContributorRegistry' defined in class path resource [org/springframework/boot/actuate/autoconfigure/health/HealthEndpointConfiguration.class]: Bean instantiation via factory method failed; nested exception is org.springframework.beans.BeanInstantiationException: Failed to instantiate [org.springframework.boot.actuate.health.HealthContributorRegistry]: Factory method 'healthContributorRegistry' threw exception; nested exception is org.springframework.beans.factory.UnsatisfiedDependencyException: Error creating bean with name 'cassandraHealthContributor' defined in class path resource [org/springframework/boot/actuate/autoconfigure/cassandra/CassandraReactiveHealthContributorAutoConfiguration.class]: Unsatisfied dependency expressed through method 'cassandraHealthContributor' parameter 0; nested exception is org.springframework.beans.factory.UnsatisfiedDependencyException: Error creating bean with name 'reactiveCassandraTemplate' defined in class path resource [org/springframework/boot/autoconfigure/data/cassandra/CassandraReactiveDataAutoConfiguration.class]: Unsatisfied dependency expressed through method 'reactiveCassandraTemplate' parameter 0; nested exception is org.springframework.beans.factory.UnsatisfiedDependencyException: Error creating bean with name 'reactiveCassandraSession' defined in class path resource [org/springframework/boot/autoconfigure/data/cassandra/CassandraReactiveDataAutoConfiguration.class]: Unsatisfied dependency expressed through method 'reactiveCassandraSession' parameter 0; nested exception is org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'cassandraSession' defined in class path resource [org/springframework/boot/autoconfigure/cassandra/CassandraAutoConfiguration.class]: Bean instantiation via factory method failed; nested exception is org.springframework.beans.BeanInstantiationException: Failed to instantiate [com.datastax.oss.driver.api.core.CqlSession]: Factory method 'cassandraSession' threw exception; nested exception is java.lang.IllegalStateException: Since you provided explicit contact points, the local DC must be explicitly set (see basic.load-balancing-policy.local-datacenter in the config, or set it programmatically with SessionBuilder.withLocalDatacenter). Current contact points are: Node(endPoint=/127.0.0.1:9042, hostId=ab45ebb4-ab00-4563-a146-1b843edb0328, hashCode=5b9bd649)=datacenter1. Current DCs in this cluster are: datacenter1
2020-10-22 09:00:58.377  INFO 7836 --- [           main] ConditionEvaluationReportLoggingListener : 

Error starting ApplicationContext. To display the conditions report re-run your application with 'debug' enabled.
2020-10-22 09:00:58.395 ERROR 7836 --- [           main] o.s.boot.SpringApplication               : Application run failed
```

## Java 13 and application.properties with...

```
spring.data.cassandra.keyspace-name=demo
spring.data.cassandra.contact-points=localhost:9042
spring.data.cassandra.local-datacenter=datacenter1
```

According to [Spring boot Cassandra: Connecting to Cassandra](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#boot-features-connecting-to-cassandra)

For knowing the datacenter, use cqlsh:
```
select data_center from system.local;


data_center
-------------
datacenter1 
```

Note: for knowing the version:
```
$ bin/cqlsh
Connected to Test Cluster at 127.0.0.1:9042.
[cqlsh 5.0.1 | Cassandra 3.11.5 | CQL spec 3.4.4 | Native protocol v4]
Use HELP for help.
cqlsh>

cqlsh> show version
[cqlsh 5.0.1 | Cassandra 3.11.5 | CQL spec 3.4.4 | Native protocol v4]
```

# Test data

```
Create the keyspace and table

cqlsh> DESC keyspaces;

system_traces  system_schema  system_auth  system  system_distributed

cqlsh> CREATE KEYSPACE IF NOT EXISTS demo WITH replication = {'class': 'SimpleStrategy', 'replication_factor': '1'};

cqlsh>
 CREATE TABLE demo.stocks (
    symbol text,
    date timestamp,
    value decimal,
    PRIMARY KEY (symbol, date)
 ) WITH CLUSTERING ORDER BY (date DESC);

```
