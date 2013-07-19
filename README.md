# Welcome to Jetty 9 on CloudBees

This is a "ClickStart" that gets you going with a Maven - Jetty 9 "seed" project starting point. You can launch it here:

<a href="https://grandcentral.cloudbees.com/?CB_clickstart=https://raw.github.com/fbelzunc/jetty9-clickstart/master/clickstart.json"><img src="https://d3ko533tu1ozfq.cloudfront.net/clickstart/deployInstantly.png"/></a>

This will setup a continuous deployment pipeline - a CloudBees Git repository, a Jenkins build compiling and running the test suite (on each commit).
Should the build succeed, this seed app is deployed on a Jetty 9 container.

# CloudBees Jetty 9 container

Jetty 9 container is available on CloudBees thanks to the [jetty9-clickstack](https://github.com/CloudBees-community/tomcat7-clickstack). Documentation is available [here](https://developer.cloudbees.com/bin/view/RUN/Tomcat7).

# How to deploy a web application on a Jetty9 ClickStack

You can deploy your web application on the jetty9 clickstack using the [CloudBees SDK](https://developer.cloudbees.com/bin/view/RUN/BeesSDK) "`app:deploy`" command.

```
bees app:deploy -a <ACCOUNT_ID>/<APP_ID> -t jetty9 -RPLUGIN.SRC.jetty9=https://felix.ci.cloudbees.com/job/jetty9-clickstack/lastSuccessfulBuild/artifact/jetty9-plugin.zip <PATH_TO_WAR_FILE>.war
```

# How to bind a CloudBees MySql database to an application on a Jetty9 ClickStack

## Create database if needed
```
db:create --username my-username --password alpha-beta jetty9-maven-clickstart-db
```

## Bind application to database

```
bees app:bind -a <APP_ID> -db jetty9-maven-clickstart-db -as mydb
```
* "`-a <APP_ID>`": the name of your application
* "`-db jetty9-maven-clickstart-db`": the name of your CloudBees MySQL Database
* "`-as mydb`": the name of the binding which is used to identify the binding and to compose the name of the environment variables used to describe this binding (always prefer '_' to '-' for bindings because '-' is not supported in linux environment variable names).

This binding will create

* A JNDI DataSource with name "`java:comp/env/jdbc/mydb`" (also available at "`jdbc/mydb`")
* The following System Properties
  * `DATABASE_URL_MYDB`: url of the database starting with "mysql:" (e.g. "mysql://ec2-1.2.3.4.compute-1.amazonaws.com:3306/jetty9-maven-clickstart-db"). **Please note** that this URL is **not** prefixed by "jdbc:".
  * `DATABASE_USERNAME_MYDB`: login of the database
  * `DATABASE_PASSWORD_MYDB`: password of the database

Details on bindings are available in [Binding services (resources) to applications](https://developer.cloudbees.com/bin/view/RUN/Resource+Management).


### Use the DataSource in you application

#### Plain Java

You can now use your "`jdbc/mydb`" JNDI DataSource in your application.

Java code sample:

```java
Context ctx = new InitialContext();
DataSource ds = (DataSource) ctx.lookup("jdbc/mydb");
Connection conn = ds.getConnection();
ResultSet rst = stmt.executeQuery("select 1");
while (rst.next()) {
    out.print("resultset result: " + rst.getString(1));
}
rst.close();
stmt.close();
conn.close();
```



 




