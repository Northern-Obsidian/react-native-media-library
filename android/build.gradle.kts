@file:Suppress("UnstableApiUsage")

plugins {
  id("com.android.library")
  id("org.jetbrains.kotlin.android")
  id("expo-module-gradle-plugin")
}

android {
  namespace = "expo.modules.mediastore"
  compileSdk = 34

  defaultConfig {
    minSdk = 21
    targetSdk = 34
  }

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
  }

  kotlinOptions {
    jvmTarget = "17"
  }
}

dependencies {
  implementation(project(":expo-modules-core"))
}
