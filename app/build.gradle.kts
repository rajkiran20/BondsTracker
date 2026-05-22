import com.google.firebase.appdistribution.gradle.firebaseAppDistribution

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
    alias(libs.plugins.firebase.appdistribution)
}

android {
    namespace = "app.le.bondstracker"
    compileSdk = 35

    defaultConfig {
        applicationId = "app.le.bondstracker"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("debug")
            firebaseAppDistribution {
                artifactType = "APK"
                appId = project.findProperty("FIREBASE_APP_ID")?.toString()
                    ?: System.getenv("FIREBASE_APP_ID")
                    ?: ""
                testers = project.findProperty("FIREBASE_TESTERS")?.toString()
                    ?: System.getenv("FIREBASE_TESTERS")
                    ?: ""
                releaseNotes = project.findProperty("FIREBASE_RELEASE_NOTES")?.toString()
                    ?: System.getenv("FIREBASE_RELEASE_NOTES")
                    ?: "New release build"
                
                // Fetch FIREBASE_TOKEN project property or env variable.
                // The Firebase App Distribution plugin natively reads it during upload.
                val firebaseToken = project.findProperty("FIREBASE_TOKEN")?.toString()
                    ?: System.getenv("FIREBASE_TOKEN")
                    ?: ""
                if (firebaseToken.isNotEmpty()) {
                    logger.info("Firebase App Distribution: Release build token configured.")
                }

                val serviceAccount = project.findProperty("FIREBASE_SERVICE_ACCOUNT_FILE")?.toString()
                    ?: System.getenv("FIREBASE_SERVICE_ACCOUNT_FILE")
                    ?: ""
                if (serviceAccount.isNotEmpty()) {
                    serviceCredentialsFile = serviceAccount
                }
            }
        }
        getByName("debug") {
            firebaseAppDistribution {
                artifactType = "APK"
                appId = project.findProperty("FIREBASE_APP_ID")?.toString()
                    ?: System.getenv("FIREBASE_APP_ID")
                    ?: ""
                testers = project.findProperty("FIREBASE_TESTERS")?.toString()
                    ?: System.getenv("FIREBASE_TESTERS")
                    ?: ""
                releaseNotes = project.findProperty("FIREBASE_RELEASE_NOTES")?.toString()
                    ?: System.getenv("FIREBASE_RELEASE_NOTES")
                    ?: "New debug build"
                
                // Fetch FIREBASE_TOKEN project property or env variable
                val firebaseToken = project.findProperty("FIREBASE_TOKEN")?.toString()
                    ?: System.getenv("FIREBASE_TOKEN")
                    ?: ""
                if (firebaseToken.isNotEmpty()) {
                    logger.info("Firebase App Distribution: Debug build token configured.")
                }

                val serviceAccount = project.findProperty("FIREBASE_SERVICE_ACCOUNT_FILE")?.toString()
                    ?: System.getenv("FIREBASE_SERVICE_ACCOUNT_FILE")
                    ?: ""
                if (serviceAccount.isNotEmpty()) {
                    serviceCredentialsFile = serviceAccount
                }
            }
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
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.material.icons.extended)
    implementation(libs.androidx.navigation.compose)

    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    implementation(libs.hilt.navigation.compose)

    // Room
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)

    // WorkManager
    implementation(libs.work.runtime.ktx)
    implementation(libs.hilt.work)
    ksp(libs.hilt.work.compiler)

    // Gson
    implementation(libs.gson)

    // Coroutines
    implementation(libs.kotlinx.coroutines.android)

    // DataStore
    implementation(libs.androidx.datastore.preferences)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}
