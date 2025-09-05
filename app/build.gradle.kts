<<<<<<< HEAD
    plugins {
        alias(libs.plugins.android.application)
        alias(libs.plugins.kotlin.android)
        alias(libs.plugins.kotlin.compose)
        id("com.google.devtools.ksp")
       }

    android {
        namespace = "com.example.test_2"
        compileSdk = 36
        defaultConfig {
            applicationId = "com.example.test_2"
            minSdk = 26
            targetSdk = 36
            versionCode = 1
            versionName = "1.0"
            testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        }
        buildTypes {
            release {
                isMinifyEnabled = false
                proguardFiles(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro"
                )
            }
        }
        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_11
            targetCompatibility = JavaVersion.VERSION_11
        }
        kotlinOptions {
            jvmTarget = "11"
        }
        buildFeatures {
            compose = true
        }
        composeOptions {
            kotlinCompilerExtensionVersion = "2.0.20" // Compatível com Kotlin 2.0.20
        }
    }

    dependencies {
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.0")
        implementation(platform("androidx.compose:compose-bom:2024.10.00")) // Versão estável
        implementation("androidx.compose.material3:material3")
        implementation("androidx.compose.ui:ui")
        implementation("androidx.compose.ui:ui-tooling-preview")
        implementation("androidx.constraintlayout:constraintlayout-compose:1.0.1")
        implementation(libs.material3)
        implementation(libs.volley)
        implementation(libs.androidx.glance)
        implementation(libs.androidx.foundation)
        implementation(libs.androidx.media3.common.ktx)
        debugImplementation("androidx.compose.ui:ui-tooling")
        implementation("androidx.navigation:navigation-compose:2.8.0")
        implementation(libs.androidx.core.ktx)
        implementation(libs.androidx.lifecycle.runtime.ktx)
        implementation(libs.androidx.activity.compose)
        testImplementation(libs.junit)
        androidTestImplementation(libs.androidx.junit)
        androidTestImplementation(libs.androidx.espresso.core)
        androidTestImplementation(libs.androidx.ui.test.junit4)
        debugImplementation(libs.androidx.ui.test.manifest)
        implementation("androidx.core:core-ktx:1.13.1")
        implementation("androidx.activity:activity-compose:1.9.3")
        implementation("androidx.compose:compose-bom:2024.10.00")
        implementation("androidx.compose.material3:material3")
        implementation("androidx.compose.material:material-icons-extended")
        implementation("androidx.navigation:navigation-compose:2.8.3")
        implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")
        implementation("io.coil-kt:coil-base:2.7.0")
        implementation("io.coil-kt:coil-compose:2.7.0")
        androidTestImplementation(libs.androidx.junit)
        androidTestImplementation(libs.androidx.espresso.core)
        implementation("de.raphaelebner:roomdatabasebackup:1.1.0")

        implementation("com.google.code.gson:gson:2.10.1")

        // Room

        val room_version = "2.7.2"

        implementation("androidx.room:room-runtime:$room_version")
        // If this project uses any Kotlin source, use Kotlin Symbol Processing (KSP)
        // See Add the KSP plugin to your project
        ksp("androidx.room:room-compiler:$room_version")
        // Coroutines
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.1")



    }
=======
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    kotlin("kapt")

}

android {
    namespace = "com.example.CarteoGest"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.CarteoGest"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
        freeCompilerArgs = listOf("-Xjvm-default=all")
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "2.0.20"
    }
}

dependencies {
    implementation("de.raphaelebner:roomdatabasebackup:1.1.0")

    implementation("com.google.code.gson:gson:2.10.1")

    implementation("org.jetbrains.kotlinx:kotlinx-metadata-jvm:0.6.1") // versão recente
    //salva estado
    implementation("androidx.datastore:datastore-preferences:1.1.0")
    // Room
    implementation("androidx.room:room-runtime:2.7.2")
    implementation(libs.androidx.media3.common.ktx)
    implementation("androidx.work:work-runtime-ktx:2.9.1")
    implementation("androidx.core:core:1.13.1")

    kapt("androidx.room:room-compiler:2.7.2")

    // Room + Kotlin Coroutines
    implementation("androidx.room:room-ktx:2.7.2")

    // Outros exemplos de dependências comuns
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")

    implementation("androidx.compose.material3:material3:1.1.0")
    implementation("androidx.compose.ui:ui:1.7.0")
    implementation("androidx.compose.material:material-icons-core:1.7.0")
    implementation("androidx.compose.material:material-icons-extended:1.7.0")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.6.0")
    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")
    implementation("io.coil-kt:coil-base:2.7.0")
    implementation("io.coil-kt:coil-compose:2.7.0")
    implementation("androidx.compose.ui:ui")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx")
    implementation("androidx.activity:activity-compose:1.10.1")
    implementation("androidx.compose.material:material-icons-extended:<versão>")
    implementation(libs.androidx.appcompat)
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.10.2")
    implementation(platform("androidx.compose:compose-bom:2025.07.00")) // Versão estável
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.constraintlayout:constraintlayout-compose:1.1.1")
    debugImplementation("androidx.compose.ui:ui-tooling")
    implementation("androidx.navigation:navigation-compose:2.8.0")

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}
>>>>>>> master
