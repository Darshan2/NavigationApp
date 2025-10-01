// refer: https://github.com/android/nowinandroid/blob/main/build-logic/convention/src/main/kotlin/AndroidLibraryConventionPlugin.kt

import com.android.build.api.dsl.ApplicationExtension
import helpers.configureAndroidCompose
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.getByType

class AndroidApplicationComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply(plugin = "navigation.android.application")
            apply(plugin = "org.jetbrains.kotlin.plugin.compose")

            val extension = extensions.getByType<ApplicationExtension>()
            val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

            configureAndroidCompose(extension, libs)
        }
    }

}