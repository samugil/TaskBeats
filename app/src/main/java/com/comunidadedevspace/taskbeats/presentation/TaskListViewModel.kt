package com.comunidadedevspace.taskbeats.presentation

import android.app.Application
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.comunidadedevspace.taskbeats.TaskBeatsApplication
import com.comunidadedevspace.taskbeats.data.Task
import com.comunidadedevspace.taskbeats.data.TaskDao
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TaskListViewModel(private val taskDao: TaskDao) : ViewModel(){

    val taskListLiveData : LiveData<List<Task>> = taskDao.getAllLiveData()





    fun crud (taskAction: TaskAction) {
        when (taskAction.actionType) {

            ActionType.DELETE.name ->{
                deleteRoom(taskAction.task!!.id)
                    }

                    ActionType . CREATE . name ->{
                insertRoom(taskAction.task!!)

                    }

                    ActionType . UPDATE . name ->{
                updateRoom(taskAction.task!!)

                    }

                ActionType . DELETE_ALL . name ->{
                deleteAll()

                    }

        }
    }

    private fun insertRoom(task: Task) {

        viewModelScope.launch(Dispatchers.IO) {
            taskDao.insert(task)

        }

    }

    private fun updateRoom(task: Task) {

        viewModelScope.launch(Dispatchers.IO) {
            taskDao.update(task)

        }

    }

    private fun deleteRoom(id: Int) {

        viewModelScope.launch(Dispatchers.IO) {
            taskDao.delete(id)

        }

    }


    private fun deleteAll() {
        viewModelScope.launch(Dispatchers.IO) {
            taskDao.deleteAll()

        }

    }


    private fun showMessage(view: View, message: String) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG)
            .setAction("Action", null)
            .show()

    }

    companion object{
        fun create(application :Application): TaskListViewModel{
            val dataBaseInstance = (application as TaskBeatsApplication).getAppDataBase()
            val dao = dataBaseInstance.taskDao()
            return  TaskListViewModel(dao)
        }

    }

}