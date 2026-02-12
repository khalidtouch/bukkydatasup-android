package com.kxtdev.bukkydatasup.common.enums

enum class DataPlanType(val title: String, val label: String, val order: Int) {
    SME("SME", "SME", 1),
    CG("CORPORATE GIFTING", "CG", 3),
    GIFTING("GIFTING", "GIFTING", 5),
    COUPON("DATA COUPONS", "COUPON", 8),
    SME2("SME2", "SME2", 2),
    DATA_SHARE("DATA SHARE", "DATA SHARE", 7),
    AWOOF("AWOOF", "AWOOF", 6),
    CG2("CG2", "CG2", 4);

    companion object {
        fun getByTitle(title: String): DataPlanType {
            return DataPlanType.entries.find { title == it.title }
                ?: CG
        }
        fun getByLabel(label: String): DataPlanType {
            return DataPlanType.entries.find { label == it.label }
                ?: CG
        }

        fun List<DataPlanType>.expressAsString(): List<String> {
            return this.map { it.label }
        }
    }
}