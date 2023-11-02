plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-android")
    id("kotlin-kapt")
    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
}

android {
    val appVersionCode = System.getenv("BUILD_NUMBER")?.toInt() ?: 1
    val appVersionName = System.getenv("TAG_VERSION") ?: "1.0.0"

    namespace = "com.croumy.hltbwearos"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.croumy.hltbwearos"
        minSdk = 30
        targetSdk = 34
        versionCode = appVersionCode
        versionName = appVersionName
        vectorDrawables {
            useSupportLibrary = true
        }

        testInstrumentationRunner = "com.croumy.hltb_wearos.helpers.CustomTestRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
        debug {
            applicationIdSuffix = ".debug"
        }
        applicationVariants.all {
            outputs.forEach { output ->
                if (output is com.android.build.gradle.internal.api.BaseVariantOutputImpl) {
                    output.outputFileName =
                        "watch-HLTBwearOS-v${this.versionName}.apk"
                }
            }
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
    val composeVersion = "1.5.4"
    val wearComposeVersion = "1.3.0-alpha08"
    val hiltVersion = "2.46"
    val hiltCoreVersion = "1.0.0"
    val workVersion = "2.8.1"
    val klockVersion = "3.4.0"
    val roomVersion = "2.5.2"
    val kotlinxVersion = "1.7.3"
    val horlogistVersion = "0.5.0"
    val accompanistVersion = "0.33.2-alpha"

    // CORE
    implementation("androidx.lifecycle:lifecycle-service:2.6.2")
    implementation("androidx.percentlayout:percentlayout:1.0.0")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("androidx.recyclerview:recyclerview:1.3.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.core:core-splashscreen:1.0.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$kotlinxVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:$kotlinxVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinxVersion")

    // WEAR OS
    implementation("androidx.wear:wear:1.3.0")
    implementation("androidx.wear:wear-ongoing:1.0.0")
    implementation("androidx.wear:wear-remote-interactions:1.0.0")
    implementation("androidx.wear:wear-remote-interactions:1.0.0")
    implementation("com.google.android.gms:play-services-wearable:18.1.0")
    implementation("androidx.wear:wear-input:1.2.0-alpha02")

    // HORLOGIST / ACCOMPANIST
    implementation("com.google.android.horologist:horologist-compose-layout:$horlogistVersion")
    implementation("com.google.accompanist:accompanist-permissions:$accompanistVersion")

    // COMPOSE
    debugImplementation("androidx.compose.ui:ui-tooling:$composeVersion")
    implementation("androidx.compose.ui:ui-tooling-preview:$composeVersion")
    implementation(platform("androidx.compose:compose-bom:2023.08.00"))
    implementation("androidx.activity:activity-compose:1.8.0")
    implementation("androidx.compose.ui:ui:$composeVersion")
    implementation("androidx.compose.material:material:$composeVersion")
    implementation("androidx.compose.material:material-icons-extended:${composeVersion}")
    implementation("androidx.wear.compose:compose-material:$wearComposeVersion")
    implementation("androidx.wear.compose:compose-foundation:$wearComposeVersion")
    implementation("androidx.wear.compose:compose-navigation:$wearComposeVersion")
    implementation("androidx.compose.runtime:runtime:$composeVersion")
    implementation("androidx.compose.animation:animation:${composeVersion}")

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

    // ROOM
    implementation("androidx.room:room-runtime:$roomVersion")
    annotationProcessor("androidx.room:room-compiler:$roomVersion")
    ksp("androidx.room:room-compiler:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")

    //Picture
    implementation("io.coil-kt:coil-compose:2.3.0")

    // Shimmer
    implementation("com.valentinilk.shimmer:compose-shimmer:1.0.4")

    //Date
    implementation("com.soywiz.korlibs.klock:klock:$klockVersion")

    // Tests
    implementation("androidx.test.espresso:espresso-idling-resource:3.5.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation("androidx.test:runner:1.5.2")
    implementation("androidx.test:rules:1.5.0")
    debugImplementation("androidx.test:core:1.5.0")
    debugImplementation("androidx.test:monitor:1.6.1")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:$composeVersion")
    debugImplementation("androidx.compose.ui:ui-test-manifest:$composeVersion")
    androidTestImplementation("com.google.dagger:hilt-android-testing:2.44")
    kaptAndroidTest("com.google.dagger:hilt-android-compiler:2.46")
}

kapt {
    correctErrorTypes = true
}
