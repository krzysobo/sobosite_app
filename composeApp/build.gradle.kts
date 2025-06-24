import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
//import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
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


    sourceSets {
        val desktopMain by getting

        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)

            implementation(libs.ktor.client.okhttp)
            implementation(project(":soboAppTpl", ""))
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
            implementation(libs.multiplatform.settings)

            implementation(libs.ktor.client.auth)
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.serialization.kotlinx.json)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.client.content.negotiation)
            implementation(project(":soboAppTpl", ""))


        }
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutines.swing)
            implementation(compose.materialIconsExtended)  // icons for desktop/others

            implementation(libs.ktor.client.okhttp)    // for JVM
            implementation(libs.ktor.client.cio)    // for JVM
            implementation(project(":soboAppTpl", ""))
        }
    }
}

android {
    namespace = "com.krzysobo.sobositeapp"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.krzysobo.sobositeapp"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            /*
                --> fix for the following BouncyCastle's error:
                2 files found with path 'META-INF/versions/9/OSGI-INF/MANIFEST.MF' from inputs:
                 - org.bouncycastle:bcprov-jdk18on:1.81/bcprov-jdk18on-1.81.jar
                 - org.jspecify:jspecify:1.0.0/jspecify-1.0.0.jar
                Adding a packaging block may help, please refer to
             */
            excludes += "META-INF/versions/9/OSGI-INF/MANIFEST.MF"
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

    debugImplementation(compose.uiTooling)
}

compose.desktop {
    application {
        mainClass = "com.krzysobo.sobositeapp.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "com.krzysobo.sobositeapp"
            packageVersion = "1.1.1"
        }
    }
}
