package com.techdoctorbd.taskmanagermongodb.adapters

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.text.HtmlCompat
import com.techdoctorbd.taskmanagermongodb.R
import com.techdoctorbd.taskmanagermongodb.data.models.Task
import com.techdoctorbd.taskmanagermongodb.ui.tasks.AddTaskActivity
import com.techdoctorbd.taskmanagermongodb.utils.TaskItemClickListener

class TaskListAdapter(
    private val taskList: List<Task>,
    private val activity: Activity,
    private val taskItemClickListener: TaskItemClickListener
) :
    BaseAdapter() {

    override fun getCount(): Int {
        return taskList.size
    }

    override fun getItem(position: Int): Any {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, view: View?, parent: ViewGroup?): View {
        var holder: ListTaskViewHolder? = null
        var convertView = view
        if (convertView == null) {
            holder = ListTaskViewHolder()
            convertView =
                LayoutInflater.from(activity).inflate(R.layout.layout_task_item, parent, false)
            holder.taskName = convertView!!.findViewById(R.id.task_name)
            holder.checkBox = convertView.findViewById(R.id.checkBtn)
            holder.taskItem = convertView.findViewById(R.id.task_item)
            convertView.tag = holder
        } else {
            holder = convertView.tag as ListTaskViewHolder
        }

        val task: Task = taskList[position]

        holder.checkBox?.isChecked = task.completed
        if (task.completed) {
            holder.taskName?.text = HtmlCompat.fromHtml(
                "<strike>" + task.description + "</strike>",
                HtmlCompat.FROM_HTML_MODE_LEGACY
            )
        } else {
            holder.taskName?.text = task.description
        }

        holder.checkBox?.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                holder.taskName?.text = HtmlCompat.fromHtml(
                    "<strike>" + task.description + "</strike>",
                    HtmlCompat.FROM_HTML_MODE_LEGACY
                )
            } else {
                holder.taskName?.text = task.description
            }
        }

        holder.checkBox?.setOnClickListener {
            taskItemClickListener.onClick(taskList[position], !taskList[position].completed)
        }

        holder.taskItem?.setOnClickListener {
            val intent = Intent(activity, AddTaskActivity::class.java)
            intent.putExtra("isModify", true)
            intent.putExtra("task", task)
            activity.startActivity(intent)
        }

        return convertView
    }

    internal class ListTaskViewHolder {
        var taskName: TextView? = null
        var checkBox: CheckBox? = null
        var taskItem: LinearLayout? = null
    }
}