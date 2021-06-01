import io.opengood.gradle.enumeration.ProjectType

plugins {
    id("io.opengood.gradle.config") version "1.22.1"
    id("org.flywaydb.flyway") version "7.9.1"
    id("net.saliman.properties") version "1.5.1"
}

group = "io.opengood.data"

opengood {
    main {
        projectType = ProjectType.LIB
    }
    artifact {
        description = "JPA data provider framework providing reusable data retrieval and persistence interface and mapping layer between consumer and data repository/entities"
    }
}

object Versions {
    const val JAVAX_PERSISTENCE = "2.2"
    const val KOTEST_EXTENSIONS = "1.0.0"
    const val KOTLIN_COMMONS = "1.12.0"
    const val POSTGRES = "42.2.20"
}

dependencies {
    api("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("io.opengood.commons:kotlin-commons:${Versions.KOTLIN_COMMONS}")
    implementation("javax.persistence:javax.persistence-api:${Versions.JAVAX_PERSISTENCE}")

    runtimeOnly("org.postgresql:postgresql:${Versions.POSTGRES}")

    testImplementation("io.opengood.extensions:kotest-extensions:${Versions.KOTEST_EXTENSIONS}")
}

allOpen {
    annotation("javax.persistence.Entity")
    annotation("javax.persistence.Embeddable")
    annotation("javax.persistence.MappedSuperclass")
}

noArg {
    annotation("io.opengood.commons.kotlin.annotation.NoArg")
    invokeInitializers = true
}

flyway {
    schemas = arrayOf("dbo")
    locations = arrayOf("filesystem:migrations")
}
