plugins {
    id("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()

    maven("https://jitpack.io")
}

dependencies {
    // PowerNukkitX
    compileOnly("cn.powernukkitx:powernukkitx:1.19.70-r2")

    //Lombok
    compileOnly("org.projectlombok:lombok:1.18.26")
    annotationProcessor("org.projectlombok:lombok:1.18.26")

    //Depend
    implementation(files("C:/Users/denis/Desktop/Test Server/plugins/DataManager-1.0-SNAPSHOT.jar"))
    implementation(files("C:/Users/denis/Desktop/Test Server/plugins/Database-1.0-SNAPSHOT.jar"))
    implementation(files("C:/Users/denis/Desktop/Test Server/plugins/FormAPI-1.0-SNAPSHOT.jar"))
    implementation(files("C:/Users/denis/Desktop/Test Server/plugins/Pattern.jar"))
    implementation(files("C:/Users/denis/Desktop/Test Server/plugins/dCore.jar"))
}