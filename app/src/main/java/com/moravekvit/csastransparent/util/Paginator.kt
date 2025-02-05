package com.moravekvit.csastransparent.util

interface Paginator<Key, Item> {
    suspend fun loadNextItems()
    fun reset()
}