plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

group = "com.obsidian_north"
version = "3.0.0"

android {
    namespace = "com.obsidian_north.mediastore"
    compileSdk = 36

    defaultConfig {
        minSdk = 24
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

kotlin {
    compilerOptions {
        jvmTarget.set(
            org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17
        )
    }
}

repositories {
    mavenCentral()
    google()
}

dependencies {
    implementation("com.facebook.react:react-android")
    implementation("androidx.exifinterface:exifinterface:1.3.7")
}
