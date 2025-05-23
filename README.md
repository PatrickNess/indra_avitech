# indra_avitech
Project demonstrates Producer Consumer design pattern in Java

# Architecture
The project implements the Producer-Consumer design pattern in Java. CommandProducer and CommandConsumer are generic classes
capable of executing any command that implements the ExecutableCommand interface. The Command pattern is used for command implementations.
The UserDbService manages database connections, creates the SUSERS table during initialization, and provides methods for database operations.
Each command holds a reference to UserDbService to perform the necessary actions.
The uniqueness of user IDs and GUIDs is enforced at the database level.

## Build
This project utilizes Maven for automation and dependency management. You can use the following commands:
- mvn clean: Removes generated build artifacts and temporary files.
- mvn compile: Compiles the project's source code.
- mvn test: Executes the project's tests.
- mvn package: Bundles the compiled code and resources into a distributable format (such as a JAR or WAR).
- mvn install: Installs the packaged artifact into your local Maven repository for use in other projects.
- mvn dependency:tree: Shows the project's dependency tree, including transitive dependencies.