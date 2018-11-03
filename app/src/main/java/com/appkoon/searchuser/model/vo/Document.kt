package com.appkoon.searchuser.model.vo

data class Document(
        val total_count: Int,
        val incomplete_results: Boolean,
        val items: List<Item>
)