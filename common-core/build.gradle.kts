plugins {
    alias(libs.plugins.navigation.android.library)
}

android {
    namespace = "com.example.common_core"
}

dependencies {
    //Retrofit
    implementation(libs.bundles.retrofit)
}

