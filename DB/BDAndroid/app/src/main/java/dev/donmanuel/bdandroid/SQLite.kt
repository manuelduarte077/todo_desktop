package dev.donmanuel.bdandroid

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class SQLite(
	context: Context?, name: String?, factory: SQLiteDatabase.CursorFactory?, version: Int
) : SQLiteOpenHelper(context, name, factory, version) {

	override fun onCreate(db: SQLiteDatabase?) {
		db?.execSQL("CREATE TABLE productos (codigo INTEGER PRIMARY KEY AUTOINCREMENT, descripcion TEXT, precio REAL)")
	}

	override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
		db?.execSQL("DROP TABLE IF EXISTS productos")
		onCreate(db)
	}

}