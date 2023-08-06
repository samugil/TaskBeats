package com.comunidadedevspace.taskbeats

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.comunidadedevspace.taskbeats.data.AppDataBase
import com.comunidadedevspace.taskbeats.data.Task
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import java.io.Serializable


class TaskListActivity : AppCompatActivity() {


    private lateinit var ctnContent: LinearLayout

    // Adapter
    private val adapter: TaskListAdapter = TaskListAdapter(::onListItemClicked)


    private val dataBase by lazy {
        Room.databaseBuilder(
            applicationContext,
            AppDataBase::class.java, "taskbeats-database"
        ).build()
    }

    private val dao by lazy {

        dataBase.taskDao()

    }


    private val startForResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == RESULT_OK) {
            // pegando o resultado
            val data = result.data
            val taskAction = data?.getSerializableExtra(TASK_ACTION_RESULTADO) as TaskAction
            val task: Task = taskAction.task

            when (taskAction.actionType) {
                ActionType.DELETE.name -> {
                    deleteRoom(task.id)
                    showMessage(ctnContent,"Tarefa deletada.")
                }
                ActionType.CREATE.name -> {
                    insertRoom(task)
                    showMessage(ctnContent,"Tafera incluída.")

                }
                ActionType.UPDATE.name -> {
                    updateRoom(task)
                    showMessage(ctnContent,"Tarefa Atualizada.")
                }
            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_list)

        listFromDatabase()
        ctnContent = findViewById(R.id.ctn_content)


        // RecyclerView
        val rvTasks: RecyclerView = findViewById(R.id.rv_task_list)
        rvTasks.adapter = adapter

        val fab = findViewById<FloatingActionButton>(R.id.fab_add)
        fab.setOnClickListener {
            showMessage(it, "Here's a Snackbar")
            openTaskListDetail()

        }
    }

    private fun insertRoom(task: Task) {

        CoroutineScope(IO).launch {
            dao.insert(task)
            listFromDatabase()
        }

    }

    private fun updateRoom(task: Task) {

        CoroutineScope(IO).launch {
            dao.update(task)
            listFromDatabase()
        }

    }

    private fun deleteRoom(id: Int) {

        CoroutineScope(IO).launch {
            dao.delete(id)
            listFromDatabase()
        }

    }

    private fun listFromDatabase() {
        CoroutineScope(IO).launch {
            val myDataBaseLIst: List<Task> = dao.getAll()
            adapter.submitList(myDataBaseLIst)

            if (myDataBaseLIst.isEmpty()) {
                ctnContent.visibility = View.VISIBLE
            }else{
                ctnContent.visibility = View.GONE
            }

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

enum class ActionType {

    DELETE,
    UPDATE,
    CREATE

}

data class TaskAction(
    val task: Task,
    val actionType: String
) : Serializable

const val TASK_ACTION_RESULTADO = "TASK_ACTION_RESULTADO"