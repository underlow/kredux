buildscript {
    dependencies {
        classpath("com.jfrog.bintray.gradle:gradle-bintray-plugin:1.+")
    }
}

plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("maven-publish")
}


repositories {
    mavenLocal()
    maven { url = uri("https://dl.bintray.com/kotlin/ktor") }
    maven { url = uri("https://dl.bintray.com/kotlin/kotlin-js-wrappers") }
    maven { url = uri("https://dl.bintray.com/kotlin/kotlinx") }
    mavenCentral()
}


publishing {
    repositories {
        maven {
            name = "kredux"
            url = uri("https://maven.pkg.github.com/underlow/kredux")
            credentials {
                username = project.findProperty("gpr.user")?.toString() ?: System.getenv("GPR_USER")
                password = project.findProperty("gpr.key")?.toString() ?: System.getenv("GPR_API_KEY")
            }
        }
    }
}


kotlin {
    jvm() // Creates a JVM target with the default name 'jvm'
    js(/*IR*/) {  // JS target named 'js'
        browser()
    }

    //jsTest doesn't work to some npm dependencies resolve issue, so all common tests run against jvm yet
//    build.dependsOn jvmTest


    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib-common"))
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.2")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }

        // Default source set for JVM-specific sources and dependencies:
        jvm().compilations["main"].defaultSourceSet {
            dependencies {
                implementation(kotlin("stdlib-jdk8"))
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:1.5.2")
            }
        }
        // JVM-specific tests and their dependencies:
        jvm().compilations["test"].defaultSourceSet {
            dependencies {
                implementation(kotlin("test"))
                implementation(kotlin("test-junit"))
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.5.2")
            }
        }

        js().compilations["main"].defaultSourceSet {
            dependencies {
                implementation(kotlin("stdlib-js"))
                implementation("org.jetbrains.kotlin-wrappers:kotlin-react:17.0.2-pre.260-kotlin-1.5.31")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-js:1.5.2")
            }

        }
        js().compilations["test"].defaultSourceSet {
            dependencies {
                implementation(kotlin("test-js"))
            }
        }

    }
}



