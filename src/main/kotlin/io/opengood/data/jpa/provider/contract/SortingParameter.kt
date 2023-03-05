package io.opengood.data.jpa.provider.contract

import org.springframework.data.domain.Sort

data class SortingParameter(
    val name: String,
    val direction: SortingDirection,
)

internal fun SortingParameter.getSort() =
    if (direction == SortingDirection.DESC) Sort.by(name).descending() else Sort.by(name).ascending()
