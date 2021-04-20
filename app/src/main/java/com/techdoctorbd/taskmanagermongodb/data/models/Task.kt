package com.techdoctorbd.taskmanagermongodb.data.models

import java.io.Serializable

data class Task(
    val _id: String? = null,
    val description: String,
    val completed: Boolean = false,
    val taskTime: String
) : Serializable
