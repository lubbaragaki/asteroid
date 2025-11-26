scalaVersion := "3.3.7"
version := "1.12.0"
name := "asteroid"
libraryDependencies ++= Seq(
        "com.lihaoyi" %% "fansi" % "0.5.1",
        "org.virtuslab" %% "scala-yaml" % "0.3.1"
)
addCommandAlias("ci","compile;test;run")
