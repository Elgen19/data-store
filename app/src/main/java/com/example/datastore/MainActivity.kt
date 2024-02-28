package com.example.datastore

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class MainActivity : AppCompatActivity() {

    private val dataStore by preferencesDataStore(name = "key_value_datastore")
    private lateinit var keyEditText: EditText
    private lateinit var valueEditText: EditText
    private lateinit var saveButton: Button
    private lateinit var searchKeyEditText: EditText
    private lateinit var displayButton: Button
    private lateinit var displayTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        keyEditText = findViewById(R.id.keyEditText)
        valueEditText = findViewById(R.id.valueEditText)
        saveButton = findViewById(R.id.saveButton)
        searchKeyEditText = findViewById(R.id.searchKeyEditText)
        displayButton = findViewById(R.id.displayButton)
        displayTextView = findViewById(R.id.displayTextView)



        saveButton.setOnClickListener {
            val key = keyEditText.text.toString()
            val value = valueEditText.text.toString()
            saveToDataStore(key, value)
        }

        displayButton.setOnClickListener {
            val keyToSearch = searchKeyEditText.text.toString()
            getValueFromDataStore(keyToSearch) { value ->
                displayTextView.text = value
            }
        }
    }

    private fun saveToDataStore(key: String, value: String) {
        runBlocking {
            val dataStoreKey = stringPreferencesKey(key)
            dataStore.edit { preferences ->
                preferences[dataStoreKey] = value
            }
            Toast.makeText(this@MainActivity, "Save successfully!", Toast.LENGTH_LONG).show()
            keyEditText.text.clear()
            valueEditText.text.clear()
        }
    }

    private fun getValueFromDataStore(key: String, callback: (String) -> Unit) {
        runBlocking {
            val dataStoreKey = stringPreferencesKey(key)
            val preferences = dataStore.data.first()
            val value = preferences[dataStoreKey] ?: "Key not found"
            callback(value)
        }
    }
}
