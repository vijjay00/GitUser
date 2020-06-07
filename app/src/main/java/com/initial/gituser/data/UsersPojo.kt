package com.initial.gituser.data

data class UsersPojo(
    val incomplete_results: Boolean,
    val items: List<Item>,
    val total_count: Int
)