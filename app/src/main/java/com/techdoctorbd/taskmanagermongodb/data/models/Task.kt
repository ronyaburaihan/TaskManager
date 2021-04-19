package com.techdoctorbd.taskmanagermongodb.data.models

import java.sql.Timestamp

data class Task(
    private val description: String,
    private val completed: Boolean = false,
    private val timestamp: Timestamp
)
