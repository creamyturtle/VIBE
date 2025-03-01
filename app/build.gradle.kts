/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.serialization") version "2.1.0"
    id("org.jetbrains.kotlin.plugin.compose")
}

android {
    namespace = "com.example.vibe"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.vibe"
        minSdk = 24
        targetSdk = 35
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.3" // Use the latest stable version
    }


}



dependencies {

    implementation(platform("androidx.compose:compose-bom:2025.02.00"))
    implementation("androidx.activity:activity-compose:1.10.1")
    implementation("androidx.activity:activity-ktx:1.10.1")
    implementation("com.google.accompanist:accompanist-pager:0.30.1")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.core:core-ktx:1.15.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:${rootProject.extra["lifecycle_version"]}")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:${rootProject.extra["lifecycle_version"]}")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:${rootProject.extra["lifecycle_version"]}")
    implementation("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:1.0.0")
    implementation("com.squareup.retrofit2:retrofit:${rootProject.extra["retrofit2_version"]}")
    implementation("io.coil-kt:coil-compose:2.7.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.8.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.10.0")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.ui:ui:1.7.8")
    implementation("androidx.navigation:navigation-compose:2.8.8") // ✅ Latest stable version
    implementation("androidx.compose.material:material-icons-extended:1.7.8")
    implementation("com.google.android.gms:play-services-maps:19.1.0")
    implementation("com.google.maps.android:maps-compose:2.11.2")
    implementation("androidx.compose.foundation:foundation:1.7.8")
    implementation("androidx.compose.foundation:foundation-layout:1.7.8")
    implementation("androidx.compose.runtime:runtime:1.7.8")

    implementation("androidx.datastore:datastore-preferences:1.1.3")

    implementation("com.kizitonwose.calendar:view:2.0.4")
    implementation("com.google.accompanist:accompanist-pager-indicators:0.30.1")

    implementation("com.google.zxing:core:3.4.1")



    implementation("com.google.mlkit:barcode-scanning:17.3.0")

    // CameraX (Required for real-time scanning)
    implementation("androidx.camera:camera-core:1.4.1")
    implementation("androidx.camera:camera-camera2:1.4.1")
    implementation("androidx.camera:camera-lifecycle:1.4.1")
    implementation("androidx.camera:camera-view:1.4.1")

    implementation("com.google.accompanist:accompanist-permissions:0.31.0-alpha")

    implementation("androidx.compose.material:material:1.7.8")
    implementation("androidx.compose.ui:ui-tooling:1.7.8")
    implementation("androidx.lifecycle:lifecycle-runtime-compose")
    implementation("com.google.firebase:firebase-perf-ktx:21.0.4")
    implementation("androidx.compose.runtime:runtime-livedata:1.7.8")
    implementation("com.google.android.libraries.places:places:4.1.0")

    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.github.dhaval2404:imagepicker:2.1")

    debugImplementation("androidx.compose.ui:ui-test-manifest")
    debugImplementation("androidx.compose.ui:ui-tooling")
}
