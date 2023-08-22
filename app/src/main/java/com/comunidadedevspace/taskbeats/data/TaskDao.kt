package com.comunidadedevspace.taskbeats.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface  TaskDao {

    @Query("SELECT * FROM Task")
    fun getAllLiveData(): LiveData<List<Task>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(task: Task)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(task: Task)

    @Query("Delete from task where id=:id")
    fun delete(id: Int)

    @Query("Delete from task")
    fun deleteAll()
}