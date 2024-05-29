plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp") version "1.9.20-1.0.14"
}

android {
    compileSdk = 34
    defaultConfig {
        applicationId = "com.example.mazdacompanionapp"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.4"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    namespace = "com.example.mazdacompanionapp"


    dependencies {
        implementation(libs.androidx.core.ktx)
        implementation(libs.androidx.lifecycle.runtime.ktx)
        implementation(libs.androidx.activity.compose)
        implementation(platform(libs.androidx.compose.bom))
        implementation(libs.androidx.ui)
        implementation(libs.androidx.ui.graphics)
        implementation(libs.androidx.ui.tooling.preview)
        implementation(libs.androidx.material3)
        implementation(libs.androidbrowserhelper)
        implementation(libs.androidx.appcompat)
        implementation("androidx.compose.ui:ui:1.2.0")
        implementation("androidx.compose.material:material:1.2.0")
        implementation("androidx.compose.ui:ui-tooling-preview:1.2.0")


        implementation(platform("androidx.compose:compose-bom:2023.10.01"))
        implementation("androidx.activity:activity-compose:1.8.1")
        implementation("androidx.compose.material3:material3")
        implementation("androidx.compose.ui:ui")
        implementation("androidx.compose.ui:ui-tooling")
        implementation("androidx.compose.ui:ui-tooling-preview")
        implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
        implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.2")
        implementation("androidx.navigation:navigation-compose:2.7.5")

        androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
        androidTestImplementation("androidx.test.ext:junit:1.1.5")

        implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
        implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.2")

        implementation("androidx.activity:activity-compose:1.4.0")
        implementation("androidx.compose.runtime:runtime-livedata:1.2.0")
        implementation("androidx.navigation:navigation-compose:2.7.5")

        implementation("androidx.room:room-runtime:${rootProject.extra["room_version"]}")
        implementation("androidx.core:core-ktx:1.12.0")
        ksp("androidx.room:room-compiler:${rootProject.extra["room_version"]}")
        implementation("androidx.room:room-ktx:${rootProject.extra["room_version"]}")

        testImplementation(libs.junit)
        androidTestImplementation(libs.androidx.junit)
        androidTestImplementation(libs.androidx.espresso.core)
        androidTestImplementation(platform(libs.androidx.compose.bom))
        androidTestImplementation(libs.androidx.ui.test.junit4)
        debugImplementation(libs.androidx.ui.tooling)
        debugImplementation(libs.androidx.ui.test.manifest)
    }
}

