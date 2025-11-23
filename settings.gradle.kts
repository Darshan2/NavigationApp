pluginManagement {
    includeBuild("build-logic")
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode = RepositoriesMode.FAIL_ON_PROJECT_REPOS
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "NavigationApp"
include(":app")
include(":jobs-core")
include(":jobs-ui")
include(":common-core")
include(":common-ui")
include(":test-utils")
include(":benchmark")
