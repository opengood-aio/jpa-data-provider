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

Simply define the mappings and have the data provider take care of the
rest:

### Entity Class

```kotlin
@Entity(name = "products")
data class Entity(
    @Id
    val id: UUID?,
    val name: String?,
    val sku: String?,
    val category: String?
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
    override val id: String = "id"
    override val mappings: Map<String, String> =
        mapOf(
            "product_id" to "id",
            "product_name" to "name",
            "product_sku" to "sku",
            "product_category" to "category"
        )

    override fun idConverter(id: Any): UUID {
        return convertToUuid(id)!!
    }

    override fun filterMapper(filters: Map<String, Any>): Entity =
        Entity(
            id = nullableObjectFieldValue("id", filters, convertToUuid),
            name = nullableObjectFieldValue("name", filters, convertToString),
            sku = nullableObjectFieldValue("sku", filters, convertToString),
            category = nullableObjectFieldValue("category", filters, convertToString)
        )

    override fun objectFieldMapper(row: Map<String, Any>): Entity =
        Entity(
            id = objectFieldValue("id", row, Uuid.empty, convertToUuid),
            name = objectFieldValue("name", row, String.empty, convertToString),
            sku = objectFieldValue("sku", row, String.empty, convertToString),
            category = objectFieldValue("category", row, String.empty, convertToString)
        )

    override fun rowColumnMapper(o: Entity): Map<String, Any> {
        val map = mutableMapOf<String, Any>()
        with(map) {
            putIfNotAbsent(rowColumnValue("id", o.id, Uuid.empty, convertFromUuid))
            putIfNotAbsent(rowColumnValue("name", o.name, String.empty, convertToString))
            putIfNotAbsent(rowColumnValue("sku", o.sku, String.empty, convertToString))
            putIfNotAbsent(rowColumnValue("category", o.category, String.empty, convertToString))
        }
        return map
    }
}
```

#### Definitions

* `name` is the name of the data provider
* `id` is the name of the entity class `@Id` field
* `mappings` provides the mappings from the consumer model to the entity
  class via a `Map<String, Any>`
  * `key` is the consumer model field name
  * `value` is the entity class field name
* `idConverter` provides a function to convert an identifier value to an
entity class `@Id` field data type
* `filterMapper` provides the filters available used to filter data
* `objectFieldMapper` constructs an entity class from a row of data
* `rowColumnMapper` constructs a row of data from an entity class

### Converter Functions

Converter functions are provided to allow common data types of `Any` and
`String` to be converted to and from explicit entity object field class
types.

The following functions are supported:

| Function | Description | Input Type | Output Type |
|---|---|---|---|
| `convertFromDate` | Converts from SQL Date | `java.sql.Date` | `String` |
| `convertFromUuid` | Converts from UUID | `UUID` | `String` |
| `convertToDate` | Converts to SQL Date | `Any` | `java.sql.Date` |
| `convertToString` | Converts to String | `Any` | `String` |
| `convertToUuid` | Converts to UUID | `Any` | `UUID` |

### Data Contracts

#### `Filters`
#### `Paging`
#### `Sorting`
#### `SortDirection`
#### `SortParameter`

Data contracts for retrieving data. Includes filtering, paging, and
sorting.

Data in JSON format:

* `filters`: Map of key/value pairs representing fields in which to
  filter data from data repository
  * `key`: Name of field in which to filter data
  * `value`: Filter value for field
* `paging`: Pagination parameters in which to retrieve a page of data
  * `index`: Current index of page of data to retrieve
  * `size`: Number of rows of data per page to retrieve
* `sorting`: Map of key/value pairs representing fields and direction in
  which to sort data from data repository
  * `params`: Sorting parameters in which to sort data
    * `name`: Name of field in which to sort data
    * `direction`: Sort direction `ASC` or `DESC` of field

##### Example

```json
{
    "filters": {
        "key": "product_name",
        "value": "iPhone"
    },
    "paging": {
        "index": 0,
        "size": 2
    },
    "sorting": {
        "params": {
            "name": "product_name",
            "direction": "ASC"
        }
    }
}
```

#### `DataResult`

Data contract containing data result. Includes page and record data.

Data in JSON format:

* `pages`: Object containing information about page data
  * `index`: Current index of page of data retrieved
  * `size`: Number of rows of data in current page retrieved
  * `count`: Total number of pages in dataset
* `records`: Object containing information about record data
  * `total`: Total number of records in dataset
* `data`: Array containing map of key/value pairs representing row(s) of
  data retrieved from data repository

##### Example

```json
{
    "pages": {
        "index": 0,
        "size": 2,
        "count": 1
    },
    "records": {
        "total": 2
    },
    "data": [
        {
            "product_id": "50d113a6-24ff-43cd-bb1d-ca3aa1014e4c",
            "product_name": "Apple iPhone",
            "product_sku": "123456",
            "product_category": "mobile"
        },
        {
            "product_id": "81bc7dde-5c9e-4736-baee-b82dc5f249e3",
            "product_name": "Apple iPad",
            "product_sku": "098765",
            "product_category": "tablet"
        }
    ]
}
```

### Constants

Several constant object values are provided:

| Object | Constant | Description | Data Type |
|---|---|---|---|
| `Dates` | `SQL_MIN_DATE` | Provides a minimum SQL Date | `java.sql.Date` |

### Formats

Several object formatters are provided:

| Object | Constant | Description | Data Type |
|---|---|---|---|
| `Formats` | `ISO_DATE` | Formats a date to ISO date format | `SimpleDateFormat` |

