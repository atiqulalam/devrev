import io.netty.util.ReferenceCountUtil.release

plugins {
    id("com.android.library")
    id("maven-publish")
    alias(libs.plugins.jetbrainsKotlinAndroid)
}

android {
    namespace = "com.devrevsdk"
    compileSdk = 34

    defaultConfig {
        //applicationId = "com.devrevsdk"
        minSdk = 24
        targetSdk = 34
        //versionCode = 1
        //versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
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
}

afterEvaluate {
    publishing {
        publications {
            register<MavenPublication>("release") {
                groupId = "com.github.atiqulalam"
                artifactId = "devrev"
                version = "1.0.0"

                afterEvaluate {
                    from(components["release"])
                }
            }
        }
    }

}


dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation (libs.gson)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}