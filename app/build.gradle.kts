plugins {
    alias(libs.plugins.androidApplication)
    kotlin("kapt")
    id("com.google.dagger.hilt.android")
    id ("kotlin-parcelize")
    alias(libs.plugins.jetbrainsKotlinAndroid)
}

android {
    namespace = "com.example.devrev"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.devrev"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }
    buildFeatures{
        buildConfig = true
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )

        }
        debug {
            buildConfigField("String","API_KEY","\"909594533c98883408adef5d56143539\"")
            buildConfigField("String","BASE_URL","\"http://api.themoviedb.org\"")
        }

    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation (libs.gson)
    implementation(libs.hilt.android)
    implementation (libs.androidx.lifecycle.viewmodel.ktx)
    implementation (libs.kotlin.stdlib)

    implementation (libs.ui)
    implementation (libs.androidx.material)
    implementation (libs.ui.tooling)
    implementation (libs.androidx.lifecycle.runtime.ktx.v231)
    implementation (libs.androidx.activity.compose.v131)
    implementation (libs.androidx.navigation.compose)

    //glide
    implementation (libs.glide)
    annotationProcessor (libs.compiler)
    implementation (libs.glide.transformations)

    kapt(libs.hilt.android.compiler)
    implementation(project(":devrevsdk"))
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    testImplementation (libs.mockito.core)
    testImplementation (libs.kotlinx.coroutines.test)
    testImplementation (libs.androidx.core.testing)
}