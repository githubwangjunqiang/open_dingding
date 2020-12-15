package com.app.xq.dingding

import android.os.Bundle
import android.widget.TimePicker
import androidx.appcompat.app.AppCompatActivity


class AelectionTime : AppCompatActivity() {

    val mTimePicture by lazy {
        findViewById<TimePicker>(R.id.timepicture)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_aelection_time)

        mTimePicture.setOnTimeChangedListener { view, hourOfDay, minutes ->
        }

    }
}