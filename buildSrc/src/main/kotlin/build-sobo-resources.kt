import org.gradle.api.Project
import org.gradle.api.Plugin
import org.w3c.dom.Element
import java.io.File
import javax.xml.parsers.DocumentBuilderFactory
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.OutputDirectory
import org.gradle.internal.impldep.com.google.api.client.util.Value
import org.gradle.kotlin.dsl.create

//import org.gradle.api.tasks.InputDirectory
//import org.gradle.api.tasks.OutputDirectory
//import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

// Extension to allow configuration in build.gradle.kts
interface SoboStringResourcesExtension {
    val resourcesDir: Property<String>
    val outputDir: Property<String>
    val resourcesOutputFile: Property<String>
    val resourcesOutputPackage: Property<String>
}


class BuildAcmeResourcesPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        println("\n====>  Build Acme ALL Resources Plugin Started - it is here!!!  ${this.javaClass.simpleName} applied on ${project.name}")
        val extension =
            project.extensions.create<SoboStringResourcesExtension>(
                "soboStringResourcesConfig"
            )
                .apply {
                    // Set default values
                    resourcesDir.convention("src/commonMain/composeResources")
                    outputDir.convention("${project.buildDir}/generated/source/kmp/main/kotlin/com/krzysobo/cryptocenter/generated")
                    resourcesOutputPackage.convention("com.krzysobo.cryptocenter.generated")
                    resourcesOutputFile.convention("StringResources.kt")
                }

        project.tasks.register<BuildAcmeStringResourcesTask>(
            "buildAcmeStringResourcesTask",
            BuildAcmeStringResourcesTask::class.java
        ) {
            group = "build"
            description =
                "Generate Kotlin file with string resources from values-XX/strings.xml. Create to enable easier language switching."
            resourcesDir.set(project.file(extension.resourcesDir))
            outputDir.set(project.file(extension.outputDir))
            resourcesOutputFile = extension.resourcesOutputFile.get()
            resourcesOutputPackage = extension.resourcesOutputPackage.get()

//            resourcesDir.set(project.file("src/commonMain/composeResources"))
//            outputDir.set(project.file("${project.buildDir}/generated/source/kmp/main/kotlin/com/krzysobo/cryptocenter/generated"))
        }

        project.tasks.named("generateResourceAccessorsForCommonMain") {
            dependsOn("buildAcmeStringResourcesTask")
        }


//        project.extensions.configure<KotlinMultiplatformExtension> {
//            sourceSets.named("commonMain") {
//                kotlin.srcDir("${project.buildDir}/generated/source/kmp/main/kotlin")
//            }
//        }
    }
}


abstract class BuildAcmeStringResourcesTask : org.gradle.api.DefaultTask() {
    @get:InputDirectory
    abstract val resourcesDir: org.gradle.api.file.DirectoryProperty

    @get:OutputDirectory
    abstract val outputDir: org.gradle.api.file.DirectoryProperty

    @get:Input
    abstract var resourcesOutputFile: String

    @get:Input
    abstract var resourcesOutputPackage: String



    fun generateResourcesObjectFileHashmap(resourceMap: Map<String, Map<String, String>>): String {
        return """
    package $resourcesOutputPackage

    object StringResources {
        val resources: Map<String, Map<String, String>> = mapOf(
    ${
            resourceMap.entries.joinToString(",\n") { (lang, strings) ->
                """"$lang" to mapOf(${
                    strings.entries.joinToString(", ") { (key, value) ->
                        """"$key" to "${value.replace("\"", "\\\"")}" """
                    }
                })"""
            }
        }
        )
    }
    """.trimIndent()
    }

    fun generateResourcesObjectFileObject(resourceMap: Map<String, Map<String, String>>): String {
        val tmpMapListByHandle: MutableMap<String, MutableList<String>> = mutableMapOf()

        for ((lang, langDefs) in resourceMap) {
            for ((handle, langDef) in langDefs) {
                if (!tmpMapListByHandle.containsKey(handle)) {
                    tmpMapListByHandle[handle] = mutableListOf()
                }
                tmpMapListByHandle[handle]!!.add("\"$lang\" to \"\"\"${langDef.trim()}  \"\"\"  ")
            }
        }

        var defsOut: MutableList<String> = mutableListOf()

        for ((handle, langDefs) in tmpMapListByHandle) {
            defsOut.add(
                "            val $handle = SoboStringResource(hashMapOf(" +
                        "\n                ${
                            langDefs.joinToString(
                                ",\n                "
                            )
                        }))\n"
            )
        }

        return """
    package $resourcesOutputPackage

    import com.krzysobo.soboapptpl.service.SoboStringResource

    object SoboRes {
        const val s = "\${'$'}s"   // avoiding interpolation problem
        const val d = "\${'$'}d"   // avoiding interpolation problem

        object string { ${"\n" + defsOut.joinToString("\n")}
    }}
    """.trimIndent()
    }


    // Parse strings.xml into a Map<String, String>
    fun parseStringsResourceXml(file: File): Map<String, String> {
        val doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file)
        doc.documentElement.normalize()
        val stringNodes = doc.getElementsByTagName("string")
        val result = mutableMapOf<String, String>()
        for (i in 0 until stringNodes.length) {
            val node = stringNodes.item(i) as Element
            val name = node.getAttribute("name")
            val value = node.textContent
            result[name] = value
        }
        return result
    }

    @org.gradle.api.tasks.TaskAction
    fun generate() {
        println("\n======> BuildAcmeStringResourcesTask is here. Working...")
        println("==================================> Generating StringResources.kt")
        val resourceMap = mutableMapOf<String, Map<String, String>>()

        val defaultFile = resourcesDir.file("values/strings.xml").get().asFile
        if (defaultFile.exists()) {
            resourceMap["en"] = parseStringsResourceXml(defaultFile)
        }

        resourcesDir.get().asFile.walk().filter {
            it.isFile && it.path.contains("values-") && it.name == "strings.xml"
        }.forEach { file ->
            val langCode = file.parentFile.name.removePrefix("values-")
            resourceMap[langCode] = parseStringsResourceXml(file)
        }

        val outputFile = outputDir.file(resourcesOutputFile).get().asFile
        outputFile.parentFile.mkdirs()
        outputFile.writeText(generateResourcesObjectFileObject(resourceMap))
    }
}

