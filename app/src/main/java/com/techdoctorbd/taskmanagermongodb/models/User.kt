package com.techdoctorbd.taskmanagermongodb.models

data class User(
    val name: String,
    val email: String,
    val password: String,
    val age: Int = 0,
)