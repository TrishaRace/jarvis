package com.carrerap.jarvis

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_resume.*

class ResumeActivity : AppCompatActivity() {

    private var subject: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resume)

        val bundle = getIntent().getExtras()
        subject = bundle.getString("subject") as String

        val image = findViewById<ImageView>(R.id.ivSubject)

        if (subject.equals(BIOLOGY)){
            image.setImageResource(R.drawable.biologia)
            tvSubjectResume.text = resources.getText(R.string.biology_resume)
        }else if (subject.equals(HISTORY)){
            image.setImageResource(R.drawable.historia)
            tvSubjectResume.text = resources.getText(R.string.history_resume)

        }
    }

}