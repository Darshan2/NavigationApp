plugins {
    alias(libs.plugins.navigation.android.library.compose)
}

android {
    namespace = "com.example.common_ui"
}

dependencies {
    implementation(project(":common-core"))

    implementation(libs.bundles.androidx.lifecycle.core)
    implementation(libs.androidx.activity.compose)
    implementation(libs.material)
}