package io.opengood.data.jpa.provider.contract

data class Filtering(
    val params: List<FilteringParameter>
) {
    companion object {
        val EMPTY = Filtering(
            params = emptyList()
        )
    }
}
