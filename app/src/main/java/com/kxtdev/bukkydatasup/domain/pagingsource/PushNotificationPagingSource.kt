package com.kxtdev.bukkydatasup.domain.pagingsource

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import coil.network.HttpException
import com.kxtdev.bukkydatasup.common.utils.Settings
import com.kxtdev.bukkydatasup.domain.datasource.NetworkDataSource
import com.kxtdev.bukkydatasup.network.models.NetworkPushNotificationResponse
import com.kxtdev.bukkydatasup.network.utils.NetworkResult
import java.io.IOException
import javax.inject.Inject


class PushNotificationPagingSource @Inject constructor(
    private val datasource: NetworkDataSource,
): PagingSource<Int, NetworkPushNotificationResponse>() {
    override fun getRefreshKey(state: PagingState<Int, NetworkPushNotificationResponse>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, NetworkPushNotificationResponse> {
        return try {
            val currentPage = params.key ?: 1
            when(val items = datasource.getPushNotifications(currentPage)) {
                is NetworkResult.Success -> {
                    LoadResult.Page(
                        data = items.data?.results ?: listOf(),
                        prevKey = if (currentPage == 1) null else currentPage - 1,
                        nextKey = if (
                            items.data?.results?.isEmpty() == true ||
                            ((currentPage * Settings.PAGING_SIZE) >= items.data?.results?.size!!)
                        ) null else currentPage + 1

                    )
                }
                is NetworkResult.Error -> {
                    Log.e("Load", "load: PushNotificationPagingSource.error => ${items.message} ", )
                    LoadResult.Error(Throwable("An error occurred!"))
                }
                else ->  LoadResult.Error(Throwable("No result found!"))
            }
        } catch (e: IOException) {
            return LoadResult.Error(e)
        } catch (e: HttpException) {
            return LoadResult.Error(e)
        }
    }
}