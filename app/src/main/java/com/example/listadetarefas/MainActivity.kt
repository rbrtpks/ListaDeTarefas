package com.example.listadetarefas

import android.app.AlertDialog
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private var coordinatorLayout: CoordinatorLayout? = null
    private var recyclerView: RecyclerView? = null

    private var db: DBHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        toolbar.setTitleTextColor(Color.WHITE)
        setSupportActionBar(toolbar)

        controle()
    }

    private fun controle() {
        coordinatorLayout = findViewById(R.id.layout_main)
        recyclerView = findViewById(R.id.recycler_main)
        db = DBHelper(this)

        val fab = findViewById<View>(R.id.fab) as FloatingActionButton

        fab.setOnClickListener { showDialog(false, null, -1) }
    }

    private fun showDialog(isUpdate: Boolean, nothing: Nothing?, position: Int) {

        val layoutInflaterAndroid = LayoutInflater.from(applicationContext)
        val view = layoutInflaterAndroid.inflate(R.layout.lista_dialog, null)

        val userInput = AlertDialog.Builder(this@MainActivity)
        userInput.setView(view)

        val input = view.findViewById<EditText>(R.id.dialogText)
        val titulo = view.findViewById<TextView>(R.id.dialogTitle)
        titulo.text = if (!isUpdate) getString(R.string.novo) else getString(R.string.editar)

        userInput
            .setCancelable(false)
            .setPositiveButton(if(isUpdate) getString(R.string.atualizar) else getString(R.string.salvar)) {dialogBox, id ->}
            .setNegativeButton(getString(R.string.cancelar)) {dialogBox, id -> dialogBox.cancel()}

        val alertDialog = userInput.create()
        alertDialog.show()

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(View.OnClickListener {
            if(TextUtils.isEmpty(input.text.toString())) {
                Toast.makeText(this@MainActivity, getString(R.string.toastTarefa), Toast.LENGTH_LONG).show()
                return@OnClickListener
            } else {
                alertDialog.dismiss()
            }

            createListaItem(input.text.toString())
        })
    }

    private fun createListaItem(listaText: String) {
        db!!.insertListaItem(listaText)

    }
}