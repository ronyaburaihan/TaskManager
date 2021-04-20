package com.techdoctorbd.taskmanagermongodb.ui.tasks

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.DatePicker
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.techdoctorbd.taskmanagermongodb.R
import com.techdoctorbd.taskmanagermongodb.data.models.Task
import com.techdoctorbd.taskmanagermongodb.databinding.ActivityAddTaskBinding
import com.techdoctorbd.taskmanagermongodb.utils.CustomProgressDialog
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*


@AndroidEntryPoint
class AddTaskActivity : AppCompatActivity() {

    private val taskViewModel: TaskViewModel by viewModels()
    private lateinit var binding: ActivityAddTaskBinding
    private val calendar = Calendar.getInstance()
    private lateinit var progressDialog: CustomProgressDialog
    private val dateFormat = SimpleDateFormat("E, dd MMMM yyyy", Locale.getDefault())
    private var isModify: Boolean? = false
    private lateinit var task: Task

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //init
        progressDialog = CustomProgressDialog(this)


        if (intent.hasExtra("isModify")) {
            isModify = intent.getBooleanExtra("isModify", false)
            task = intent.getSerializableExtra("task") as Task
            initModify()
        }


        binding.dateText.text = dateFormat.format(calendar.time)

        binding.pickDate.setOnClickListener {
            chooseDate()
        }


        binding.saveBtn.setOnClickListener {
            val description = binding.editText.text.toString().trim()
            if (TextUtils.isEmpty(description)) {
                Toast.makeText(this, "Task description can't be empty", Toast.LENGTH_SHORT).show()
            } else {
                progressDialog.show("Please wait, Adding new task for you")

                //adding new task
                taskViewModel.addTask(
                    Task(
                        description = description,
                        taskTime = calendar.timeInMillis.toString()
                    )
                )
            }
        }

        taskViewModel.taskResponse.observe(this, {
            //hide progress dialog
            progressDialog.dismiss()

            if (it.message.isNullOrEmpty()) {
                //task added successfully
                Toast.makeText(this, "Task added successfully", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                //failed to add new task
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun initModify() {
        binding.toolbarTitle.text = resources.getString(R.string.modify_task)
        binding.saveBtn.text = resources.getString(R.string.update)
        binding.deleteTask.visibility = View.VISIBLE

        binding.editText.setText(task.description)
    }

    private fun chooseDate() {
        val dialogView = View.inflate(this, R.layout.layout_date_picker, null)
        val datePicker = dialogView.findViewById<DatePicker>(R.id.date_picker)
        datePicker.updateDate(
            calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(
                Calendar.DAY_OF_MONTH
            )
        )
        val builder = AlertDialog.Builder(this)
        builder.setView(dialogView)
        builder.setTitle("Choose Date")
        builder.setNegativeButton("Cancel", null)
        builder.setPositiveButton("Set") { _, _ ->
            calendar.set(datePicker.year, datePicker.month, datePicker.dayOfMonth)
            binding.dateText.text = dateFormat.format(calendar.time)
        }
        builder.show()
    }
}