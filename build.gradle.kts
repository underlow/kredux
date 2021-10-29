
plugins {
    kotlin("multiplatform") version "1.5.31" apply false
    kotlin("plugin.serialization") version "1.5.31" apply false
    id ("net.saliman.properties") version "1.5.1" apply false
}


allprojects {
    group = "com.github.underlow"
    version = "0.9"
}
