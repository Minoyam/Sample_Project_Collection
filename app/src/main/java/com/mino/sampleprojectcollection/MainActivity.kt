package com.mino.sampleprojectcollection

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mino.sampleprojectcollection.adapter.VideoAdapter
import com.mino.sampleprojectcollection.dto.VideoDto
import com.mino.sampleprojectcollection.service.VideoService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var videoAdapter: VideoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, PlayerFragment())
            .commit()

        videoAdapter = VideoAdapter(callback = { url, title ->
            supportFragmentManager.fragments.find {
                it is PlayerFragment
            }?.let {
                (it as PlayerFragment).play(url, title)
            }
        })

        findViewById<RecyclerView>(R.id.mainRecyclerView).apply {
            adapter = videoAdapter
            layoutManager = LinearLayoutManager(context)
        }
        getVideoList()
    }

    private fun getVideoList() {
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://run.mocky.io")
            .build()

        retrofit.create(VideoService::class.java)
            .also {
                it.listVideos()
                    .enqueue(object : Callback<VideoDto> {
                        override fun onResponse(
                            call: Call<VideoDto>,
                            response: Response<VideoDto>
                        ) {
                            if (response.isSuccessful.not()) return

                            response.body()?.let { videoDto ->
                                videoAdapter.submitList(videoDto.videos)
                            }
                        }

                        override fun onFailure(call: Call<VideoDto>, t: Throwable) {}
                    })
            }
    }
}