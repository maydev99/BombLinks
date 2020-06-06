package com.bombadu.bomblinks.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [LinkData::class], version = 1, exportSchema = false)
abstract class LinkDatabase: RoomDatabase() {

    abstract  fun linkDao(): LinkDao


    private class LinkDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {
        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            INSTANCE?.let { linkDatabase ->
                scope.launch {  }
            }
        }
    }



    companion object {
        @Volatile
        private var INSTANCE: LinkDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): LinkDatabase {
            val tempInstance = INSTANCE
            if(tempInstance != null) {
                return tempInstance
            }

            synchronized(this) {
                val instance = databaseBuilder(
                    context.applicationContext,
                    LinkDatabase::class.java,
                    "record_database"
                ).addCallback(LinkDatabaseCallback(scope)).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}