scalaVersion := "2.13.17"
version := "1.12.0"
name := "asteroid"
libraryDependencies ++= Seq(
        "com.lihaoyi" %% "fansi" % "0.5.1"
)
addCommandAlias("ci","compile;test;run")
