package com.example.tea1

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.google.gson.Gson

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings_container, SettingsFragment())
                .commit()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    class SettingsFragment : PreferenceFragmentCompat() {
        private val PREFS_NAME = "com.example.tea1.prefs"
        private val KEY_PSEUDO_HISTORY = "pseudo_history"
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.preferences, rootKey)

            val clearHistoryPreference = findPreference<androidx.preference.Preference>("clear_pseudo_history")
            clearHistoryPreference?.setOnPreferenceClickListener {
                clearPseudoHistory()
                true
            }
        }

        private fun clearPseudoHistory() {
            val prefs = requireActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            val editor = prefs.edit()
            editor.putString(KEY_PSEUDO_HISTORY, Gson().toJson(emptyList<String>()))
            editor.apply()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
} 