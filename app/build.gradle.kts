plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.gms.google.services)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.basilalasadi.iti.plateful"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.basilalasadi.iti.plateful"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
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
        viewBinding = true
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    implementation(libs.retrofit)
    implementation(libs.glide)
    implementation(libs.rxjava)
    implementation(libs.rxandroid)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.bom)
    implementation(libs.firebase.firestore)
    implementation(libs.lottie)

    implementation("com.github.Dimezis:BlurView:version-2.0.6")
    implementation(libs.core.ktx)
    implementation(libs.commons.text)
    implementation(libs.room.common)
    implementation(libs.room.runtime)
    implementation(libs.room.rxjava3)

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}