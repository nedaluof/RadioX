plugins {
  alias(libs.plugins.com.android.application)
  alias(libs.plugins.org.jetbrains.kotlin.android)
  alias(libs.plugins.ksp)
  alias(libs.plugins.org.jetbrains.kotlin.kapt)
  alias(libs.plugins.kotlin.parcelize)
}

android {
  namespace = "com.nedaluof.radiox"
  compileSdk = libs.versions.compile.sdk.get().toInt()

  defaultConfig {
    applicationId = "com.nedaluof.radiox"
    minSdk = libs.versions.min.sdk.get().toInt()
    targetSdk = libs.versions.compile.sdk.get().toInt()
    versionCode = libs.versions.version.code.get().toInt()
    versionName = libs.versions.version.name.get()
    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    vectorDrawables {
      useSupportLibrary = true
    }
  }

  buildTypes {
    release {
      isMinifyEnabled = false
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
    }
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
  }
  kotlinOptions {
    jvmTarget = "17"
  }
  buildFeatures {
    compose = true
  }
  composeOptions {
    kotlinCompilerExtensionVersion = "1.5.14"
  }
  packaging {
    resources {
      excludes += "/META-INF/{AL2.0,LGPL2.1}"
    }
  }
}

dependencies {
  /** app **/
  //activity ktx
  implementation(libs.activity.ktx)
  //compose activity
  implementation(libs.compose.activity)
  //app compat
  implementation(libs.androidx.appcompat)
  //core extensions
  implementation(libs.core.ktx)
  //MDC
  implementation(libs.xml.material)
  //compose ui
  val composeBom = platform(libs.compose.bom)
  implementation(composeBom)
  androidTestImplementation(composeBom)
  implementation(libs.bundles.compose)
  debugImplementation(libs.compose.ui.tooling.preview)
  debugImplementation(libs.compose.ui.test.manifest)
  //coil image loader
  implementation(libs.coil.compose)
  //compose nav component
  implementation(libs.compose.navigation)
  //paging
  implementation(libs.paging.compose)
  //media3
  implementation(libs.bundles.media3)
  /**jet-pack Components**/
  //lifecycle
  implementation(libs.bundles.compose.lifecycle)
  //koin DI
  implementation(platform(libs.koin.bom))
  implementation(libs.bundles.koin)
  //room database
  implementation(libs.bundles.room)
  ksp(libs.room.compiler)
  //networking
  implementation(libs.retrofit) {
    exclude(module = "okhttp")
  }
  implementation(libs.retrofit.moshi.converter)
  implementation(libs.logging.interceptor)
  implementation(libs.moshi)
  ksp(libs.moshi.kotlin.codegen)
  //coroutines
  implementation(libs.coroutines)
  //debugging
  implementation(libs.timber)
  //test libs
  testImplementation(libs.junit)
  testImplementation(libs.bundles.mockito)
  //androidTestImplementation(libs.bundles.test)
  androidTestImplementation(libs.compose.ui.test.junit4)
}