plugins {
  alias(libs.plugins.android.application)
  alias(libs.plugins.kotlin.android)
  id("org.jetbrains.kotlin.plugin.serialization")
}

android {
  namespace = "com.keyfall.mypokedex"
  compileSdk = 35

  defaultConfig {
    applicationId = "com.keyfall.mypokedex"
    minSdk = 28
    targetSdk = 35
    versionCode = 1
    versionName = "1.0"

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
  }

  buildTypes {
    release {
      isMinifyEnabled = false
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
    }
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
  }
  kotlinOptions {
    jvmTarget = "11"
  }

  buildFeatures {
    compose = true
  }
  composeOptions {
    kotlinCompilerExtensionVersion = "1.5.14" // Example version
  }
}

dependencies {
  //FOR API CALLS AND (DE)SERIALIZATION
  implementation(libs.retrofit)
  implementation(libs.retrofit2.kotlinx.serialization.converter)
  implementation(libs.kotlinx.coroutines.android)
  implementation(libs.kotlinx.serialization.json)

  //FOR UI ELEMENTS
  implementation(libs.androidx.ui)
  implementation(libs.androidx.material)
  implementation(libs.ui.tooling.preview)
  debugImplementation(libs.androidx.ui.tooling)
  debugImplementation(libs.ui.test.manifest)
  implementation(libs.activity.compose)
  implementation(libs.androidx.graphics.shapes)

  //DEFAULT PROJECT
  implementation(libs.androidx.core.ktx)
  implementation(libs.androidx.appcompat)
  implementation(libs.material)
  implementation(libs.androidx.activity)
  implementation(libs.androidx.constraintlayout)
  implementation(libs.androidx.runtime.android)
  implementation(libs.androidx.material3.android)
  testImplementation(libs.junit)
  androidTestImplementation(libs.androidx.junit)
  androidTestImplementation(libs.androidx.espresso.core)
}