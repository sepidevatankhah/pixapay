package ir.nwise.app.domain.entities

import ir.nwise.app.common.Config

data class PhotoModel(
    val query: String = Config.DEFAULT_FILTER,
    val pageSize: Int = Config.DEFAULT_PAGE_SIZE,
    val pageNum: Int = Config.DEFAULT_PAGE_NUM
)