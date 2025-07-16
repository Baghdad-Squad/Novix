package com.baghdad.viewmodel.base

import android.graphics.pdf.LoadParams
import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.baghdad.domain.model.PagedResult
import kotlinx.coroutines.flow.Flow

class PagedResultPagingSource<T : Any>(
    private val loadData: suspend (page: Int) -> PagedResult<T>
) : PagingSource<Int, T>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> {
        return try {
            val page = params.key ?: 1
            val result = loadData(page)
            Log.e("pagggge", "searching for34343 ${result.data}")
            LoadResult.Page(
                data = result.data,
                prevKey = result.prevKey,
                nextKey = result.nextKey
            )

        } catch (e: Exception) {
            Log.e("pagggge", "exception ${e}", e)
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
    loadData: suspend (page: Int) -> PagedResult<T>
): Flow<PagingData<T>> {
    return Pager(
        config = PagingConfig(pageSize = pageSize),
        pagingSourceFactory = { PagedResultPagingSource(loadData) }
    ).flow
}

