plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.google.services)
    id("com.google.devtools.ksp")
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
    id("com.google.firebase.crashlytics")
    alias(libs.plugins.protobuf)
    id("kotlinx-serialization")
    alias(libs.plugins.kotlin.parcelize)
}

android {
    namespace = "com.kxtdev.bukkydatasup"
    compileSdk = 36

    val baseUrl = "https://bukkydatasup.com"
    val whatsAppBaseUrl = "https://wa.me"

    defaultConfig {
        applicationId = "com.kxtdev.bukkydatasup"
        minSdk = 24
        targetSdk = 36
        versionCode = 1

        val versionMajor = "1"
        val versionMinor = "0"
        val versionPatch = "0"

        versionName = "$versionMajor.$versionMinor.$versionPatch"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        ndk {
            debugSymbolLevel = "SYMBOL_TABLE"
        }

        ksp {
            arg("room.schemaLocation", "$projectDir/schemas")
        }

    }

    buildTypes {
        val debug by getting {
            applicationIdSuffix = ".debug"
            buildConfigField("String", "BASE_URL", "\"$baseUrl\"")
            buildConfigField("String", "WHATSAPP_BASE_URL", "\"$whatsAppBaseUrl\"")
        }
        val release by getting {
            isMinifyEnabled = true
            isShrinkResources = true

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            buildConfigField("String", "BASE_URL", "\"$baseUrl\"")
            buildConfigField("String", "WHATSAPP_BASE_URL", "\"$whatsAppBaseUrl\"")

            ndk {
                debugSymbolLevel = "FULL"
            }
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
        isCoreLibraryDesugaringEnabled = true
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.15"
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
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.play.services.analytics.impl)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    coreLibraryDesugaring(libs.android.desugarJdkLibs)

    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.hilt.android.testing)
    implementation(libs.androidx.hilt.work)
    implementation(libs.androidx.work.runtime)
    implementation(libs.androidx.work.rxjava)

    kapt(libs.androidx.hilt.compiler)
    kapt(libs.hilt.compiler)
    kaptAndroidTest(libs.hilt.compiler)
    implementation(libs.hilt.android)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.messaging.ktx)

    implementation(libs.coil.kt)

    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.navigation.runtime)
    implementation(libs.androidx.compose.material3.windowSizeClass)
    implementation(libs.androidx.compose.runtime.tracing)
    implementation(libs.androidx.appcompat)
    implementation(libs.accompanist.systemuicontroller)
    implementation(libs.androidx.splashscreen)

    implementation(libs.paystack)
    implementation(libs.paystack.widget.pinpad)

    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.datetime)
    implementation(libs.androidx.biometric)

    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.foundation.layout)
    implementation(libs.androidx.compose.runtime)
    implementation(libs.androidx.compose.ui.util)
    implementation(libs.androidx.compose.material.icons.ext)

    implementation(libs.androidx.compose.ui.tooling)

    implementation(libs.coil.kt.compose)
    implementation(libs.androidx.tracing)

    implementation(libs.retrofit.core)

    implementation(libs.androidx.test.junit)
    implementation(libs.androidx.test.rules)
    implementation(libs.androidx.test.runner)

    implementation(libs.junit)
    implementation(libs.kotlinx.coroutines.test)
    implementation(libs.turbine)

    implementation(libs.accompanist.permissions)

    implementation(libs.androidx.paging.common)
    implementation(libs.androidx.paging.runtime)
    implementation(libs.androidx.paging.compose)

    implementation(libs.kotlinx.serialization.json)
    implementation(libs.okhttp.logging)
    implementation(libs.google.gson)
    implementation(libs.retrofit.kotlin.serialization)
    implementation(libs.androidx.security.crypto)
    implementation(libs.androidx.security.crypto.ktx)

    implementation(libs.accompanist.webview)

    implementation(libs.lottie.compose)

    implementation(libs.androidx.dataStore.core)
    implementation(libs.androidx.dataStore.preferences)
    implementation(libs.androidx.datastore.core.okio.jvm)

    implementation(libs.androidx.room.runtime)
    testImplementation(libs.androidx.room.testing)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.paging)

    implementation(libs.androidx.hilt.work)
    implementation(libs.androidx.work.runtime)
    implementation(libs.androidx.work.rxjava)

}


kapt {
    correctErrorTypes = true
}

hilt {
    enableAggregatingTask = false
}

