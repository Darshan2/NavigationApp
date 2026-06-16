// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.hilt.android) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.jetbrains.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.android.test) apply false
    alias(libs.plugins.kotlinx.kover) apply true
}

dependencies {
    kover(project(":app"))
    kover(project(":common-core"))
    kover(project(":common-ui"))
    kover(project(":jobs-core"))
    kover(project(":jobs-ui"))
}

// 3. Define the aggregated output rules
kover {
    reports {
        total {
            xml { onCheck = true }
            html { onCheck = true }
        }
    }
}