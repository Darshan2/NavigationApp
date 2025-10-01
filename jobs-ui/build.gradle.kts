plugins {
    alias(libs.plugins.navigation.android.library.compose)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.example.jobs_ui"
}

dependencies {
    implementation(project(":jobs-core"))
    implementation(project(":common-ui"))
    implementation(project(":common-core"))

    implementation(libs.bundles.androidx.lifecycle.core)

    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.ui)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}