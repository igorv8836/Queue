pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        maven ( url = uri("www.jitpack.io") )
        google()
        mavenCentral()
    }
}


rootProject.name = "Queue"
include(":app")