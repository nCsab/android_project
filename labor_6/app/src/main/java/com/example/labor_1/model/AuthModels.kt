package com.example.labor_1.model

data class User(
    val id: String,
    val email: String,
    val name: String
)
data class Tokens(
    val accessToken: String,
    val refreshToken: String
)
data class AuthRequest(
    val email: String,
    val password: String,
    val name: String? = null
)
data class AuthResponse(
    val tokens: Tokens,
    val user: User
)
