package com.kxtdev.bukkydatasup.common.enums

import com.kxtdev.bukkydatasup.common.models.DisabledNetwork
import com.kxtdev.bukkydatasup.common.models.getNetworks
import com.kxtdev.bukkydatasup.ui.design.PoshImage


enum class Network(val id: Int, val title: String, val capitalizedName: String, val icon: Int) {
    MTN(1, "MTN", "MTN", PoshImage.mtn),
    GLO(2, "GLO", "GLO", PoshImage.glo),
    NINE_MOBILE(3, "9Mobile", "9MOBILE", PoshImage.nineMobile),
    AIRTEL(4, "Airtel", "AIRTEL", PoshImage.airtel);

    companion object {
        fun asPair(product: Product, disabledNetworks: List<DisabledNetwork>): List<Pair<String, Int>> {
            return getActiveNetworks(product, disabledNetworks)
                .toTypedArray()
                .mapIndexed { _, network -> Pair(network.title, network.icon) }
        }

        fun getByName(name: String): Network? {
            return entries
                .toTypedArray()
                .find { it.title.lowercase() == name.lowercase() }
        }

        fun getById(networkId: Int): Network? {
            return entries
                .toTypedArray()
                .find { it.id == networkId }
        }

        fun checkPhone(phone: String): Network? {
            val library = hashMapOf<Network, List<String>>(
                MTN to listOf(
                    "07025",
                    "07026",
                    "0703",
                    "0704",
                    "0706",
                    "0803",
                    "0806",
                    "0810",
                    "0813",
                    "0814",
                    "0816",
                    "0903",
                    "0913",
                    "0906",
                    "0916"
                ),
                GLO to listOf("0705", "0805", "0811", "0807", "0815", "0905", "0915"),
                AIRTEL to listOf(
                    "0708",
                    "0802",
                    "0808",
                    "0812",
                    "0907",
                    "0701",
                    "0901",
                    "0902",
                    "0917",
                    "0904",
                    "0912",
                    "0911"
                ),
                NINE_MOBILE to listOf("0809", "0817", "0818", "0909", "0908"),
            )

            var sample = phone
            if(sample.startsWith("+234")) sample = sample.replace("+234", "0")
            if(sample.startsWith("234")) sample = sample.replace("234", "0")

            library[MTN]?.forEach { prefix ->
                if(sample.startsWith(prefix)) return MTN
            }

            library[GLO]?.forEach { prefix ->
                if(sample.startsWith(prefix)) return GLO
            }

            library[AIRTEL]?.forEach { prefix ->
                if(sample.startsWith(prefix)) return AIRTEL
            }

            library[NINE_MOBILE]?.forEach { prefix ->
                if(sample.startsWith(prefix)) return NINE_MOBILE
            }

            return null
        }

        private fun getActiveNetworks(product: Product, disabledNetworks: List<DisabledNetwork>): List<Network> {
            return entries.filterNot { network -> network in disabledNetworks.getNetworks(product) }
        }
    }
}