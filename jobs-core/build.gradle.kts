plugins {
    alias(libs.plugins.navigation.android.library)
}

android {
    namespace = "com.example.jobs_core"
}

dependencies {
    implementation(project(":common-core"))
    testImplementation(project(":test-utils"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)

    //Retrofit
    implementation(libs.bundles.retrofit)

    testImplementation(libs.bundles.junit4.test)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    testImplementation(kotlin("test"))
}