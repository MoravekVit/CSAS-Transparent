package com.moravekvit.csastransparent.data.pagination

import com.moravekvit.csastransparent.domain.NetworkError
import com.moravekvit.csastransparent.domain.Result
import com.moravekvit.csastransparent.util.Paginator

class DefaultPaginator<Key, Item>(
    private val initialKey: Key,
    private inline val onLoadUpdated: (Boolean) -> Unit,
    private inline val onRequest: suspend (nextKey: Key) -> Result<Item, NetworkError>,
    private inline val getNextKey: suspend (Item) -> Key,
    private inline val onError: suspend (NetworkError) -> Unit,
    private inline val onSuccess: suspend (item: Item, newKey: Key) -> Unit
) : Paginator<Key, Item> {

    private var currentKey: Key = initialKey

    private var isMakingRequest = false

    override suspend fun loadNextItems() {
        if (isMakingRequest) {
            return
        }
        isMakingRequest = true
        onLoadUpdated(true)
        val result = onRequest(currentKey)
        isMakingRequest = false
        if (result is Result.Error) {
            onError(result.error)
            onLoadUpdated(false)
            return
        }
        val item = (result as Result.Success).data
        currentKey = getNextKey(item)
        onSuccess(item, currentKey)
        onLoadUpdated(false)
    }

    override fun reset() {
        currentKey = initialKey
    }

}