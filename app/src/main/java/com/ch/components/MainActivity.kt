package com.ch.components

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.widget.AppCompatTextView
import com.ch.ui.form.FormFiled
import com.ch.ui.form.FormLayout

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        val title = findViewById<AppCompatTextView>(R.id.fl_title);
//        title.setOnClickListener {
//            Toast.makeText(this, "${title.text}", Toast.LENGTH_SHORT).show()
//        }

        val ff = findViewById<FormFiled>(R.id.ff);
        ff.setOnClickListener {
        }
        findViewById<Button>(R.id.btn).setOnClickListener {
            Toast.makeText(this, "金额为：${ff.mEtText.text.toString().trim().toFloat()}", Toast.LENGTH_SHORT).show()
        }
    }
}