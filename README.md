# LearningAkka
Learn akka and make some practices

# How to build and run
1.required<br/>

* scala 2.11+
* java 1.7+
* akka 2.3.14+(maven will download it)

2.run<br/>

```shell
mvn clean && mvn package
scala -cp target/learning-akka-1.0-SNAPSHOT.jar com.learning.akka.hello.Main
scala -cp target/learning-akka-1.0-SNAPSHOT.jar com.learning.akka.hello.Main2
```
