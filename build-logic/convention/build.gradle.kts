import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    `kotlin-dsl`
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_17
    }
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.android.tools.common)
    compileOnly(libs.compose.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.ksp.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("androidApplicationCompose") {
            id = libs.plugins.navigation.android.application.compose.get().pluginId
            implementationClass = "AndroidApplicationComposeConventionPlugin"
        }
        register("androidApplication") {
            id = libs.plugins.navigation.android.application.asProvider().get().pluginId
            implementationClass = "AndroidApplicationConventionPlugin"
        }

        register("androidLibraryCompose") {
            id = libs.plugins.navigation.android.library.compose.get().pluginId
            implementationClass = "AndroidLibraryComposeConventionPlugin"
        }
        register("androidLibrary") {
            id = libs.plugins.navigation.android.library.asProvider().get().pluginId
            implementationClass = "AndroidLibraryConventionPlugin"
        }
//        register("androidFeature") {
//            id = libs.plugins.navigation.android.feature.get().pluginId
//            implementationClass = "AndroidFeatureConventionPlugin"
//        }
//
//        register("androidTest") {
//            id = libs.plugins.navigation.android.test.get().pluginId
//            implementationClass = "AndroidTestConventionPlugin"
//        }
        register("hilt") {
            id = libs.plugins.navigation.hilt.get().pluginId
            implementationClass = "HiltConventionPlugin"
        }
//        register("androidRoom") {
//            id = libs.plugins.navigation.android.room.get().pluginId
//            implementationClass = "AndroidRoomConventionPlugin"
//        }
//        register("androidFlavors") {
//            id = libs.plugins.navigation.android.application.flavors.get().pluginId
//            implementationClass = "AndroidApplicationFlavorsConventionPlugin"
//        }
//
        register("jvmLibrary") {
            id = libs.plugins.navigation.jvm.library.get().pluginId
            implementationClass = "JvmLibraryConventionPlugin"
        }

        register("androidNavigation") {
            id = libs.plugins.navigation.android.navigation.get().pluginId
            implementationClass = "AndroidNavigationConventionPlugin"
        }
    }
}