package com.baghdad.viewmodel.base

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.baghdad.domain.model.PagedResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class PagedResultPagingSource<T : Any>(
    private val loadData: suspend (page: Int) -> PagedResult<T>,
    private val onInitialLoadFinished: suspend () -> Unit,
    private val onError: (Throwable) -> Unit
) : PagingSource<Int, T>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> {
        return try {
            val page = params.key ?: 1
            val result = withContext(Dispatchers.IO) {
                loadData(page)
            }
            if (params.key == null || page == 1) {
                onInitialLoadFinished()
            }
            LoadResult.Page(
                data = result.data,
                prevKey = result.prevKey,
                nextKey = result.nextKey
            )

        } catch (e: Exception) {
            onInitialLoadFinished()
            if (params.key == 1) {
                onError(e)
            }
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, T>): Int? {
        return state.anchorPosition?.let { position ->
            state.closestPageToPosition(position)?.nextKey?.minus(1)
        }
    }
}


fun <T : Any> createPagedResultPager(
    pageSize: Int = 20,
    loadData: suspend (page: Int) -> PagedResult<T>,
    onInitialLoadFinished: suspend () -> Unit,
    onError: (Throwable) -> Unit
): Flow<PagingData<T>> {
    return Pager(
        config = PagingConfig(
            pageSize = pageSize,
            initialLoadSize = pageSize,
            prefetchDistance = 4
        ),
        pagingSourceFactory = {
            PagedResultPagingSource(loadData, onInitialLoadFinished, onError)
        }
    ).flow
}


