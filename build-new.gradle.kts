plugins {
//    application
    kotlin("jvm") version "1.2.71"
}

repositories {
    mavenCentral()
}

dependencies {
    testCompile("org.junit.jupiter:junit-jupiter-api:5.3.1")
    testCompile("org.junit.jupiter:junit-jupiter-params:5.3.1")
    testRuntime("org.junit.jupiter:junit-jupiter-engine:5.3.1")
}

tasks.withType<Test> {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
}

//application {
//    mainClassName = "deployer.Deployer"
//}

//dependencies {
////    compile(kotlin("stdlib"))
//    compile("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
//    compile("org.jetbrains.kotlin:kotlin-reflect")
//    compile("org.apache.commons:commons-lang3:3.4")
//    compile("org.slf4j:slf4j-api:1.7.7")
//    compile("ch.qos.logback:logback-classic:1.0.13")
//    compile("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.7.1")
//    compile("com.fasterxml.jackson.core:jackson-databind:2.7.1-1")
//    compile("com.fasterxml.jackson.module:jackson-module-kotlin:2.7.1-2")
//
//    testCompile("junit:junit:4.8.1")
//    testCompile("org.mockito:mockito-all:1.9.0")
//    testCompile("org.jetbrains.kotlin:kotlin-test")
//    testCompile("org.jetbrains.kotlin:kotlin-test-junit")
//}

//repositories {
//    jcenter()
//}


