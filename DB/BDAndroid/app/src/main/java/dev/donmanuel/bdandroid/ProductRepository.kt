package dev.donmanuel.bdandroid

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase

class ProductRepository(context: Context) {

	private val conn: SQLite = SQLite(context, "bd_productos", null, 1)

	// Guardar producto
	fun saveProduct(codigo: String, descripcion: String, precio: String): Boolean {
		val db: SQLiteDatabase = conn.writableDatabase
		val values = ContentValues().apply {
			put("codigo", codigo)
			put("descripcion", descripcion)
			put("precio", precio)
		}

		val result = db.insert("productos", null, values)
		db.close()
		return result != -1L
	}

	// Buscar producto
	fun findProductByCodigo(codigo: String): Cursor? {
		val db: SQLiteDatabase = conn.writableDatabase
		return db.rawQuery(
			"SELECT descripcion, precio FROM productos WHERE codigo = ?",
			arrayOf(codigo)
		)
	}

	// Eliminar producto
	fun deleteProductByCodigo(codigo: String): Int {
		val db: SQLiteDatabase = conn.writableDatabase
		val result = db.delete("productos", "codigo = ?", arrayOf(codigo))
		db.close()

		return result
	}

	// Actualizar producto
	fun updateProduct(codigo: String, descripcion: String, precio: String): Int {
		val db: SQLiteDatabase = conn.writableDatabase
		val values = ContentValues().apply {
			put("descripcion", descripcion)
			put("precio", precio)
		}
		val result = db.update("productos", values, "codigo = ?", arrayOf(codigo))
		db.close()
		return result
	}

	// Listar productos
	fun listAllProducts(): Cursor {
		val db: SQLiteDatabase = conn.writableDatabase
		return db.rawQuery("SELECT * FROM productos", null)
	}

}