package com.example.smsclient.data

import kotlinx.serialization.Serializable

@Serializable
data class SmsModel(
    val phoneNumber: String = "",
    val message: String = ""
)
