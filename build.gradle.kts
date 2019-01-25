import com.bmuschko.gradle.docker.tasks.image.DockerBuildImage
import com.bmuschko.gradle.docker.tasks.image.DockerPushImage
import com.bmuschko.gradle.docker.tasks.image.DockerTagImage
import org.gradle.internal.impldep.org.apache.http.client.methods.RequestBuilder.options
import org.jetbrains.kotlin.contracts.model.structure.UNKNOWN_COMPUTATION.type
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.3.11"
    kotlin("kapt") version "1.3.20"
    application
    id("com.bmuschko.docker-java-application") version "4.3.0"
}

val versionObj = Version(major = 5, minor = 0, revision = 0)

group = "com.grosslicht"
version = "$versionObj"

repositories {
    mavenCentral()
    jcenter()
}

val requeryVersion = "1.5.1"
val log4jVersion = "2.11.1"

dependencies {
    compile(kotlin("stdlib-jdk8"))

    compile("net.dv8tion:JDA:3.8.1_437") {
        exclude(module = "opus-java")
    }

    compile("org.slf4j:slf4j-api:1.7.25")
    compile("org.apache.logging.log4j:log4j-api:$log4jVersion")
    compile("org.apache.logging.log4j:log4j-core:$log4jVersion")
    compile("org.apache.logging.log4j:log4j-slf4j-impl:$log4jVersion")
    compile("io.github.microutils:kotlin-logging:1.6.22")

    compile("io.requery:requery:$requeryVersion")
    compile("io.requery:requery-kotlin:$requeryVersion")
    kapt("io.requery:requery-processor:$requeryVersion")


    compile("com.google.code.gson:gson:2.8.5")
    compile("com.github.kittinunf.fuel:fuel:2.0.0")
    compile("com.github.kittinunf.fuel:fuel-gson:2.0.0")

    compile("de.vandermeer:asciitable:0.3.2")

    compile("org.apache.commons:commons-math3:3.6.1")
    compile("com.google.guava:guava:27.0.1-jre")
    compile("org.mariadb.jdbc:mariadb-java-client:2.3.0")

    compile("ca.pjer:chatter-bot-api:2.0.1")

    compile("com.kennycason:kumo-core:1.17")

    compile("com.lmax:disruptor:3.4.2")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

tasks.named<ProcessResources>("processResources") {
    filesMatching("**/build.properties") {
        expand(project.properties)
    }
}

application {
    mainClassName = "com.grosslicht.patricksbot.PatricksBotKt"
}

val dockerImage = System.getenv("CONTAINER_NAME")

docker {
    javaApplication {
        baseImage.set("openjdk:8-jre")
        maintainer.set("Patrick Grosslicht <patrick@grosslicht.com>")
        ports.set(listOf())
        tag.set("$dockerImage:${project.version}")
    }
}

tasks.named<DockerBuildImage>("dockerBuildImage") {
    tags.add("$dockerImage:latest")
}

data class Version(val major: Int, val minor: Int, val revision: Int) {
    private val buildNumber: String = System.getenv("TRAVIS_BUILD_NUMBER") ?: "dev"

    override fun toString(): String = "$major.$minor.$revision-$buildNumber"
}