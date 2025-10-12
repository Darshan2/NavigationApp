plugins {
    alias(libs.plugins.navigation.android.library)
}

android {
    namespace = "com.example.test_utils"
}

dependencies {
    api(libs.androidx.lifecycle.livedata.core)
    implementation(libs.bundles.junit4.test)
}
