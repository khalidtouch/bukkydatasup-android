package com.kxtdev.bukkydatasup.domain.mappers.responses

import com.kxtdev.bukkydatasup.common.models.Advertisement
import com.kxtdev.bukkydatasup.network.models.NetworkAdvertisementItem
import com.kxtdev.bukkydatasup.network.utils.NetworkResult

fun NetworkAdvertisementItem.convertToAdvertisementItem(): Advertisement {
    return Advertisement(
        id = id,
        image = image,
        description = description,
    )
}

fun List<NetworkAdvertisementItem>.convertToAdvertisementItems(): List<Advertisement> {
    return map { item -> item.convertToAdvertisementItem() }
}

fun NetworkResult<List<NetworkAdvertisementItem>>.convertToAdvertisement(): List<Advertisement> {
    return when(this) {
        is NetworkResult.Success -> data.convertToAdvertisementItems()
        is NetworkResult.Failed -> data.convertToAdvertisementItems()
        is NetworkResult.Error -> listOf()
    }
}