package com.sachin.shopping.demo.data.repository

import com.sachin.shopping.demo.data.local.dao.ProductDao
import com.sachin.shopping.demo.data.model.ProductListItem
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchRepository @Inject constructor(private val dao: ProductDao) {

    fun search(query: String): Flow<List<ProductListItem>> = dao.search(query.escapeLike())
}

// escape LIKE wildcards so "50%" or "a_b" are searched literally
private fun String.escapeLike() =
    replace("\\", "\\\\").replace("%", "\\%").replace("_", "\\_")