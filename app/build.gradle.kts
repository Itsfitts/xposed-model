plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.compose.compiler)

}

android {
    namespace = "com.niki.xposed"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.niki.xposed.model"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "0.1"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildFeatures {
//        dataBinding = true
        compose = true
        buildConfig = true
    }
    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }

        debug {
            isMinifyEnabled = false
//            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules-debug.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    sourceSets {
        getByName("main") {
            assets {
                srcDirs("src/main/assets")
            }
        }
    }
}

dependencies {
    compileOnly(libs.xposed.api)

    implementation(project(":hooker"))
//    implementation(project(":chat"))
//    implementation(project(":tool-call"))
    implementation(project(":common"))

    implementation(libs.annotation)

    implementation("androidx.activity:activity-compose:1.4.0")

    implementation("androidx.compose.material3:material3:1.3.2")
    implementation("androidx.compose.material3:material3-window-size-class:1.3.2")
    implementation("androidx.compose.material3:material3-adaptive-navigation-suite:1.4.0-alpha16")
    implementation("androidx.compose.material:material-icons-core:1.7.8")
    implementation("androidx.compose.material:material-icons-extended:1.7.8")

    implementation(libs.ui.tooling.preview.android)

    implementation(libs.kotlinx.serialization.json)

    implementation(libs.zephyr.tools)
    implementation(libs.zephyr.provider)
    implementation(libs.zephyr.log)

    implementation(libs.material)
//    implementation("com.squareup.leakcanary:leakcanary-android:2.7")
    implementation(kotlin("reflect"))
}