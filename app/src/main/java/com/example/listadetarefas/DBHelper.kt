package com.example.listadetarefas

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

private val DB_NAME = "lista_db"

class DBHelper (context: Context): SQLiteOpenHelper(context, DB_NAME, null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(ListaItemModel.CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS" + ListaItemModel.LISTA_TABLE_NAME)

        onCreate(db)
    }

    fun insertListaItem(listaItem: String): Long{
        val db = this.writableDatabase

        val values = ContentValues()
        values.put(ListaItemModel.LISTA_TEXT_COLUMN, listaItem)

        val item = db.insert(ListaItemModel.LISTA_TABLE_NAME, null, values)

        db.close()

        return item
    }

}