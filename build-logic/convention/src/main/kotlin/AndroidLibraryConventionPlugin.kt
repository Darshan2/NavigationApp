
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import com.android.build.api.dsl.LibraryExtension
import helpers.configureKotlinAndroid
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.getByType

class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply(plugin = "com.android.library")
            apply(plugin = "org.jetbrains.kotlin.android")
            apply(plugin = "kotlin-parcelize")
            apply(plugin = "navigation.hilt")
            apply(plugin = "org.jetbrains.kotlinx.kover")

            val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
            extensions.configure<LibraryExtension> {
                configureKotlinAndroid(this, libs)

                lint {
                    abortOnError = false
                    htmlReport = true // Keeps your human-readable artifact
                    xmlReport = true // CRITICAL: Generates the machine-readable XML for Sonar

                    // NO: Do not turn warnings into errors. Keep them as warnings.
                    warningsAsErrors = false
                }

                defaultConfig {
                    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                }
            }
        }
    }
}