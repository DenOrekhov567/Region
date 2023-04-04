plugins {
    id("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()

    maven("https://repo.opencollab.dev/maven-snapshots/")
    maven("https://repo.opencollab.dev/maven-releases/")
}

dependencies {
    //Nukkit
    compileOnly("cn.nukkit:nukkit:1.0-SNAPSHOT")

    //Lombok
    compileOnly("org.projectlombok:lombok:1.18.26")
    annotationProcessor("org.projectlombok:lombok:1.18.26")

    //Plugins
    implementation(files("./lib/DataManager.jar"))
    implementation(files("./lib/Database.jar"))
    implementation(files("./lib/FormAPI.jar"))
    implementation(files("./lib/Account.jar"))
    implementation(files("./lib/Rank.jar"))
}