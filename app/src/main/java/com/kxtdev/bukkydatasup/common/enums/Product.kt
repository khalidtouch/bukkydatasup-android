package com.kxtdev.bukkydatasup.common.enums

import com.kxtdev.bukkydatasup.ui.design.PoshIcon

enum class Product(val title: String, val icon: Int, val secondaryIcon: Int, val description: String) {
    WALLET_HISTORY("Wallet History", PoshIcon.Time, PoshIcon.Time, "Wallet Summary"),
    AIRTIME("Airtime", PoshIcon.Phone, PoshIcon.Phone, "Get cheap airtime.json deals"),
    DATA("Data", PoshIcon.Network, PoshIcon.Network, "Get cheap and reliable data services"),
    BULK_SMS("Bulk SMS", PoshIcon.Message, PoshIcon.Message, "Send messages in bulk"),
    RESULT_CHECKER("Exam Pins", PoshIcon.Student, PoshIcon.Student, "Check your exam results easily"),
    CABLE("Cable TV", PoshIcon.Tv, PoshIcon.Tv, "Best way to recharge your TV"),
    ELECTRICITY("Electricity", PoshIcon.Flash, PoshIcon.Flash, "Get more energy for less"),
    REFER_FRIEND("Refer", PoshIcon.Refer, PoshIcon.Refer, "Share love by referring a friend"),
    TRANSFER("Transfer", PoshIcon.Transfer, PoshIcon.Transfer, "Quick way to transfer funds to others"),
    PRINT_CARD("Print Card", PoshIcon.Printer, PoshIcon.Printer, "Generate and customize recharge cards"),
    WHATSAPP_GROUP("Join Group",  PoshIcon.WhatsApp, PoshIcon.WhatsApp,"Join us on WhatsApp"),
    AIRTIME_SWAP("Airtime Swap",  PoshIcon.Swap, PoshIcon.Swap,"Swap your airtime.json for cash");


    companion object {
        fun getRecommendations(currentProduct: Product): List<Product> {
            return Product.entries
                .filter { it.ordinal <= 5 &&
                        it != currentProduct &&
                        it != WALLET_HISTORY
                }
                .take(3)
        }

        private fun getHistoryItems(): List<Product> {
            return entries
                .toTypedArray()
                .filter {
                    it != WHATSAPP_GROUP &&
                            it != TRANSFER &&
                            it != REFER_FRIEND &&
                            it != AIRTIME_SWAP
                }
        }

        fun getHistoryItemsAsString(): List<String> {
            return getHistoryItems()
                .map { it.title }
        }

        fun getHomeServices(): List<Product> {
            return entries
                .toTypedArray()
                .filter {
                    it == AIRTIME ||
                            it == DATA ||
                            it == CABLE ||
                            it == ELECTRICITY ||
                            it == TRANSFER ||
                            it == WHATSAPP_GROUP
                }
        }

        fun getByTitle(title: String): Product {
            return entries
                .toTypedArray()
                .find { it.title == title } ?: WALLET_HISTORY
        }

        fun getMainServices(): List<Product> {
            return entries
                .toTypedArray()
                .filterNot {
                    it == WALLET_HISTORY ||
                            it == REFER_FRIEND ||
                            it == WHATSAPP_GROUP
                }
        }

    }
}