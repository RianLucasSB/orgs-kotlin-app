package com.boas.rian.myapplication.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.boas.rian.myapplication.database.converter.Converters
import com.boas.rian.myapplication.database.dao.ProdutoDao
import com.boas.rian.myapplication.model.Produto

@Database(entities = [Produto::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun produtoDao(): ProdutoDao

    companion object {
        fun instancia(context: Context): AppDatabase{
            return Room.databaseBuilder(context, AppDatabase::class.java, "orgs.db")
                .allowMainThreadQueries()
                .build()
        }
    }
}