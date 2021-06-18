package com.mino.sampleprojectcollection

import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import java.util.*
import kotlin.concurrent.timer

class PhotoFrameActivity : AppCompatActivity() {

    private val photoList = mutableListOf<Uri>()

    private var currentPosition = 0

    private val photoImageView: ImageView by lazy {
        findViewById(R.id.photoImageView)
    }
    private val backgroundPhotoImageView: ImageView by lazy {
        findViewById(R.id.backgroundPhotoImageView)
    }

    private var timer : Timer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photoframe)

        getPhotoUriFromIntent()
    }

    override fun onStart() {
        super.onStart()

        startTimer()
    }
    override fun onStop() {
        super.onStop()

        timer?.cancel()
    }

    override fun onDestroy() {
        super.onDestroy()

        timer?.cancel()
    }

    private fun getPhotoUriFromIntent() {
        val size = intent.getIntExtra("photoListSize", 0)
        for (i in 0..size) {
            intent.getStringExtra("photo$i")?.let {
                photoList.add(Uri.parse(it))
            }
        }
    }

    private fun startTimer() {
        timer = timer(period = 5 * 1000) {
            runOnUiThread {
                val current = currentPosition
                val next = if (photoList.size <= currentPosition + 1) 0 else currentPosition + 1

                backgroundPhotoImageView.setImageURI(photoList[current])

                photoImageView.apply {
                    alpha = 0f
                    setImageURI(photoList[next])
                    animate()
                        .alpha(1.0f)
                        .setDuration(1000)
                        .start()
                }
                currentPosition = next
            }
        }
    }
}