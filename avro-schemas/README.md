# Apache Avro™ 1.10.1 Getting Started (Java)

From https://avro.apache.org/docs/current/gettingstartedjava.html

# Avro schema

Let's use the one from the tutorial itself: user.avsc

```
{"namespace": "example.avro",
 "type": "record",
 "name": "User",
 "fields": [
     {"name": "name", "type": "string"},
     {"name": "favorite_number",  "type": ["int", "null"]},
     {"name": "favorite_color", "type": ["string", "null"]}
 ]
}
```
