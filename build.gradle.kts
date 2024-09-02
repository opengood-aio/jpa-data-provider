plugins {
    id("io.opengood.gradle.config")
    id("org.flywaydb.flyway")
    id("net.saliman.properties")
}

group = "io.opengood.data"

opengood {
    features {
        assertj = true
        junitJupiter = true
        lombok = true
    }
    artifact {
        description = "JPA data provider framework providing reusable data retrieval and persistence interface and mapping layer between consumer and data repository/entities"
    }
    test {
        maxParallelForks = 1

    }
}

dependencies {
    api("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("io.opengood.commons:kotlin-commons:_")
    implementation("jakarta.persistence:jakarta.persistence-api:_")

    runtimeOnly("org.postgresql:postgresql:_")

    testImplementation("io.opengood.extensions:kotest-extensions:_")
}

allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.Embeddable")
    annotation("jakarta.persistence.MappedSuperclass")
}

noArg {
    annotation("io.opengood.commons.kotlin.annotation.NoArg")
    invokeInitializers = true
}

flyway {
    schemas = arrayOf("dbo")
    locations = arrayOf("filesystem:migrations")
}
