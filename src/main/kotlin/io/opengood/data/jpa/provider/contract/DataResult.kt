package io.opengood.data.jpa.provider.contract

data class DataResult(
    val pages: Page,
    val records: Record,
    val data: List<Map<String, Any>>
) {
    data class Page(
        val index: Int,
        val size: Int,
        val count: Int
    ) {
        companion object {
            val EMPTY = Page(
                index = 0,
                size = 0,
                count = 0
            )
        }
    }

    data class Record(
        val total: Long
    ) {
        companion object {
            val EMPTY = Record(
                total = 0
            )
        }
    }

    companion object {
        val EMPTY = DataResult(
            pages = Page.EMPTY,
            records = Record.EMPTY,
            data = emptyList()
        )
    }
}
