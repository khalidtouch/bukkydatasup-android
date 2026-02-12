package com.kxtdev.bukkydatasup.common.enums

sealed interface BiometricsResult {
    data object HardwareUnavailable : BiometricsResult
    data object FeatureUnavailable : BiometricsResult
    data class AuthenticationError(val error: String) : BiometricsResult
    data object AuthenticationFailed : BiometricsResult
    data object AuthenticationSuccess : BiometricsResult
    data object AuthenticationNotSet : BiometricsResult
}