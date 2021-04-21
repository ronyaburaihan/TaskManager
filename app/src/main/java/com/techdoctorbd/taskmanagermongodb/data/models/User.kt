package com.techdoctorbd.taskmanagermongodb.data.models

data class User(
    val _id: String? = null,
    val name: String? = null,
    val email: String,
    val password: String
)