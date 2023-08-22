package com.comunidadedevspace.taskbeats.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.comunidadedevspace.taskbeats.R
import com.comunidadedevspace.taskbeats.data.Task
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import java.io.Serializable


class TaskListActivity : AppCompatActivity() {


    private lateinit var ctnContent: LinearLayout

    // Adapter
    private val adapter: TaskListAdapter  by lazy{
        TaskListAdapter(::onListItemClicked)
    }

    private val viewModel: TaskListViewModel by lazy {
        TaskListViewModel.create(application)
    }




    private val startForResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == RESULT_OK) {
            // pegando o resultado
            val data = result.data
            val taskAction = data?.getSerializableExtra(TASK_ACTION_RESULTADO) as TaskAction
            viewModel.crud(taskAction)
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_list)
        setSupportActionBar(findViewById(R.id.toolbar))

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

    override fun onStart(){
        super.onStart()
        listFromDatabase()

    }

    private fun deleteAll(){
        val taskAction = TaskAction(null, ActionType.DELETE_ALL.name)
        viewModel.crud(taskAction)
    }


    private fun listFromDatabase() {

            val listObserver = Observer<List<Task>>{ listTasks ->
                   adapter.submitList(listTasks)
            //estado de vazio
            if (listTasks.isEmpty()) {
                ctnContent.visibility = View.VISIBLE
            }else{
                ctnContent.visibility = View.GONE
            }


        }
        viewModel.taskListLiveData.observe(this@TaskListActivity, listObserver)


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


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_task_list, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId){
            R.id.delete_all_task -> {
                deleteAll()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}

enum class ActionType {

    DELETE,
    DELETE_ALL,
    UPDATE,
    CREATE

}

data class TaskAction(
    val task: Task?,
    val actionType: String
) : Serializable

const val TASK_ACTION_RESULTADO = "TASK_ACTION_RESULTADO"