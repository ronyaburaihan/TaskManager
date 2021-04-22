package com.techdoctorbd.taskmanagermongodb.utils

import com.techdoctorbd.taskmanagermongodb.data.models.Task

interface TaskItemClickListener {
    fun onClick(task: Task, isCompleted: Boolean)
}