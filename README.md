# JPA Data Provider

[![Build](https://github.com/opengoodio/jpa-data-provider/workflows/build/badge.svg)](https://github.com/opengoodio/jpa-data-provider/actions?query=workflow%3Abuild)
[![Release](https://github.com/opengoodio/jpa-data-provider/workflows/release/badge.svg)](https://github.com/opengoodio/jpa-data-provider/actions?query=workflow%3Arelease)
[![Codecov](https://codecov.io/gh/opengoodio/jpa-data-provider/branch/main/graph/badge.svg?token=AEEYTGK87F)](https://codecov.io/gh/opengoodio/jpa-data-provider)
[![Release Version](https://img.shields.io/github/release/opengoodio/jpa-data-provider.svg)](https://github.com/opengoodio/jpa-data-provider/releases/latest)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.opengood.data/jpa-data-provider/badge.svg)](https://maven-badges.herokuapp.com/maven-central/io.opengood.data/jpa-data-provider)
[![License](https://img.shields.io/badge/license-MIT-blue.svg)](https://raw.githubusercontent.com/opengoodio/jpa-data-provider/master/LICENSE)
[![FOSSA](https://app.fossa.com/api/projects/custom%2B22161%2Fgithub.com%2Fopengoodio%2Fjpa-data-provider.svg?type=small)](https://app.fossa.com/projects/custom%2B22161%2Fgithub.com%2Fopengoodio%2Fjpa-data-provider?ref=badge_small)

JPA data provider framework providing reusable data retrieval and
persistence interface and mapping layer between consumer and data
repository/entities

## Setup

### Add Dependency

#### Gradle

```groovy
implementation("io.opengood.data:jpa-data-provider:VERSION")
```

#### Maven

```xml
<dependency>
    <groupId>io.opengood.data</groupId>
    <artifactId>jpa-data-provider</artifactId>
    <version>VERSION</version>
</dependency>
```

**Note:** See *Release* version badge above for latest version.

## Testing

### Postgres Database

Test data is stored in a Postgres database. A local database is used for
testing and local development.

Flyway is used to perform database DDL and DML changes to the database.

#### Local

To create database service account, copy `create_database_user.sql` from
Vault to `migrations` and run:

```bash
psql postgres -f migrations/create_database_user.sql
```

To create database, run:

```bash
psql postgres -f migrations/create_database.sql
```

To deploy database schema, copy `gradle-default.properties` from
Vault to project root and run:

```bash
./gradlew flywayMigrate -PenvironmentName=default
```

## Features

When developing REST APIs with data retrieval and persistence to a
JPA-enabled data repository, one typically has to write boilerplate code
mapping a model to an entity object.

The JPA Data Provider framework providers a simple means to
automatically perform data retrieval and persistence using a common
interface from mappings defined in a special `JpaDataProvider<T, Id>`
class implementation.

Simply define the mappings and have the data provider take of the rest:

### Entity Class

```kotlin
@Entity(name = "products")
data class Entity(
    @Id
    val id: UUID?,
    val name: String?,
    val sku: String?
)
```

### Data Repository

```kotlin
@Repository
interface DataRepository : JpaRepository<Entity, UUID>
```

### Data Provider

```kotlin
@Component
class DataProvider(
    override val repository: DataRepository
) : JpaDataProvider<Entity, UUID> {

    override val name: String = "products"
    override val mappings: Map<String, String> =
        mapOf(
            "product_id" to "id",
            "product_name" to "name",
            "product_sku" to "sku"
        )

    override fun filterMapper(filters: Map<String, Any>): Entity =
        Entity(
            id = nullableObjectValue("id", filters, convertToUuid),
            name = nullableObjectValue("name", filters, convertToString),
            sku = nullableObjectValue("sku", filters, convertToString)
        )

    override fun objectMapper(row: Map<String, Any>): Entity =
        Entity(
            id = objectValue("id", row, Uuid.empty, convertToUuid),
            name = objectValue("name", row, String.empty, convertToString),
            sku = objectValue("sku", row, String.empty, convertToString)
        )

    override fun rowMapper(o: Entity): Map<String, Any> {
        val map = mutableMapOf<String, Any>()
        with(map) {
            putIfNotAbsent(rowValue("id", o.id, Uuid.empty, convertFromUuid))
            putIfNotAbsent(rowValue("name", o.name, String.empty, convertToString))
            putIfNotAbsent(rowValue("sku", o.sku, String.empty, convertToString))
        }
        return map
    }
}
```

*Notes:*

* `mappings` provides the mappings from the consumer model to the entity
  class via a `Map<String, String>`
  * `key` is the consumer model field name
  * `value` is the entity class field name
* `filterMapper` provides the filters available used to filter data
* `objectMapper` constructs an entity class from a row of data
* `rowMapper` constructs a row of data from an entity class

