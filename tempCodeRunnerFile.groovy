plugins {
    id 'com.android.application'
    id 'org.jetbrains.kotlin.android'
    id 'kotlin-android'
    id 'kotlin-kapt'
}

android {
    namespace 'com.hyouteki.projects.droid'
    compileSdk 33

    defaultConfig {
        applicationId "com.hyouteki.projects.droid"
        minSdk 21
        targetSdk 33
        versionCode 1
        versionName "1.0"

        testInstrumentationRunner "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            minifyEnabled false
            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
        }
    }
    compileOptions {
        sourceCompatibility JavaVersion.VERSION_11
        targetCompatibility JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = '1.8'
    }
    buildFeatures {
        viewBinding true
    }
}

dependencies {
    implementation 'androidx.core:core-ktx:1.7.0'
    implementation 'androidx.appcompat:appcompat:1.6.1'
    implementation 'com.google.android.material:material:1.8.0'
    implementation 'androidx.constraintlayout:constraintlayout:2.1.4'
    implementation 'androidx.navigation:navigation-fragment-ktx:2.5.3'
    implementation 'androidx.navigation:navigation-ui-ktx:2.5.3'
    implementation 'androidx.preference:preference:1.1.+'
    testImplementation 'junit:junit:4.13.2'
    androidTestImplementation 'androidx.test.ext:junit:1.1.5'
    androidTestImplementation 'androidx.test.espresso:espresso-core:3.5.1'

    // Room components
    implementation "androidx.room:room-ktx:2.5.1"
    implementation 'androidx.legacy:legacy-support-v4:1.0.0'
    implementation 'androidx.browser:browser:1.5.0'
    kapt "androidx.room:room-compiler:2.5.1"
    androidTestImplementation "androidx.room:room-testing:2.5.1"

//    // Lifecycle components
//    implementation "androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1"
//    implementation "androidx.lifecycle:lifecycle-livedata-ktx:2.6.1"
//    implementation "androidx.lifecycle:lifecycle-common-java8:2.6.1"

    // lifecycleScope:
    implementation 'androidx.lifecycle:lifecycle-runtime-ktx:2.2.0-alpha04'

    // viewModelScope:
    implementation 'androidx.lifecycle:lifecycle-viewmodel-ktx:2.2.0-alpha04'


    // For fragment mvvm
    implementation 'androidx.fragment:fragment-ktx:1.5.7'

    // for volley and glide library
    implementation 'com.android.volley:volley:1.2.1'
    implementation 'com.github.bumptech.glide:glide:4.13.0'
    annotationProcessor 'com.github.bumptech.glide:compiler:4.13.0'
}