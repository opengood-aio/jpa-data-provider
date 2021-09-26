package io.opengood.data.jpa.provider.contract

data class RecordInfo(
    val total: Long
) {
    companion object {
        val EMPTY = RecordInfo(
            total = 0
        )
    }
}
