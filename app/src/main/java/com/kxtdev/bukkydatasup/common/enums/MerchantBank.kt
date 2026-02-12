package com.kxtdev.bukkydatasup.common.enums

import com.kxtdev.bukkydatasup.ui.design.PoshImage

enum class MerchantBank(val code: String, val title: String, val image: Int) {
    ACCESS("044", "Access Bank", PoshImage.BankAccess),
    CORONATION("559", "Coronation Bank", PoshImage.BankCoronation),
    DIAMOND("063", "Diamond Bank", PoshImage.BankDiamond),
    ECOBANK("050", "Ecobank", PoshImage.BankEcobank),
    FIDELITY("070", "Fidelity Bank", PoshImage.BankFidelity),
    FIRST("011", "First Bank", PoshImage.BankFirst),
    FCMB("214", "FCMB", PoshImage.BankFCMB),
    GT("058", "GTBank", PoshImage.BankGT),
    HERITAGE("030", "Heritage Bank", PoshImage.BankHeritage),
    JAIZ("301", "Jaiz Bank", PoshImage.BankJaiz),
    KEYSTONE("082", "Keystone Bank", PoshImage.BankKeystone),
    POLARIS("076", "Polaris Bank", PoshImage.BankPolaris),
    PROVIDUS("101", "Providus Bank", PoshImage.BankProvidus),
    STANBIC("221", "Stanbic IBTC Bank", PoshImage.BankStanbic),
    STANDARD_CHARTERED("068", "Standard Chartered Bank", PoshImage.BankStandardChartered),
    STERLING("232", "Sterling Bank", PoshImage.BankSterling),
    SUN_TRUST("100", "Suntrust Bank", PoshImage.BankSunTrust),
    UNION("032", "Union Bank", PoshImage.BankUnion),
    UBA("033", "UBA Bank", PoshImage.BankUba),
    UNITY("215", "Unity Bank", PoshImage.BankUnity),
    WEMA("035", "Wema Bank", PoshImage.BankWema),
    ZENITH("057", "Zenith Bank", PoshImage.BankZenith),
    NINE_PAYMENT("120001", "9PSB", PoshImage.Bank9PSB),
    VFD("00", "VFD", PoshImage.BankVFD),
    MONIEPOINT("50515", "MoniePoint", PoshImage.BankMoniePoint),
    PALMPAY("20946", "Palmpay", PoshImage.Palmpay);

    companion object {
        fun getBankFromCode(bankCode: String): MerchantBank? {
            return entries
                .toTypedArray()
                .find { it.code == bankCode }
        }
    }

}