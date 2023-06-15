package com.comunidadedevspace.taskbeats

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import java.io.Serializable


class TaskListActivity : AppCompatActivity() {

    //Kotlin
    private var taskList = arrayListOf(
        Task(0, "Title 0", "Desc 0"),
        Task(1, "Title 1", "Desc 1"),
        Task(2, "Title 2", "Desc 2"),
        Task(3, "Title 3", "Desc 3")
    )

    private lateinit var ctnContent: LinearLayout

    // Adapter
    private val adapter: TaskListAdapter = TaskListAdapter(::onListItemClicked)


    private val startForResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == RESULT_OK) {
            // pegando o resultado
            val data = result.data
            val taskAction = data?.getSerializableExtra(TASK_ACTION_RESULTADO) as TaskAction
            val task: Task = taskAction.task

            val newList = arrayListOf<Task>().apply {
                addAll(taskList)
            }

            // removendo o item da lista kotlin
            newList.remove(task)
            showMessage(ctnContent, message = "Item Deletado ${task.title}")

            if (newList.size == 0) {
                ctnContent.visibility = View.VISIBLE
            }

            // atualizando o adpater
            adapter.submitList(newList)
            taskList = newList
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_list)

        ctnContent = findViewById(R.id.ctn_content)
        adapter.submitList(taskList)

        // RecyclerView
        val rvTasks: RecyclerView = findViewById(R.id.rv_task_list)
        rvTasks.adapter = adapter

        val fab = findViewById<FloatingActionButton>(R.id.fab_add)
        fab.setOnClickListener {
            showMessage(it, "Here's a Snackbar")
            openTaskListDetail()

        }
    }

    private fun showMessage(view: View, message: String) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG)
            .setAction("Action", null)
            .show()

    }

    private fun onListItemClicked(task: Task) {
        openTaskListDetail(task)

    }

    private fun openTaskListDetail(task: Task? = null) {
        val intent = TaskDetailActivity.start(this, task)
        startForResult.launch(intent)
    }

}

sealed class ActionType : Serializable {

    object DELETE : ActionType()
    object UPDATE : ActionType()
    object CREATE : ActionType()

}

data class TaskAction(
    val task: Task,
    val actionType: ActionType
) : Serializable

const val TASK_ACTION_RESULTADO = "TASK_ACTION_RESULTADO"