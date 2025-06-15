import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.serialization)
}

kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }
    
    jvm("desktop")
    
    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        moduleName = "composeApp"
        browser {
            val rootDirPath = project.rootDir.path
            val projectDirPath = project.projectDir.path
            commonWebpackConfig {
                outputFileName = "composeApp.js"
                devServer = (devServer ?: KotlinWebpackConfig.DevServer()).apply {
                    static = (static ?: mutableListOf()).apply {
                        // Serve sources to debug inside browser
                        add(rootDirPath)
                        add(projectDirPath)
                    }
                }
            }
        }
        binaries.executable()
    }
    
    sourceSets {
        val desktopMain by getting
        
        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.ktor.client.okhttp)
//            implementation(libs.windedge.table)

//            implementation("androidx.compose.material:material-icons-extended:1.7.8")
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtime.compose)
            implementation(compose.materialIconsExtended)  // icons for desktop/others
            implementation(libs.ktor.client.auth)
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.serialization.kotlinx.json)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.multiplatform.settings)
            implementation("io.github.sunny-chung:composable-table:1.3.1")
//            implementation("io.github.windedge.table:table-m3:0.2.0")
//            implementation("com.ryinex.kotlin:compose-data-table:1.0.7")
//            implementation("io.github.windedge.table:table-m2:0.2.1")
//            implementation(libs.windedge.table)
//                        implementation("io.ktor:ktor-client-content-negotiation:3.1.3")
            //            implementation(libs.okhttp3)

        }
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutines.swing)
            implementation(compose.materialIconsExtended)  // icons for desktop/others
            implementation(libs.ktor.client.okhttp)    // for JVM
            implementation(libs.ktor.client.cio)    // for JVM
//            implementation("com.ryinex.kotlin:compose-data-table:1.0.5")
//            implementation(libs.windedge.table)
            //            implementation(libs.okhttp3)
            //            implementation(libs.ktor.client.js)    // for WasmJs

        }
    }
}

android {
    namespace = "com.krzysobo.ktsob"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.krzysobo.ktsob"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(libs.androidx.ui.android)
    implementation(libs.androidx.ui.text.android)
    implementation(libs.androidx.foundation.layout.android)
    implementation(libs.foundation.layout.android)
    implementation(libs.play.services.auth)
    implementation(libs.ktor.client.cio)
    implementation(libs.ui.android)    // for Android

//    implementation(libs.androidx.material3.android)
//    implementation(libs.androidx.navigation.compose)
//    implementation("androidx.navigation:navigation-compose:2.8.9")
//    implementation(libs.okhttp3)
//    implementation(libs.androidx.navigation.runtime.desktop)
//    implementation(libs.androidx.navigation.runtime.ktx)
//    implementation(libs.androidx.navigation.compose)
//    implementation(libs.androidx.navigation.runtime.desktop)
//    implementation(libs.androidx.navigation.runtime.ktx)
    debugImplementation(compose.uiTooling)
}

compose.desktop {
    application {
        mainClass = "com.krzysobo.ktsob.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "com.krzysobo.ktsob"
            packageVersion = "1.0.1"
        }
    }
}
