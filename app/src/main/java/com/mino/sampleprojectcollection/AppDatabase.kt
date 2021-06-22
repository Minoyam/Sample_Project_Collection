package com.mino.sampleprojectcollection

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mino.sampleprojectcollection.dao.HistoryDao
import com.mino.sampleprojectcollection.model.History

@Database(entities = [History::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun historyDao(): HistoryDao
}