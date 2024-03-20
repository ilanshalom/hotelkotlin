package com.example.hotelkotlin

import android.content.Context
import android.database.sqlite.SQLiteOpenHelper
import com.example.hotelkotlin.HotelHelper
import android.database.sqlite.SQLiteDatabase

class HotelHelper internal constructor(context: Context?) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(TABLE_CREATE)
    }

    override fun onUpgrade(arg0: SQLiteDatabase, arg1: Int, arg2: Int)
    {
    }

    //veja https://kotlinlang.org/docs/object-declarations.html#companion-objects
    companion object {
        private const val DATABASE_VERSION = 1
        private const val TABELA = "tbl_habitacoes" //nome da tabela
        private const val DATABASE_NAME = "db_hotel" //nome do BD
        private const val TABLE_CREATE = ("create table " + TABELA
                + " (codhabitacao int PRIMARY KEY, qtdepessoas int, precodiaria float);")
    }
}