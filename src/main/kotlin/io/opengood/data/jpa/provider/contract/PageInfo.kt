package io.opengood.data.jpa.provider.contract

data class PageInfo(
    val index: Int,
    val size: Int,
    val count: Int,
) {
    companion object {
        val EMPTY =
            PageInfo(
                index = 0,
                size = 0,
                count = 0,
            )
    }
}
