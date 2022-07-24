plugins {
    kotlin("multiplatform")
    id("ru.mipt.npm.gradle.common")
    id("ru.mipt.npm.gradle.native")
}

kotlin.sourceSets.commonMain {
    dependencies {
        api(projects.kmath.kmathGeometry)
    }
}

readme {
    maturity = ru.mipt.npm.gradle.Maturity.PROTOTYPE
}