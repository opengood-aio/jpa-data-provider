package io.opengood.data.jpa.provider.contract

data class Paging(
    val index: Int,
    val size: Int,
) {
    companion object {
        val EMPTY = Paging(
            index = 0,
            size = 0,
        )
    }
}
