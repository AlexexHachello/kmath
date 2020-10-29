plugins {
    id("ru.mipt.npm.project")
}

internal val kmathVersion: String by extra("0.2.0-dev-3")
internal val bintrayRepo: String by extra("kscience")
internal val githubProject: String by extra("kmath")

allprojects {
    repositories {
        jcenter()
        maven("https://dl.bintray.com/kotlin/kotlin-eap")
        maven("https://dl.bintray.com/kotlin/kotlinx")
        maven("https://dl.bintray.com/hotkeytlt/maven")
        maven("https://jitpack.io")
    }

    group = "kscience.kmath"
    version = kmathVersion
}

subprojects {
    if (name.startsWith("kmath")) apply<ru.mipt.npm.gradle.KSciencePublishPlugin>()
}

readme {
    readmeTemplate = file("docs/templates/README-TEMPLATE.md")
}

apiValidation {
    validationDisabled = true
}
