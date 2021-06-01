package io.opengood.data.jpa.provider

import io.opengood.commons.kotlin.function.makeEntry
import io.opengood.commons.kotlin.infix.firstOrDefault

fun JpaDataProvider<*, *>.getObjectMapping(name: String) =
    mappings.filter { it.key == name }.map { it.value }.first()

fun JpaDataProvider<*, *>.getRowMapping(name: String) =
    mappings.filter { it.value == name }.map { it.key }.first()

fun <Out : Any> JpaDataProvider<*, *>.nullableObjectValue(
    name: String,
    row: Map<String, Any>,
    converter: (Any?) -> Out?
): Out? {
    if (mappings.containsValue(name)) {
        val key = getRowMapping(name)
        return if (row.containsKey(key)) converter(row[key]) else null
    }
    throw IllegalArgumentException("Mapping not found: $name")
}

fun <In : Any, Out : Any> JpaDataProvider<*, *>.nullableRowValue(
    name: String,
    value: In?,
    converter: (In?) -> Out?
): Map.Entry<String, Any>? {
    if (mappings.containsValue(name)) {
        if (value != null) {
            val key = getRowMapping(name)
            val result = converter(value)
            if (result != null) {
                return makeEntry(key, result)
            }
        }
        return null
    }
    throw IllegalArgumentException("Mapping not found: $name")
}

fun <Out : Any> JpaDataProvider<*, *>.objectValue(
    name: String,
    row: Map<String, Any>,
    default: Out,
    converter: (Any?) -> Out?
): Out =
    nullableObjectValue(name, row, converter).firstOrDefault(default)

fun <In : Any, Out : Any> JpaDataProvider<*, *>.rowValue(
    name: String,
    value: In?,
    default: In,
    converter: (In?) -> Out?
): Map.Entry<String, Any> =
    nullableRowValue(name, value, converter).firstOrDefault(makeEntry(getRowMapping(name), default))
