pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
<<<<<<< HEAD
        maven { url = uri("https://jitpack.io") }
    }
=======
        maven { url = uri("https://jitpack.io") }    }
>>>>>>> master
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
<<<<<<< HEAD
        maven { url = uri("https://jitpack.io") }    
    }
}

rootProject.name = "test_2"
=======
        maven { url = uri("https://jitpack.io") }
    }
}

rootProject.name = "CarteoGest"
>>>>>>> master
include(":app")
 