package com.comunidadedevspace.taskbeats

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar


class TaskDetailActivity : AppCompatActivity() {

    private var task: Task? = null
    private lateinit var tvTaskDetail: TextView

    companion object {

        private const val TASK_DETAIL_EXTRA = "task.extra.detail"

        fun start(context: Context, task: Task?): Intent {
            val intent = Intent(context, TaskDetailActivity::class.java).apply {
                putExtra(TASK_DETAIL_EXTRA, task)
            }
            return intent
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_detail)

        // recuperar task
        task = (intent.getSerializableExtra(TASK_DETAIL_EXTRA) as Task?)

        //recuperar campo do xml
        tvTaskDetail = findViewById(R.id.tv_task_detail)

        //setar o novo texto na tela
        tvTaskDetail.text = task?.title ?: "Adicione uma tarefa"

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_task_detail, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_delete -> {

                if (task != null) {
                    task?.let {
                        val intent = Intent().apply {
                            val actionType = ActionType.DELETE
                            val taskAction = TaskAction(task!!, actionType)
                            putExtra(TASK_ACTION_RESULTADO, taskAction)
                        }
                        setResult(Activity.RESULT_OK, intent)
                        finish()
                    }
                } else {
                    showMessage(tvTaskDetail, "Nenhuma tarefa para deletar")
                }

                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showMessage(view: View, message: String) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG)
            .setAction("Action", null)
            .show()

    }
}
