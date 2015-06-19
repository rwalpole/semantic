## Semantic Search Tool

Currently this application allows you ask questions about someone's place of birth by formulating questions such as "What is the birth place of David Cameron?" or "Where was David Cameron born?".

To use the application you first need to build the jar file by running the following Maven command:

    mvn clean compile assembly:single

You can then run the executable jar file found in the target directory, for example:

    java -jar SemanticRecruitmentTest-0.0.1-SNAPSHOT-jar-with-dependencies.jar

In order to run the program you will need a Java JDK (version 1.7+) and Apache Maven (version 3+) installed.
