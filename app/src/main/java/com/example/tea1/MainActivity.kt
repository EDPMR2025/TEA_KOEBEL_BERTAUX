package com.example.tea1

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MainActivity : GenericActivity() {

    private lateinit var pseudoEditText: AutoCompleteTextView
    private lateinit var okButton: Button
    private val PREFS_NAME = "com.example.tea1.prefs"
    private val KEY_PSEUDO = "pseudo"
    private val KEY_PSEUDO_HISTORY = "pseudo_history"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        pseudoEditText = findViewById(R.id.pseudoEditText)
        okButton = findViewById(R.id.okButton)

        val prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val savedPseudo = prefs.getString(KEY_PSEUDO, "")
        pseudoEditText.setText(savedPseudo)

        val pseudoHistory = loadPseudoHistory()
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, pseudoHistory)
        pseudoEditText.setAdapter(adapter)

        okButton.setOnClickListener {
            val pseudo = pseudoEditText.text.toString()
            if (pseudo.isNotEmpty()) {
                savePseudo(pseudo)
                addPseudoToHistory(pseudo)
                val intent = Intent(this, ChoixListActivity::class.java)
                intent.putExtra("pseudo", pseudo)
                startActivity(intent)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (super.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun savePseudo(pseudo: String) {
        val prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putString(KEY_PSEUDO, pseudo)
        editor.apply()
    }

    private fun addPseudoToHistory(pseudo: String) {
        val prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val historyJson = prefs.getString(KEY_PSEUDO_HISTORY, "[]")
        val type = object : TypeToken<MutableList<String>>() {}.type
        val history: MutableList<String> = Gson().fromJson(historyJson, type)
        if (!history.contains(pseudo)) {
            history.add(0, pseudo)
            if (history.size > 10) {
                history.removeAt(history.size - 1)
            }
        }
        val editor = prefs.edit()
        editor.putString(KEY_PSEUDO_HISTORY, Gson().toJson(history))
        editor.apply()
    }

    private fun loadPseudoHistory(): List<String> {
        val prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val historyJson = prefs.getString(KEY_PSEUDO_HISTORY, "[]")
        val type = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(historyJson, type)
    }
} 