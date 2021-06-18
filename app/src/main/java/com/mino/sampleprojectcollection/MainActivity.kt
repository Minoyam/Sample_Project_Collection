package com.mino.sampleprojectcollection

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.mino.sampleprojectcollection.databinding.ActivityMainBinding
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        initViews()

        val model = fetchDataFromSharedPreferences()
        renderView(model)
    }

    private fun initViews() {
        binding.run {
            onOffButton.setOnClickListener {
                val model = it.tag as? AlarmDisplayModel ?: return@setOnClickListener
                val newModel = saveAlarmModel(model.hour, model.minute, model.onOff.not())
                renderView(newModel)

                if (newModel.onOff) {
                    val calendar = Calendar.getInstance().apply {
                        set(Calendar.HOUR_OF_DAY, newModel.hour)
                        set(Calendar.MINUTE, newModel.minute)

                        if (before(Calendar.getInstance())) {
                            add(Calendar.DATE, 1)
                        }
                    }

                    val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
                    val intent = Intent(this@MainActivity, AlarmReceiver::class.java)
                    val pendingIntent = PendingIntent.getBroadcast(
                        this@MainActivity,
                        ALARM_REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT
                    )



                    alarmManager.setInexactRepeating(
                        AlarmManager.RTC_WAKEUP,
                        calendar.timeInMillis,
                        AlarmManager.INTERVAL_DAY,
                        pendingIntent
                    )
                } else {
                    cancelAlarm()
                }
            }

            val calendar = Calendar.getInstance()

            changeAlarmTimeButton.setOnClickListener {
                TimePickerDialog(this@MainActivity, { picker, hour, minute ->

                    val model = saveAlarmModel(hour, minute, false)
                    renderView(model)
                    cancelAlarm()

                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false)
                    .show()
            }
        }
    }

    private fun cancelAlarm() {
        val pendingIntent = PendingIntent.getBroadcast(
            this@MainActivity,
            ALARM_REQUEST_CODE,
            Intent(this@MainActivity, AlarmReceiver::class.java),
            PendingIntent.FLAG_NO_CREATE
        )
        pendingIntent?.cancel()
    }

    private fun saveAlarmModel(hour: Int, minute: Int, onOff: Boolean): AlarmDisplayModel {
        val model = AlarmDisplayModel(
            hour = hour,
            minute = minute,
            onOff = onOff
        )

        val sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)

        with(sharedPreferences.edit()) {
            putString(ALARM_KEY, model.makeDataForDB())
            putBoolean(ON_OFF_KEY, model.onOff)
            commit()
        }
        return model
    }

    private fun fetchDataFromSharedPreferences(): AlarmDisplayModel {
        val sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)

        val timeDBValue = sharedPreferences.getString(ALARM_KEY, "9:30") ?: "9:30"
        val onOffDBValue = sharedPreferences.getBoolean(ON_OFF_KEY, false)
        val alarmData = timeDBValue.split(":")

        val alarmDisplayModel = AlarmDisplayModel(
            hour = alarmData[0].toInt(),
            minute = alarmData[1].toInt(),
            onOff = onOffDBValue
        )

        val pendingIntent = PendingIntent.getBroadcast(
            this,
            ALARM_REQUEST_CODE,
            Intent(this, AlarmReceiver::class.java),
            PendingIntent.FLAG_NO_CREATE
        )
        if ((pendingIntent == null) and alarmDisplayModel.onOff) {
            alarmDisplayModel.onOff = false
        } else if ((pendingIntent != null) and alarmDisplayModel.onOff.not()) {
            pendingIntent.cancel()
        }
        return alarmDisplayModel
    }

    private fun renderView(model: AlarmDisplayModel) {
        binding.run {
            amPmTextView.text = model.amPmText
            timeTextView.text = model.timeText
            onOffButton.text = model.onOffText
            onOffButton.tag = model
        }

    }

    companion object {
        private const val SHARED_PREFERENCES_NAME = "time"
        private const val ALARM_KEY = "alarm"
        private const val ON_OFF_KEY = "onOff"
        private const val ALARM_REQUEST_CODE = 1000
    }
}