package com.techdoctorbd.taskmanagermongodb.adapters

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

class CompletedTaskListAdapter(
    private val taskList: List<Task>
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
        val holder: ListTaskViewHolder?
        var convertView = view
        if (convertView == null) {
            holder = ListTaskViewHolder()
            convertView =
                LayoutInflater.from(parent?.context)
                    .inflate(R.layout.layout_task_item, parent, false)
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

        holder.checkBox?.isClickable = false
        holder.taskItem?.isClickable = false

        return convertView
    }

    internal class ListTaskViewHolder {
        var taskName: TextView? = null
        var checkBox: CheckBox? = null
        var taskItem: LinearLayout? = null
    }
}