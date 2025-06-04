package com.example.tea1

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.gson.Gson
import java.io.File
import java.io.FileReader
import java.io.FileWriter

class ShowListActivity : GenericActivity() {

    private lateinit var itemsListView: ListView
    private lateinit var nouvelItemEditText: EditText
    private lateinit var okButtonItem: Button
    private lateinit var pseudo: String
    private var listIndex: Int = -1
    private var profil: ProfilListeToDo? = null
    private var currentList: ListeToDo? = null
    private lateinit var itemAdapter: ItemToDoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_list)

        pseudo = intent.getStringExtra("pseudo") ?: ""
        listIndex = intent.getIntExtra("listIndex", -1)

        itemsListView = findViewById(R.id.itemsListView)
        nouvelItemEditText = findViewById(R.id.nouvelItemEditText)
        okButtonItem = findViewById(R.id.okButtonItem)

        loadProfil()
        currentList = profil?.mesListesToDo?.getOrNull(listIndex)

        if (currentList == null) {
            Toast.makeText(this, "Liste non trouvée", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        title = currentList?.titreListeToDo

        itemAdapter = ItemToDoAdapter(this, currentList!!.lesItems)
        itemsListView.adapter = itemAdapter

        okButtonItem.setOnClickListener {
            val newItemDescription = nouvelItemEditText.text.toString()
            if (newItemDescription.isNotEmpty()) {
                val newItem = ItemToDo(newItemDescription)
                currentList?.lesItems?.add(newItem)
                saveProfil()
                itemAdapter.notifyDataSetChanged()
                nouvelItemEditText.text.clear()
            } else {
                Toast.makeText(this, "La description de l'item ne peut pas être vide", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadProfil() {
        val file = File(filesDir, "$pseudo.json")
        if (file.exists()) {
            val gson = Gson()
            val fileReader = FileReader(file)
            profil = gson.fromJson(fileReader, ProfilListeToDo::class.java)
            fileReader.close()
        } else {
            profil = ProfilListeToDo(pseudo)
        }
        if (profil == null) {
            profil = ProfilListeToDo(pseudo)
        }
    }

    private fun saveProfil() {
        profil?.let {
            val gson = Gson()
            val jsonString = gson.toJson(it)
            val file = File(filesDir, "$pseudo.json")
            val fileWriter = FileWriter(file)
            fileWriter.write(jsonString)
            fileWriter.close()
        }
    }

    inner class ItemToDoAdapter(context: Context, private val items: MutableList<ItemToDo>) :
        ArrayAdapter<ItemToDo>(context, 0, items) {

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.list_item_todo, parent, false)
            val item = items[position]

            val itemDescriptionTextView = view.findViewById<TextView>(R.id.itemDescriptionTextView)
            val itemCheckBox = view.findViewById<CheckBox>(R.id.itemCheckBox)

            itemDescriptionTextView.text = item.description
            itemCheckBox.isChecked = item.fait

            itemCheckBox.setOnCheckedChangeListener { _, isChecked ->
                item.fait = isChecked
                saveProfil()
            }
            return view
        }
    }
} 