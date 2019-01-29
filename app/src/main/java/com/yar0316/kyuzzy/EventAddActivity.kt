package com.yar0316.kyuzzy

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_add_event.*

class EventAddActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_event)

        buttonCompleteRegistrationSingle.setOnClickListener {

        }

        buttonCancelRegistrationSingle.setOnClickListener {
            val intent  = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}
