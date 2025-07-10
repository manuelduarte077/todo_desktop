package dev.donmanuel.bdandroid

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.TableLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

	private lateinit var txtCodigo: EditText
	private lateinit var txtDescripcion: EditText
	private lateinit var txtPrecio: EditText
	private lateinit var tlProductos: TableLayout
	private lateinit var productRepository: ProductRepository

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		enableEdgeToEdge()
		setContentView(R.layout.activity_main)

		// Inicializar los campos y repositorio
		txtCodigo = findViewById(R.id.txtCodigo)
		txtDescripcion = findViewById(R.id.txtDescripcion)
		txtPrecio = findViewById(R.id.txtPrecio)
		tlProductos = findViewById(R.id.tlProductos)
		productRepository = ProductRepository(this)

		listarProductos()

		ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
			val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
			v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
			insets
		}
	}

	// Método para validar que los campos no estén vacíos
	private fun areFieldsValid(): Boolean {
		return txtCodigo.text.isNotEmpty() && txtDescripcion.text.isNotEmpty() && txtPrecio.text.isNotEmpty()
	}

	// Guardar producto
	fun guardar(view: View) {
		if (areFieldsValid()) {
			val success = productRepository.saveProduct(
				txtCodigo.text.toString(),
				txtDescripcion.text.toString(),
				txtPrecio.text.toString()
			)
			if (success) {
				Toast.makeText(this, "Producto guardado", Toast.LENGTH_SHORT).show()
				limpiarCampos()
				listarProductos()
			} else {
				Toast.makeText(this, "Error al guardar el producto", Toast.LENGTH_SHORT).show()
			}
		} else {
			Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show()
		}
	}

	// Buscar producto por código
	fun buscar(view: View) {
		val codigo = txtCodigo.text.toString()
		if (codigo.isNotEmpty()) {
			val cursor = productRepository.findProductByCodigo(codigo)
			cursor?.let {
				if (it.moveToFirst()) {
					txtDescripcion.setText(it.getString(0))
					txtPrecio.setText(it.getString(1))
				} else {
					Toast.makeText(this, "Producto no encontrado", Toast.LENGTH_SHORT).show()
				}
				it.close()
			}
		} else {
			Toast.makeText(this, "El campo código es obligatorio", Toast.LENGTH_SHORT).show()
		}
	}

	// Eliminar producto
	fun eliminar(view: View) {
		val codigo = txtCodigo.text.toString()
		if (codigo.isNotEmpty()) {
			val result = productRepository.deleteProductByCodigo(codigo)
			if (result > 0) {
				Toast.makeText(this, "Producto eliminado", Toast.LENGTH_SHORT).show()
				limpiarCampos()
				listarProductos()
			} else {
				Toast.makeText(this, "Producto no encontrado", Toast.LENGTH_SHORT).show()
			}
		} else {
			Toast.makeText(this, "El campo código es obligatorio", Toast.LENGTH_SHORT).show()
		}
	}

	// Actualizar producto
	fun actualizar(view: View) {
		if (areFieldsValid()) {
			val result = productRepository.updateProduct(
				txtCodigo.text.toString(),
				txtDescripcion.text.toString(),
				txtPrecio.text.toString()
			)
			if (result > 0) {
				Toast.makeText(this, "Producto actualizado", Toast.LENGTH_SHORT).show()
				listarProductos()
			} else {
				Toast.makeText(this, "Producto no encontrado", Toast.LENGTH_SHORT).show()
			}
		} else {
			Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show()
		}
	}

	// Listar todos los productos
	private fun listarProductos() {
		val cursor = productRepository.listAllProducts()
		tlProductos.removeAllViews()

		if (cursor.moveToFirst()) {
			do {
				val registro = LayoutInflater.from(this).inflate(R.layout.item_producto, null, false)
				val tvCodigo = registro.findViewById<TextView>(R.id.tvCodigo)
				val tvDescripcion = registro.findViewById<TextView>(R.id.tvDescripcion)
				val tvPrecio = registro.findViewById<TextView>(R.id.tvPrecio)

				tvCodigo.text = cursor.getString(0)
				tvDescripcion.text = cursor.getString(1)
				tvPrecio.text = cursor.getString(2)

				tlProductos.addView(registro)
			} while (cursor.moveToNext())
		} else {
			Toast.makeText(this, "No hay productos disponibles", Toast.LENGTH_SHORT).show()
		}
		cursor.close()
	}

	// Método para limpiar los campos
	private fun limpiarCampos() {
		txtCodigo.setText("")
		txtDescripcion.setText("")
		txtPrecio.setText("")
	}
}