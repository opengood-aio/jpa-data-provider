package io.opengood.data.jpa.provider.contract

data class DataResult(
    val pageInfo: PageInfo,
    val recordInfo: RecordInfo,
    val data: List<Map<String, Any>>,
) {
    companion object {
        val EMPTY =
            DataResult(
                pageInfo = PageInfo.EMPTY,
                recordInfo = RecordInfo.EMPTY,
                data = emptyList(),
            )
    }
}
