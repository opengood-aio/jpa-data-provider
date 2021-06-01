package io.opengood.data.jpa.provider.contract

data class Sorting(
    val params: List<SortingParameter>
) {
    companion object {
        val EMPTY = Sorting(
            params = emptyList()
        )
    }
}
