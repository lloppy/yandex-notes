// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    extra.apply {
        set(
            "room_version",
            "2.6.1"
        ) // https://mvnrepository.com/artifact/androidx.room/room-runtime
    }
}

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.android.library) apply false

    kotlin("jvm") version "2.1.10" apply false
    id("com.google.devtools.ksp") version "2.1.10-1.0.31" apply false
}