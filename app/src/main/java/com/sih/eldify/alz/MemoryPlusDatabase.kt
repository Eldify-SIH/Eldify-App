package com.sih.eldify.alz

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import com.sih.eldify.alz.contacts.Contacts
import com.sih.eldify.alz.contacts.ContactsDAO
import com.sih.eldify.alz.notes.NoteDAO
import com.sih.eldify.alz.notes.Note

@Database(entities = [Note::class, Contacts::class], version = 1, exportSchema = false)
abstract class MemoryPlusDatabase : RoomDatabase() {
    abstract val noteDAO: NoteDAO

    abstract fun contactsDAO(): ContactsDAO

    companion object {
        private const val DB_NAME = "MemoryPlusDatabase.db"

        @Volatile
        private var INSTANCE: MemoryPlusDatabase? = null

        @Synchronized
        fun getDatabase(context: Context): MemoryPlusDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                        context.applicationContext,
                        MemoryPlusDatabase::class.java,
                        DB_NAME
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}