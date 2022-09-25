package com.udacity

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {

    private lateinit var fileNameTextView: TextView
    private lateinit var statusTextView: TextView
    private lateinit var fab: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)

        var status = intent.getStringExtra("status")
        var fileName = intent.getStringExtra("file name")

        fileNameTextView = findViewById(R.id.file_name_container)
        statusTextView = findViewById(R.id.status_container)
        fab = findViewById(R.id.fab)

        fileNameTextView.text = fileName
        statusTextView.text = status

        fab.setOnClickListener {
            finish()
        }
    }

}
