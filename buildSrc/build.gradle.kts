
plugins {
    `kotlin-dsl`
//    id("org.jetbrains.kotlin.multiplatform") version "2.1.0"
//    alias(libs.plugins.kotlinMultiplatform)
}

repositories {
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    compileOnly("org.jetbrains.kotlin:kotlin-gradle-plugin:2.1.10")
}


gradlePlugin {
    plugins {
        create("helloWorldPlugins") {
            id = "hello-world-class"
            implementationClass = "HelloWorldPlugin"
        }
        create("buildAcmeResourcesPlugins") {
            id = "build-acme-resources"
            implementationClass = "BuildAcmeResourcesPlugin"
        }
    }
}