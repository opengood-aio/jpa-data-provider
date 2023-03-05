package io.opengood.data.jpa.provider.contract

import org.springframework.data.domain.ExampleMatcher
import org.springframework.data.domain.ExampleMatcher.StringMatcher

data class FilteringParameter(
    val name: String,
    val value: Any,
    val type: FilteringType,
    val condition: FilteringCondition = FilteringCondition.OR,
) {
    companion object {
        val defaultMatcher: ExampleMatcher
            get() = ExampleMatcher.matchingAny().withStringMatcher(StringMatcher.CONTAINING).withIgnoreCase()
    }
}

internal fun FilteringParameter.getMatcher(matcher: ExampleMatcher) =
    when (type) {
        FilteringType.CONTAINS -> {
            matcher.withMatcher(name, ExampleMatcher.GenericPropertyMatcher().contains()).withIgnoreCase()
        }
        FilteringType.EQUALS -> {
            matcher.withMatcher(name, ExampleMatcher.GenericPropertyMatcher().exact())
        }
    }
