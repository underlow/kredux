plugins {
    kotlin("js")
    kotlin("plugin.serialization")

}

repositories {
    mavenCentral()
//    mavenLocal()
    maven { url = uri("https://maven.pkg.jetbrains.space/public/p/kotlinx-html/maven") }
}

kotlin {
    js(/*IR*/) {
        browser {
        }
        binaries.executable()
    }
}


rootProject.plugins.withType(org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootPlugin::class.java) {
    rootProject.the<org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootExtension>().versions.webpackCli.version = "4.9.0"
}

dependencies {
    implementation(kotlin("stdlib-js"))
    implementation(kotlin("test-js"))

    implementation("org.jetbrains.kotlin:kotlin-stdlib-js")
    testImplementation("org.jetbrains.kotlin:kotlin-test-js")

    implementation("org.jetbrains.kotlin:kotlin-serialization:1.3.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-js:1.4.2")
//    kotlin-react:17.0.2-pre.260-kotlin-1.5.31")
    implementation(enforcedPlatform("org.jetbrains.kotlin-wrappers:kotlin-wrappers-bom:0.0.1-pre.260-kotlin-1.5.31"))

    implementation ("org.jetbrains.kotlin-wrappers:kotlin-react")
    implementation ("org.jetbrains.kotlin-wrappers:kotlin-react-dom")
    implementation ("org.jetbrains.kotlinx:kotlinx-html:0.7.2")
    implementation ("org.jetbrains.kotlin-wrappers:kotlin-styled")
    implementation ("org.jetbrains.kotlin-wrappers:kotlin-extensions")


    // to run example kredux.jar file should be got from maven, uncomment this, publish kredux project to local maven and fix version
//                implementation "com.github.underlow:kredux-js:master-SNAPSHOT"


    // project won't work with this but will be built on CI
    implementation(project(":kredux"))

//    implementation(npm("core-js", "^2.0.0"))
//    implementation(npm("kotlinx-html", "0.7.2"))
//    implementation(npm("@jetbrains/kotlin-react", "16.9.0-pre.91"))
//    implementation(npm("@jetbrains/kotlin-react-dom", "16.9.0-pre.91"))
//    implementation(npm("@jetbrains/kotlin-styled", "1.0.0-pre.67"))
//    implementation(npm("@jetbrains/kotlin-extensions", "1.0.1-pre.91"))
//    implementation(npm("@jetbrains/kotlin-css", "1.0.0-pre.67"))
//    implementation(npm("@jetbrains/kotlin-css-js", "1.0.0-pre.67"))
    implementation(npm("react", "17.0.2"))
    implementation(npm("react-dom", "17.0.2"))
//    implementation(npm("inline-style-prefixer", "5.0.4"))
//    implementation(npm("styled-components", "3.4.10"))
//                implementation(npm("react-router-dom", "^4.3.1"))


//    testImplementation(npm("enzyme", "3.9.0"))
//    testImplementation(npm("enzyme-adapter-react-16", "1.12.1"))
}

