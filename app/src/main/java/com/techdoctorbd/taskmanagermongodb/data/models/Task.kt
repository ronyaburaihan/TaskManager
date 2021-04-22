package com.techdoctorbd.taskmanagermongodb.data.models

import java.io.Serializable

data class Task(
    var _id: String? = null,
    var description: String,
    var completed: Boolean = false,
    var taskTime: String
) : Serializable
