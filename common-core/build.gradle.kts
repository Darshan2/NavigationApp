plugins {
    alias(libs.plugins.navigation.android.library)
}

android {
    namespace = "com.example.common_core"
}

dependencies {
    implementation(libs.bundles.retrofit)

    testImplementation(libs.bundles.junit4.test)
}

