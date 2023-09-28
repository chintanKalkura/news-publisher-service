Redis

```docker run --name redis -d -p 6379:6379 redis:latest```

Kafka

```E:\kafka_2.13-3.5.0\bin\windows>zookeeper-server-start.bat zookeeper.properties```

```E:\kafka_2.13-3.5.0\bin\windows>kafka-server-start.bat server.properties```

```E:\kafka_2.13-3.5.0\bin\windows>kafka-console-consumer.bat --topic publishedArticles --from-beginning --bootstrap-server localhost:9092```

Cassandra

```docker run --name cassandra -d -p 9042:9042 cassandra:latest```

```docker exec -it cassandra bash```

```cqlsh```

```CREATE KEYSPACE newspaper WITH replication = {'class' : 'SimpleStrategy', 'replication_factor' : 1};```