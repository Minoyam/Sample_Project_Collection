package com.mino.sampleprojectcollection

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.firebase.messaging.FirebaseMessaging
import com.mino.sampleprojectcollection.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        initFirebase()
        updateResult()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        setIntent(intent)
        updateResult(true)
    }

    private fun initFirebase() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                binding.firebaseTokenTextView.text = task.result
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateResult(isNewIntent: Boolean = false) {
        binding.resultTextView.text = (intent.getStringExtra("notificationType") ?: "App Launcher") + if (isNewIntent) {
            "(으)로 갱신했습니다."
        } else {
            "(으)로 실행했습니다."
        }
    }
}