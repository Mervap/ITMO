package me.mervap

import kotlinx.serialization.Serializable

@Serializable
data class UserCredentials(var username: String, var password: String)