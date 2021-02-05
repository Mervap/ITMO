plugins {
  kotlin("multiplatform") version "1.4.21" apply false
  id("org.hidetake.ssh") version "2.10.1"
}

allprojects {
  version = "0.1.0"

  repositories {
    mavenCentral()
    jcenter()
    maven("https://dl.bintray.com/kotlin/kotlinx")
  }
}

tasks.register<Copy>("stage") {
  dependsOn("server:build")

  destinationDir = File("build/dist")

  from(tarTree("server/build/distributions/server-0.1.0.tar"))
  from("server/build") {
    include("resources/**")
  }
}

task("awsDeploy") {
  dependsOn("stage")

  val awsProperties = java.util.Properties()
  awsProperties.load(project.rootProject.file("local.properties").inputStream())

  val awsUser = awsProperties.getProperty("aws.hostUser") as String
  val awsHost = awsProperties.getProperty("aws.host") as String
  val port = awsProperties.getProperty("aws.port") as String
  val dbHost = awsProperties.getProperty("aws.dbHost") as String
  val dbUser = awsProperties.getProperty("aws.dbUser") as String
  val dbPassword = awsProperties.getProperty("aws.dbPassword") as String

  val awsRemote = remotes.create("awsRemote") {
    host = awsHost
    user = awsUser
    passphrase = ""
    identity = File("${System.getProperty("user.home")}/.ssh/aws/kcs.pem")
  }

  doLast {
    ssh.run(delegateClosureOf<org.hidetake.groovy.ssh.core.RunHandler> {
      session(awsRemote, delegateClosureOf<org.hidetake.groovy.ssh.session.SessionHandler> {
        logger.lifecycle("Killing existing jvms")
        execute("sudo killall java || true")

        logger.lifecycle("Create a directory")

        execute("rm -rf ~/app || true")
        execute("mkdir -p ~/app || true")

        val distDir = File(rootDir, "build/dist")

        logger.lifecycle("Copy bundle to $awsHost")

        put(
          hashMapOf(
            "from" to distDir.absolutePath,
            "into" to "/home/$awsUser/app",
            "fileTransfer" to "scp"
          )
        )

        logger.lifecycle("Run server...")

        execute("cd /home/$awsUser/app/dist; nohup sudo bash server-0.1.0/bin/server --host \"$awsHost\" --port \"$port\" --dbHost \"$dbHost\" --dbUser \"$dbUser\" --dbPassword \"$dbPassword\" > /dev/null 2>&1 &")
      })
    })
  }
}