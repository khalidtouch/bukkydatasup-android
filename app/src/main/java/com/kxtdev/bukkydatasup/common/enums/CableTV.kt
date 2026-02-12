package com.kxtdev.bukkydatasup.common.enums

import com.kxtdev.bukkydatasup.ui.design.PoshImage


enum class CableTV(val id: Int, val title: String, val icon: Int) {
    GOTV(1, "GOTV", PoshImage.Gotv),
    DSTV(2, "DSTV", PoshImage.Dstv),
    STARTIME(3, "STARTIME", PoshImage.Startime);

    companion object {
        fun asPair(): List<Pair<String, Int>> {
            return entries
                .toTypedArray()
                .mapIndexed { _, cable -> Pair(cable.title, cable.icon) }
        }

        fun getById(cableId: Int): CableTV? {
            return entries
                .toTypedArray()
                .find { it.id == cableId }
        }

        fun getByTitle(title: String): CableTV? {
            return entries
                .toTypedArray()
                .find { it.title == title }
        }
    }
}