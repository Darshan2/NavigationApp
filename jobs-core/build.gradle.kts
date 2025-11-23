plugins {
    alias(libs.plugins.navigation.android.library)
}

android {
    namespace = "com.example.jobs.core"
}

dependencies {
    implementation(project(":common-core"))
    testImplementation(project(":test-utils"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)

    implementation(libs.bundles.retrofit)

    //Room
    implementation(libs.bundles.room)
    ksp(libs.room.compiler)

    implementation(libs.paging.runtime)

    //Testing
    testImplementation(libs.bundles.junit4.test)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    testImplementation(kotlin("test"))
}