package com.boas.rian.myapplication.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.boas.rian.myapplication.database.converter.Converters
import com.boas.rian.myapplication.database.dao.ProdutoDao
import com.boas.rian.myapplication.database.dao.UsuarioDao
import com.boas.rian.myapplication.model.Produto
import com.boas.rian.myapplication.model.Usuario

@Database(entities = [Produto::class, Usuario::class], version = 3, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun produtoDao(): ProdutoDao
    abstract fun usuarioDao(): UsuarioDao

    companion object {
        @Volatile
        private var db: AppDatabase? = null

        fun instancia(context: Context): AppDatabase {
            return db ?: Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "orgs.db"
            )
                .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
                .build()
                .also { db = it }
        }
    }
}