package helpers

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.kotlin.dsl.dependencies

/**
 * Configure Compose-specific options
 */
internal fun Project.configureAndroidCompose(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
    libs: VersionCatalog
) {

    commonExtension.apply {
        buildFeatures {
            compose = true
        }

        dependencies {
            val composeBom = platform(libs.findLibrary("androidx-compose-bom").get())
            add("implementation", composeBom)
            add("androidTestImplementation", composeBom)

            add("implementation", libs.findLibrary("androidx-compose-material3").get())

            add("implementation", libs.findLibrary("androidx-compose-ui-tooling-preview").get())
            add("debugImplementation", libs.findLibrary("androidx-compose-ui-tooling").get())
            add("androidTestImplementation", libs.findLibrary("androidx.compose.ui.test.junit4").get())
            add("debugImplementation", libs.findLibrary("androidx-compose-ui-test-manifest").get())
        }
    }


}