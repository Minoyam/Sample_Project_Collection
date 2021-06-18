package com.mino.sampleprojectcollection

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.mino.sampleprojectcollection.databinding.ActivityMainBinding
import org.json.JSONArray
import org.json.JSONObject
import kotlin.math.absoluteValue

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        initViews()
        initData()
    }

    private fun initViews() {
        binding.viewPager.setPageTransformer { page, position ->
            when {
                position.absoluteValue >= 1.0F -> {
                    page.alpha = 0F
                }
                position == 0F -> {
                    page.alpha = 1F
                }
                else -> {
                    page.alpha = 1F - (2 * position.absoluteValue)
                }
            }
        }
    }

    private fun initData() {
        val remoteConfig = Firebase.remoteConfig
        remoteConfig.setConfigSettingsAsync(
            remoteConfigSettings {
                minimumFetchIntervalInSeconds = 0
            }
        )
        remoteConfig.fetchAndActivate().addOnCompleteListener {
            binding.progressBar.isVisible = false
            if (it.isSuccessful) {
                val quotes = parseQuotesJson(remoteConfig.getString("quotes"))
                val isNameRevealed = remoteConfig.getBoolean("is_name_revealed")
                displayQuotePager(quotes, isNameRevealed)
            }
        }
    }

    private fun displayQuotePager(quotes: List<QuoteResponse>, nameRevealed: Boolean) {
        val adapter = QuotePagerAdapter(quotes, nameRevealed)
        binding.viewPager.adapter = adapter
        binding.viewPager.setCurrentItem(adapter.itemCount / 2, false)
    }

    private fun parseQuotesJson(json: String): List<QuoteResponse> {
        val jsonArray = JSONArray(json)
        var jsonList = emptyList<JSONObject>()
        for (index in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(index)
            jsonObject?.let {
                jsonList = jsonList + it
            }
        }
        return jsonList.map {
            QuoteResponse(
                it.getString("quote"),
                it.getString("name")
            )
        }
    }
}