plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-android")
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
}

android {
    namespace = "com.croumy.hltb_wearos"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.croumy.hltb_wearos"
        minSdk = 30
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"
        vectorDrawables {
            useSupportLibrary = true
        }

    }

    buildTypes {
        release {
            isShrinkResources = true
            isMinifyEnabled = true
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
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}


dependencies {
    val composeVersion = "1.5.0-alpha04"
    val wearComposeVersion = "1.1.2"
    val hiltVersion = "2.46"
    val hiltCoreVersion = "1.0.0"
    val workVersion = "2.8.0"
    val klockVersion = "2.2.0"

    // CORE
    implementation("androidx.lifecycle:lifecycle-service:2.6.1")
    implementation("com.google.android.gms:play-services-wearable:18.0.0")
    implementation("androidx.percentlayout:percentlayout:1.0.0")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("androidx.recyclerview:recyclerview:1.3.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")
    implementation("androidx.compose.ui:ui-tooling-preview:$composeVersion")
    implementation("androidx.core:core-ktx:1.10.1")

    // WEAR OS
    implementation("androidx.wear:wear:1.2.0")
    implementation("androidx.wear:wear-ongoing:1.0.0")

    // COMPOSE
    implementation("androidx.activity:activity-compose:1.7.1")
    implementation("androidx.compose.ui:ui:$composeVersion")
    implementation("androidx.compose.material:material:$composeVersion")
    implementation("androidx.compose.material:material-icons-extended:${composeVersion}")
    implementation("androidx.wear.compose:compose-material:$wearComposeVersion")
    implementation("androidx.wear.compose:compose-foundation:$wearComposeVersion")
    implementation("androidx.wear.compose:compose-navigation:$wearComposeVersion")
    implementation("androidx.navigation:navigation-compose:2.6.0")
    implementation("androidx.compose.animation:animation:${composeVersion}")


    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.2")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.4.2")

    //HILT
    kapt("com.google.dagger:hilt-android-compiler:$hiltVersion")
    kapt("com.google.dagger:hilt-compiler:$hiltVersion")
    kapt("androidx.hilt:hilt-compiler:$hiltCoreVersion")
    implementation("com.google.dagger:hilt-android:$hiltVersion")
    implementation("androidx.hilt:hilt-common:$hiltCoreVersion")
    implementation("androidx.hilt:hilt-work:$hiltCoreVersion")
    implementation("androidx.work:work-runtime-ktx:$workVersion")
    implementation("androidx.hilt:hilt-navigation-fragment:$hiltCoreVersion")
    implementation("androidx.hilt:hilt-navigation-compose:$hiltCoreVersion")

    // Data
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:okhttp:4.10.0")

    //Picture
    implementation("io.coil-kt:coil-compose:2.3.0")

    // Shimmer
    implementation("com.valentinilk.shimmer:compose-shimmer:1.0.4")

    //Date
    implementation("com.soywiz.korlibs.klock:klock:$klockVersion")

    // Tests
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:$composeVersion")
    debugImplementation("androidx.compose.ui:ui-tooling:$composeVersion")
    debugImplementation("androidx.compose.ui:ui-test-manifest:$composeVersion")
}

kapt {
    correctErrorTypes = true
}
