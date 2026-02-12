package com.kxtdev.bukkydatasup.common.models

import android.content.Context
import com.kxtdev.bukkydatasup.R
import com.kxtdev.bukkydatasup.common.enums.AirtimeType
import com.kxtdev.bukkydatasup.common.enums.CableTV
import com.kxtdev.bukkydatasup.common.enums.DataPlanType
import com.kxtdev.bukkydatasup.common.enums.DiscoProvider
import com.kxtdev.bukkydatasup.common.enums.ExamType
import com.kxtdev.bukkydatasup.common.enums.MeterType
import com.kxtdev.bukkydatasup.common.enums.Network
import com.kxtdev.bukkydatasup.common.enums.Product
import com.kxtdev.bukkydatasup.common.utils.asMoney


data class TransactionRequest(
    val recipientPhone: String? = null,
    val networkName: String? = null,
    val networkId: Int? = null,
    val amount: Double? = null,
    val percentageDiscount: Double? = null,
    val airtimeType: AirtimeType? = null,
    val dataPlanType: DataPlanType? = null,
    val meterType: MeterType? = null,
    val planId: Int? = null,
    val planSize: String? = null,
    val planValidity: String? = null,
    val cableName: String? = null,
    val cableId: Int? = null,
    val discoName: String? = null,
    val discoId: Int? = null,
    val deviceNumber: String? = null,
    val customerName: String? = null,
    val quantity: Int? = null,
    val examName: String? = null,
    val networkAmountId: Int? = null,
    val nameOnCard: String? = null,
    val message: String? = null,
    val isDND: Boolean? = null,
    val product: Product? = null,
    val address: String? = null,
    val pinResponse: PinResponse? = null,
    val validityName: String? = null,
    val location: Int? = null,
    val locationName: String? = null,
) {
    val airtimeTopUpAndDataAmountToPay: Double
        get() {
            if (amount == null) return 0.0
            if (percentageDiscount == null) return amount
            return amount - (amount * percentageDiscount / 100)
        }

    fun itemsWithIcon(
        disabledNetworks: List<DisabledNetwork>,
    ): List<Pair<String, Int>> {
        return when(product) {
            Product.AIRTIME, Product.DATA, Product.AIRTIME_SWAP -> Network.asPair(product,disabledNetworks)
            Product.CABLE -> CableTV.asPair()
            Product.ELECTRICITY -> DiscoProvider.asPair()
            Product.RESULT_CHECKER -> ExamType.asPair()
            Product.PRINT_CARD -> Network.asPair(product,disabledNetworks)
            else -> listOf()
        }
    }

    val selectedItemName: String? get() {
        return when(product) {
            Product.AIRTIME, Product.DATA, Product.AIRTIME_SWAP -> networkName
            Product.CABLE -> cableName
            Product.ELECTRICITY -> discoName
            Product.RESULT_CHECKER -> examName
            Product.PRINT_CARD -> networkName
            else -> null
        }
    }

    val dialogTitle: String get() {
        return when(product) {
            Product.AIRTIME,
            Product.DATA,
            Product.PRINT_CARD,
            Product.AIRTIME_SWAP -> "Select Network"
            Product.CABLE -> "Select Cable"
            Product.ELECTRICITY -> "Select Disco"
            Product.RESULT_CHECKER -> "Select Exam"
            else -> ""
        }
    }

    val actionLabel: String? get() {
        return when(product) {
            Product.AIRTIME -> "Buy Airtime"
            Product.DATA -> "Buy Data"
            Product.BULK_SMS -> "Send SMS"
            Product.RESULT_CHECKER -> "Buy Pins"
            Product.CABLE -> "Subscribe Cable"
            Product.ELECTRICITY -> "Pay Bill"
            Product.PRINT_CARD -> "Print Card"
            Product.TRANSFER -> "Transfer"
            Product.AIRTIME_SWAP -> "Swap Airtime"
            else -> null
        }
    }

    val providerCaption: String get() {
        return when(product) {
            Product.RESULT_CHECKER -> "Select Exam Name"
            else -> ""
        }
    }

    val selectedProviderName: String? get() {
        return when(product) {
            Product.RESULT_CHECKER -> examName
            Product.CABLE -> cableName
            Product.ELECTRICITY -> discoName
            Product.PRINT_CARD -> networkName
            else -> null
        }
    }

    val selectedProviderIcon: Int? get() {
        return when(product) {
            Product.CABLE -> {
                val provider = CableTV.getByTitle(selectedProviderName.orEmpty()) ?: CableTV.GOTV
                provider.icon
            }
            Product.ELECTRICITY -> {
                val provider = DiscoProvider.getByTitle(selectedProviderName.orEmpty()) ?: DiscoProvider.IKEJA_ELECTRIC
                provider.icon
            }
            Product.RESULT_CHECKER -> {
                val provider = ExamType.getByTitle(selectedProviderName.orEmpty()) ?: ExamType.NECO
                provider.icon
            }
            Product.PRINT_CARD -> {
                val provider = Network.getByName(selectedProviderName.orEmpty()) ?: Network.MTN
                provider.icon
            }
            else -> null
        }
    }

    val enterAmountPlaceholderText: String? get() {
        return when(product) {
            Product.AIRTIME, Product.AIRTIME_SWAP -> "Amount"
            Product.RESULT_CHECKER,
            Product.PRINT_CARD -> "Quantity"
            else -> null
        }
    }

    fun isValid(): Boolean {
        return when(product) {
            Product.AIRTIME -> {
                !recipientPhone.isNullOrBlank() &&
                        !networkName.isNullOrBlank() &&
                        networkId != null &&
                        amount != null &&
                        airtimeType != null
            }
            Product.DATA -> {
                !recipientPhone.isNullOrBlank() &&
                        !networkName.isNullOrBlank() &&
                        networkId != null &&
                        planId != null &&
                        !planSize.isNullOrBlank() &&
                        dataPlanType != null
            }
            Product.CABLE -> {
                planId != null &&
                        !planSize.isNullOrBlank() &&
                        !cableName.isNullOrBlank() &&
                        cableId != null &&
                        !deviceNumber.isNullOrBlank()
            }
            Product.ELECTRICITY -> {
                discoId != null &&
                        !discoName.isNullOrBlank() &&
                        amount != null &&
                        !deviceNumber.isNullOrBlank() &&
                        meterType != null &&
                        !recipientPhone.isNullOrBlank() &&
                        !customerName.isNullOrBlank() &&
                        !address.isNullOrBlank()
            }
            Product.RESULT_CHECKER -> {
                !examName.isNullOrBlank() &&
                        quantity != null
            }
            Product.PRINT_CARD -> {
                !networkName.isNullOrBlank() &&
                        networkId != null &&
                        quantity != null &&
                        !nameOnCard.isNullOrBlank()
            }
            Product.TRANSFER -> {
                amount != null &&
                        !customerName.isNullOrBlank()
            }
            Product.BULK_SMS -> {
                !recipientPhone.isNullOrBlank() &&
                        !customerName.isNullOrBlank() &&
                        !message.isNullOrBlank()
            }
            Product.AIRTIME_SWAP -> {
                !recipientPhone.isNullOrBlank() &&
                        !networkName.isNullOrBlank() &&
                        amount != null
            }

            else -> false
        }
    }

    fun getSelectedNetwork(): Network? {
        return when {
            !networkName.isNullOrBlank() -> Network.getByName(networkName)
            networkId != null -> Network.getById(networkId)
            else -> Network.MTN
        }
    }

    val selectedAirtimeType: AirtimeType get() {
        return airtimeType ?: AirtimeType.VTU
    }

    val selectedDataPlanType: DataPlanType get() {
        return dataPlanType ?: DataPlanType.CG
    }

    val selectedMeterType: MeterType get() {
        return meterType ?: MeterType.PREPAID
    }

    fun getAirtimeTotalAmount(topUpPercentage: Double): Double {
        return (amount?.times(  topUpPercentage / 100.0)) ?: 0.0
    }

    fun getResultCheckerTotalAmount(examAmountPerUnit: Double): Double {
        return examAmountPerUnit * (quantity ?: 0)
    }

    fun getCheckoutScreen(
        context: Context,
        topUpPercentage: Double,
        airtimeSwapPercentage: Double,
    ): HashMap<String, String> {
        val params = hashMapOf<String, String>()
        val topUpTotalAmount = (amount?.times(topUpPercentage / 100)) ?: 0.0

        when(product) {
            Product.AIRTIME -> {
                params["Phone Number"] = recipientPhone.orEmpty()
                params["Network"] = networkName.orEmpty()
                params["Airtime Amount"] = context.getString(R.string.money, amount.toString().asMoney())
                params["Amount To Pay"] = context.getString(R.string.money, topUpTotalAmount.toString().asMoney())
                params["TopUp Discount"] = context.getString(R.string.percentage, (100.0 - topUpPercentage).toString().asMoney())
            }
            Product.DATA -> {
                params["Phone Number"] = recipientPhone.orEmpty()
                params["Network"] = networkName.orEmpty()
                params["Amount"] = context.getString(R.string.money, amount.toString().asMoney())
                params["Data Plan"] = planSize.orEmpty()
                params["Validity"] = planValidity.orEmpty()
            }
            Product.RESULT_CHECKER -> {
                params["Exam Name"] = examName.orEmpty()
                params["Quantity"] = (quantity ?: 0).toString()
                params["Amount"] = context.getString(R.string.money, amount.toString().asMoney())
            }
            Product.CABLE -> {
                params["Cable Name"] = cableName.orEmpty()
                params["Cable Number"] = deviceNumber.orEmpty()
                params["Plan"] = planSize.orEmpty()
                params["Amount"] = context.getString(R.string.money, amount.toString().asMoney())
            }
            Product.ELECTRICITY -> {
                params["Disco Provider"] = discoName.orEmpty()
                params["Meter Number"] = deviceNumber.orEmpty()
                params["Customer Name"] = customerName.orEmpty()
                params["Customer Address"] = address.orEmpty()
                params["Meter Type"] = meterType?.title.orEmpty()
                params["Amount"] = context.getString(R.string.money, amount.toString().asMoney())
            }
            Product.PRINT_CARD -> {
                params["Network Provider"] = networkName.orEmpty()
                params["Card Amount"] = context.getString(R.string.money, pinResponse?.amountToPay.toString().asMoney())
                params["Name On Card"] = nameOnCard.orEmpty()
                params["Card Quantity"] = quantity.toString()
                params["Total Amount"] = context.getString(
                    R.string.money,
                    (quantity?.times(pinResponse?.amountToPay ?: 0.0) ?: 0.0).toString().asMoney()
                )
            }
            Product.TRANSFER -> {
                params["Recipient user"] = customerName.orEmpty()
                params["Amount To Send"] = context.getString(R.string.money, amount.toString().asMoney())
            }
            Product.AIRTIME_SWAP -> {
                params["Phone Number"] = recipientPhone.orEmpty()
                params["Airtime Amount"] = context.getString(R.string.money, amount.toString().asMoney())
                params["Amount In Cash"] = context.getString(R.string.money, (amount?.times(airtimeSwapPercentage / 100.0)).toString().asMoney())
            }
            Product.BULK_SMS -> {
                params["Recipient Phone(s)"] = recipientPhone.orEmpty()
                params["Sender"] = customerName.orEmpty()
                params["Message Body"] = message.orEmpty()
            }

            else -> Unit
        }

        return params
    }

}

