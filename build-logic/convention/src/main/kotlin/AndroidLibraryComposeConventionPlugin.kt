import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.LibraryExtension
import helpers.configureAndroidCompose
import helpers.configureKotlinAndroid
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType

class AndroidLibraryComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply(plugin = "navigation.android.library")
            apply(plugin = "org.jetbrains.kotlin.plugin.compose")
            apply(plugin = "org.jetbrains.kotlinx.kover")

            val extension = extensions.getByType<LibraryExtension>()
            val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

            configureAndroidCompose(extension, libs)

            extensions.configure<LibraryExtension> {
                lint {
                    abortOnError = false
                    htmlReport = true // Keeps your human-readable artifact
                    xmlReport = true // CRITICAL: Generates the machine-readable XML for Sonar

                    // NO: Do not turn warnings into errors. Keep them as warnings.
                    warningsAsErrors = false
                }
            }
        }
    }
}