import org.gradle.api.Project
import org.gradle.api.Plugin

class HelloWorldPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        println("\n==========================>>>>> Hello World Plugin here!!!  ${this.javaClass.simpleName} applied on ${project.name}")

        project.tasks.register<HelloWorldTask>("helloWorldTask", HelloWorldTask::class.java) {
            group = "build"
            description = "Generate Kotlin file with string resources from values-XX/strings.xml"
        }


//        project.tasks.register<HelloWorldTask>("helloWorldTskk") {
//            group = "build"
//            description = "Generate Kotlin file with string resources from values-XX/strings.xml"
//            resourcesDir.set(project.file("src/commonMain/resources"))
//            outputDir.set(project.file("${project.buildDir}/generated/source/kmp/main/kotlin"))
//        }

        project.tasks.named("generateResourceAccessorsForCommonMain") {
            dependsOn("helloWorldTask")
        }
    }
}


abstract class HelloWorldTask : org.gradle.api.DefaultTask() {
    @org.gradle.api.tasks.TaskAction
    fun generate() {
        println("==================================> Hello World Task here! it works ")
    }
}

//apply<HelloWorldPlugin>()
