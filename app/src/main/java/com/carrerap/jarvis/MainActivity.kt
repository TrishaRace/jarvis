package com.carrerap.jarvis


import ai.api.AIConfiguration
import ai.api.AIListener
import ai.api.android.AIService
import ai.api.model.AIError
import ai.api.model.AIResponse
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), AIListener {

    lateinit var mAIService: AIService
    lateinit var textToSpeech :TextToSpeech
    lateinit var buttonToSpeech : FloatingActionButton
    lateinit var userPhoto :ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        userPhoto = findViewById(R.id.ivPeterParker)
        askAllPermissionsNeeded()

        val config = ai.api.android.AIConfiguration(
            DIALOG_FLOW_KEY,
            AIConfiguration.SupportedLanguages.Spanish,
            ai.api.android.AIConfiguration.RecognitionEngine.System
        )

        mAIService = AIService.getService(this, config)
        mAIService.setListener(this)

        buttonToSpeech = findViewById(R.id.bMicrophone)
        buttonToSpeech.setOnClickListener{ mAIService.startListening() }

        textToSpeech =  TextToSpeech(this, TextToSpeech.OnInitListener{})


    }

    //AIListener override methods
    override fun onResult(aiResponse: AIResponse?) {
        val result = aiResponse?.result
        var subject = ""

        userPhoto.setImageResource(R.drawable.peter)

        textToSpeech.speak(result?.fulfillment?.speech, TextToSpeech.QUEUE_FLUSH, null, null)

        tvEdithDialog.visibility = View.VISIBLE

        tvPeterDialog.text = result?.resolvedQuery
        tvPeterDialog.visibility =View.VISIBLE
        tvEdithDialog.text = result?.fulfillment?.speech

        if (result!!.action == "showSubjectInfo") {
            subject = result.parameters["subject"]!!.toString()
            subject = subject.removeSurrounding("\"","\"")
            openResume(subject)
        }

        result.contexts.forEach { context ->
            if (context.name.equals("thorfeeding")) {
                userPhoto.setImageResource(R.drawable.thor)
            }
        }
    }
    override fun onListeningStarted() {}
    override fun onAudioLevel(level: Float) {}
    override fun onError(error: AIError?) {Log.d("tag", error.toString())}
    override fun onListeningCanceled() {}
    override fun onListeningFinished() {}

    fun openResume(subject: String) {
        val intent = Intent(this@MainActivity, ResumeActivity::class.java)
        val bundle = Bundle()
        bundle.putString("subject", subject)
        intent.putExtras(bundle)
        startActivity(intent)
    }

    fun askAllPermissionsNeeded() {
        val permissions = arrayOf(Manifest.permission.INTERNET,Manifest.permission.RECORD_AUDIO)
        requestPermission(permissions, MY_PERMISSION_REQUEST)
    }

    fun requestPermission(permissions: Array<String>,myPermissionConstant : Int) {
        val permissionsNotGranted : ArrayList<String> = ArrayList()

        permissions.forEach { permission ->
            if (ContextCompat.checkSelfPermission(this, permission)
                != PackageManager.PERMISSION_GRANTED) {
                permissionsNotGranted.add(permission)
            }
            permissionsNotGranted.toArray()
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.INTERNET,Manifest.permission.RECORD_AUDIO),myPermissionConstant)
        }


    }

}
