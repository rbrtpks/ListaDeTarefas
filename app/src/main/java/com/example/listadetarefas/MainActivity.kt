package com.example.listadetarefas

import android.app.AlertDialog
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private var coordinatorLayout: CoordinatorLayout? = null
    private var recyclerView: RecyclerView? = null
    private var itemsList = ArrayList<ListaItemModel>()
    private var mAdapter: ListaItemAdapter? = null

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

        //Exibe os Resultados
        itemsList.addAll(db!!.ItensList)
        mAdapter = ListaItemAdapter(this, itemsList)
        val mLayoutManager = LinearLayoutManager(applicationContext)
        recyclerView!!.layoutManager = mLayoutManager
        recyclerView!!.itemAnimator = DefaultItemAnimator()
        recyclerView!!.adapter = mAdapter

        //Função de Clique
        recyclerView!!.addOnItemTouchListener(ItemLongPressListener(this,
            recyclerView!!, object : ItemLongPressListener.ClickListener {
                override fun onClick(view: View, position: Int) {}

                override fun onLongClick(view: View?, position: Int) {
                    showActionsDialog(position)
                }
            }
        ))
    }

    private fun showActionsDialog(position: Int) {
        val options = arrayOf<CharSequence>(
            getString(R.string.editar),
            getString(R.string.excluir),
            getString(R.string.excluirTudo)
        )
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.tituloOpcao))
        builder.setItems(options) { dialog, itemIndex ->
            when(itemIndex) {
                0 -> showDialog(true, itemsList[position], position)
                1 -> deleteListaItem(position)
                2 -> deleteTodosItens()
                else -> Toast.makeText(applicationContext, getString(R.string.toastErro), Toast.LENGTH_SHORT).show()
            }
        }

        builder.show()
    }

    private fun deleteListaItem(position: Int) {

    }

    private fun deleteTodosItens() {

    }

    private fun showDialog(isUpdate: Boolean, listaItemModel: ListaItemModel?, position: Int) {

        val layoutInflaterAndroid = LayoutInflater.from(applicationContext)
        val view = layoutInflaterAndroid.inflate(R.layout.lista_dialog, null)

        val userInput = AlertDialog.Builder(this@MainActivity)
        userInput.setView(view)

        val input = view.findViewById<EditText>(R.id.dialogText)
        val titulo = view.findViewById<TextView>(R.id.dialogTitle)
        titulo.text = if (!isUpdate) getString(R.string.novo) else getString(R.string.editar)

        userInput
            .setCancelable(false)
            .setPositiveButton(if (isUpdate) getString(R.string.atualizar) else getString(R.string.salvar)) { dialogBox, id -> }
            .setNegativeButton(getString(R.string.cancelar)) { dialogBox, id -> dialogBox.cancel() }

        val alertDialog = userInput.create()
        alertDialog.show()

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(View.OnClickListener {
            if (TextUtils.isEmpty(input.text.toString())) {
                Toast.makeText(
                    this@MainActivity,
                    getString(R.string.toastTarefa),
                    Toast.LENGTH_LONG
                ).show()
                return@OnClickListener
            } else {
                alertDialog.dismiss()
            }

            createListaItem(input.text.toString())
        })
    }

    private fun createListaItem(listaText: String) {
        val item = db!!.insertListaItem(listaText)
        val novoItem = db!!.getListaItem(item)

        if (novoItem != null) {
            itemsList.add(0, novoItem)
            mAdapter!!.notifyDataSetChanged()
        }
    }
}