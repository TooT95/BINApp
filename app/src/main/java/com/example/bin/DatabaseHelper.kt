package com.example.bin

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "bin_database"
        const val DATABASE_VERSION = 1
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.let {
            val createTable = ("CREATE TABLE " + TableBin.name + "("
                    + TableBin.id + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + TableBin.binId + " TEXT,"
                    + TableBin.responseString + " TEXT)")
            it.execSQL(createTable)
        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db?.let {
            db.execSQL("DROP TABLE IF EXISTS ${TableBin.name}")
            onCreate(db)
        }
    }

    fun addBin(binId: String, responseString: String): Long {
        val db = writableDatabase
        val values = ContentValues()
        values.put(TableBin.binId, binId)
        values.put(TableBin.responseString, responseString)
        val id = db.insert(TableBin.name, null, values)
        db.close()
        return id
    }

    fun getAllBin(): List<Map<String, Any>> {
        val result = mutableListOf<Map<String, Any>>()
        val selectQuery = "SELECT  * FROM ${TableBin.name}"

        val db = writableDatabase
        val cursor = db.rawQuery(selectQuery, null)
        if (cursor.moveToFirst()) {
            do {
                val mapOf = mutableMapOf<String, Any>()
                mapOf[TableBin.id] = cursor.getString(0).toLong()
                mapOf[TableBin.binId] = cursor.getString(1)
                mapOf[TableBin.responseString] = cursor.getString(2)
                result.add(mapOf)
            } while (cursor.moveToNext())
        }
        return result
    }

    object TableBin {
        const val name = "bin"
        const val id = "id"
        const val binId = "bin"
        const val responseString = "responseString"
    }

}