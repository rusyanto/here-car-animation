import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
}

val localProps = Properties()
val localPropsFile = rootProject.file("local.properties")
if (localPropsFile.exists()) {
    localProps.load(localPropsFile.inputStream())
} else {
    throw GradleException("local.properties not found in project root.")
}

val accessKeyId: String = localProps.getProperty("here.accessKeyId")
    ?: throw GradleException("Missing here.accessKeyId in local.properties")

val accessKeySecret: String = localProps.getProperty("here.accessKeySecret")
    ?: throw GradleException("Missing here.accessKeySecret in local.properties")

android {
    namespace = "com.example.caranimation"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.caranimation"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildFeatures {
            buildConfig = true
        }

        buildConfigField("String", "HERE_ACCESS_KEY_ID", "\"$accessKeyId\"")
        buildConfigField("String", "HERE_ACCESS_KEY_SECRET", "\"$accessKeySecret\"")
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
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation(fileTree(mapOf(
        "dir" to "libs",
        "include" to listOf("*.aar", "*.jar"),
        "exclude" to listOf("*mock*.jar")
    )))
    implementation(mapOf("name" to "heresdk-explore-android-4.22.5.0.197265", "ext" to "aar"))
}