package com.kxtdev.bukkydatasup.common.enums

import com.kxtdev.bukkydatasup.ui.design.PoshImage


enum class DiscoProvider(val id: Int, val title: String, val icon: Int) {
    ABA_ELECTRIC(12, "Aba Electric", PoshImage.AbaElectric),
    IKEJA_ELECTRIC(1, "Ikeja Electric", PoshImage.IkejaElectric),
    EKO_ELECTRIC(2, "Eko Electric", PoshImage.EkoElectric),
    ABUJA_ELECTRIC(3, "Abuja Electric", PoshImage.AbujaElectric),
    KANO_ELECTRIC(4, "Kano Electric", PoshImage.KanoElectric),
    ENUGU_ELECTRIC(5, "Enugu Electric", PoshImage.EnuguElectric),
    PORT_HARCOURT_ELECTRIC(6, "Ph Electric", PoshImage.PhElectric),
    IBADAN_ELECTRIC(7, "Ibadan Electric", PoshImage.IbadanElectric),
    KADUNA_ELECTRIC(8, "Kaduna Electric", PoshImage.KadunaElectric),
    JOS_ELECTRIC(9, "Jos Electric", PoshImage.JosElectric),
    BENIN_ELECTRIC(10, "Benin Electric", PoshImage.BeninElectric),
    YOLA_ELECTRIC(11, "Yola Electric", PoshImage.YolaElectric);


    companion object {
        fun getByTitle(title: String): DiscoProvider? {
            return DiscoProvider.entries.find { it.title.lowercase() == title.lowercase() }
        }
        fun asPair(): List<Pair<String, Int>> {
            return entries
                .toTypedArray()
                .mapIndexed { _, disco -> Pair(disco.title, disco.icon) }
        }
        fun getById(id: Int): DiscoProvider? {
            return entries
                .toTypedArray()
                .find { it.id == id }
        }
    }

}