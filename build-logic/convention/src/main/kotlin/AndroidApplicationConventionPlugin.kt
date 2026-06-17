import com.android.build.api.dsl.ApplicationExtension
import helpers.configureAndroidCompose
import helpers.configureKotlinAndroid
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType

class AndroidApplicationConventionPlugin: Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply(plugin = "com.android.application")
            apply(plugin = "org.jetbrains.kotlin.android")

            val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

            extensions.configure<ApplicationExtension> {
                configureKotlinAndroid(this, libs)
                defaultConfig.targetSdk = 35
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