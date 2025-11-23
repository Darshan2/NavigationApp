plugins {
    alias(libs.plugins.navigation.android.application.compose)
    alias(libs.plugins.navigation.hilt)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.navigation.android.navigation)
}

android {
    namespace = "com.example.navigationapp"

    defaultConfig {
        applicationId = "com.example.navigationapp"
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
        }

        create("benchmark") {
            initWith(buildTypes.getByName("release"))
            signingConfig = signingConfigs.getByName("debug")
            matchingFallbacks += listOf("release")
            isDebuggable = false
            proguardFiles("baseline-profile-rules.pro")
        }
    }

    defaultConfig {
        ksp {
            arg("room.schemaLocation", "$projectDir/schemas")
        }
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(project(":common-core"))
    implementation(project(":common-ui"))
    implementation(project(":jobs-ui"))
    implementation(project(":jobs-core"))

    implementation(libs.androidx.activity.compose)
    implementation(libs.material)

    implementation(libs.bundles.androidx.lifecycle.core)
    implementation(libs.bundles.retrofit)

    implementation(libs.androidx.lifecycle.service)

    //Room
    implementation(libs.bundles.room)
    ksp(libs.room.compiler) // Use ksp for Room's annotation processor



    //---------------- Unit test ---------------------------------------------------
    testImplementation(libs.junit4)

    // ---------------- Instrumentation test ---------------------------------------
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.room.testing)
}