package com.example.tea1

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import com.google.gson.Gson
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.io.IOException

class ChoixListActivity : GenericActivity() {

    private lateinit var listView: ListView
    private lateinit var nouvelleListeEditText: EditText
    private lateinit var okButtonListe: Button
    private lateinit var pseudo: String
    private var profil: ProfilListeToDo? = null
    private lateinit var adapter: ArrayAdapter<String>
    private val listTitles = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choix_list)

        pseudo = intent.getStringExtra("pseudo") ?: ""
        title = "Listes de $pseudo"

        listView = findViewById(R.id.listView)
        nouvelleListeEditText = findViewById(R.id.nouvelleListeEditText)
        okButtonListe = findViewById(R.id.okButtonListe)

        loadProfil()
        if (profil == null) {
            profil = ProfilListeToDo(pseudo) //
            saveProfil()
        }

        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, listTitles)
        listView.adapter = adapter
        updateListView() //

        listView.setOnItemClickListener { _, _, position, _ ->
            val intent = Intent(this, ShowListActivity::class.java)
            intent.putExtra("pseudo", pseudo)
            intent.putExtra("listIndex", position)
            startActivity(intent)
        }

        okButtonListe.setOnClickListener {
            val newListName = nouvelleListeEditText.text.toString()
            if (newListName.isNotEmpty()) {
                val newList = ListeToDo(newListName)
                profil?.ajouteListe(newList)
                saveProfil()
                updateListView()
                nouvelleListeEditText.text.clear()
            } else {
                Toast.makeText(this, "Le nom de la liste ne peut pas être vide", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        loadProfil()
        if (profil == null) {
            profil = ProfilListeToDo(pseudo)
        }
        updateListView()
    }

    private fun loadProfil() {
        val file = File(filesDir, "$pseudo.json")
        if (file.exists()) {
            try {
                FileReader(file).use { reader ->
                    profil = Gson().fromJson(reader, ProfilListeToDo::class.java)
                }
            } catch (e: IOException) {
                e.printStackTrace()
                Toast.makeText(this, "Erreur de chargement du profil: ${e.message}", Toast.LENGTH_LONG).show()
                profil = null
            } catch (e: com.google.gson.JsonSyntaxException) {
                e.printStackTrace()
                Toast.makeText(this, "Erreur de format du profil: ${e.message}", Toast.LENGTH_LONG).show()
                file.delete()
                profil = null
            }
        } else {
            profil = null
        }
    }

    private fun saveProfil() {
        profil?.let { currentProfil ->
            val file = File(filesDir, "$pseudo.json")
            try {
                FileWriter(file).use { writer ->
                    Gson().toJson(currentProfil, writer)
                }
            } catch (e: IOException) {
                e.printStackTrace()
                Toast.makeText(this, "Erreur de sauvegarde du profil: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun updateListView() {
        listTitles.clear()
        profil?.mesListesToDo?.forEach { listTitles.add(it.titreListeToDo) }
        adapter.notifyDataSetChanged()
    }
} 