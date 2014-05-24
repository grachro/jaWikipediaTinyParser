name := "jaWikipediaTinyParser"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  javaJdbc,
  javaEbean,
  cache,
  "commons-io" % "commons-io" % "2.4",
  "commons-dbutils" % "commons-dbutils" % "1.5",
  "mysql" % "mysql-connector-java" % "5.1.30"
)     

play.Project.playJavaSettings
