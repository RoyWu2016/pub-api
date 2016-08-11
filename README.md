
## Requirements

[JDK](http://www.oracle.com/technetwork/java/javase/downloads/index.html)
[Tomcat 7](http://tomcat.apache.org/tomcat-7.0-doc/index.html)
[Maven](https://maven.apache.org)
[commons](https://github.com/asiainspection/commons)

## Build

We need to skip tests for now

```bash
mvn clean
mvn package -Dmaven.test.skip=true
```

## Installation

Set an environment variable for Tomcat path (CATALINA_HOME)

Ask [@Allen](https://github.com/actan) for keystore file, lib folder and database credentials

```bash
cp server.xml %CATALINA_HOME%
cp context.xml %CATALINA_HOME%
cp -R lib %CATALINA_HOME%/lib
```

Change in %CATALINA_HOME%/server.xml:

```bash
keystoreFile="D:\AI_DEV\deploy_docs\local\keystore"
```

To point at the desired location of keystore file, and:

```bash
jdbcUrl="jdbc:oracle:thin:@host" user="user"
password="password" />
```
To the credentials provided
