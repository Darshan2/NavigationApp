plugins {
    alias(libs.plugins.navigation.android.library.compose)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.example.jobs.ui"
}

dependencies {
    implementation(project(":jobs-core"))
    implementation(project(":common-ui"))
    implementation(project(":common-core"))

    testImplementation(project(":test-utils"))

    implementation(libs.bundles.androidx.lifecycle.core)

    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.ui)

//    testImplementation(libs.bundles.junit.jupiter)
//    testImplementation(libs.junit.jupiter.engine)
    testImplementation(libs.bundles.junit4.test)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}

//tasks.withType<Test> {
//    useJUnitPlatform()
//}