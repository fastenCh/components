package com.ch.components

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.widget.AppCompatTextView
import com.ch.ui.form.FormFiled
import com.ch.ui.form.FormLayout

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val title = findViewById<AppCompatTextView>(R.id.fl_title);
        title.setOnClickListener {
            Toast.makeText(this, "${title.text}", Toast.LENGTH_SHORT).show()
        }
        val fl = findViewById<FormLayout>(R.id.fl);
        val text = findViewById<AppCompatTextView>(R.id.tv_text);
        text.setOnClickListener {
            text.setText("我是改变后的文本我是改变后的文本我是改变后的文本我是改变后的文本我是改变后的文本我是改变后的文本我是改变后的文本我是改变后的文本我是改变后的文本我是改变后的文本我是改变后的文本我是改变后的文本我是改变后的文本我是改变后的文本我是改变后的文本我是改变后的文本我是改变后的文本")
            fl.setUnit("")
        }
        val ff = findViewById<FormFiled>(R.id.ff);
        ff.setOnClickListener {
            ff.setUnit("")
        }

    }
}