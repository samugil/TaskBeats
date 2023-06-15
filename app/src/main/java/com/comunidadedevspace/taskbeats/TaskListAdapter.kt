package com.comunidadedevspace.taskbeats

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

class TaskListAdapter(
    private val openTaskDeteailView:(task: Task) -> Unit):
    ListAdapter<Task, TaskListViewHolder>(TaskListAdapter) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskListViewHolder {
    val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false)
        return TaskListViewHolder(view)
    }

    //juntar os itens da lista pela posição na lista
    override fun onBindViewHolder(holder: TaskListViewHolder, position: Int) {
    val task = getItem(position)
        holder.bind(task, openTaskDeteailView)
    }



    companion object :  DiffUtil.ItemCallback<Task>(){
        override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
            return  oldItem.title == newItem.title && oldItem.description == newItem.description
        }

    }

}

class TaskListViewHolder(private val view: View) : RecyclerView.ViewHolder(view){

    private val tvTitle = view.findViewById<TextView>(R.id.tv_task_title)
    private val tvDesc = view.findViewById<TextView>(R.id.tv_task_desc)

    fun bind(task: Task, openTaskDeteailView:(task: Task) -> Unit){
        tvTitle.text = task.title
        tvDesc.text = task.description

        view.setOnClickListener{
            openTaskDeteailView.invoke(task)
        }
    }
   }