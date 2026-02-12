package com.kxtdev.bukkydatasup.domain.pagingsource

import androidx.paging.PagingSource
import androidx.paging.PagingState

class BasePagingSourceMapper<A: Any, B: Any>(
    private val parentPagingSource: () -> PagingSource<Int, A>,
    private val firstMapper: (B) -> A,
    private val secondMapper: (A) -> B,
    ): PagingSource<Int, B>() {
    override fun getRefreshKey(state: PagingState<Int,B>): Int? {
        return parentPagingSource.invoke().getRefreshKey(
          state.mapState { firstMapper.invoke(it) }
        )
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, B> {
        return when(val result = parentPagingSource.invoke().load(params)) {
            is LoadResult.Page -> {
                LoadResult.Page(
                    data = result.data.map { secondMapper.invoke(it) },
                    prevKey = result.prevKey,
                    nextKey = result.nextKey,
                )
            }
            is LoadResult.Error -> {
                LoadResult.Error(result.throwable)
            }

            is LoadResult.Invalid -> {
                LoadResult.Error(Throwable("Invalid Response"))
            }
        }
    }

}

private fun <A:Any,B:Any> PagingState<Int,B>.mapState(mapper: (B) -> A): PagingState<Int,A> {
    return PagingState(
        pages = pages.map { page ->
            PagingSource.LoadResult.Page(
                data = page.data.map(mapper),
                prevKey = page.prevKey,
                nextKey = page.nextKey,
                itemsBefore = page.itemsBefore,
                itemsAfter = page.itemsAfter,
            )
        },
        anchorPosition = anchorPosition,
        config = config,
        leadingPlaceholderCount = 0
    )
}

