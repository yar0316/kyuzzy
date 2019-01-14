package com.yar0316.kyuzzy

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import com.yar0316.kyuzzy.dao.EventRegistrationDAO
import com.yar0316.kyuzzy.models.EventModel
import io.realm.RealmResults

import kotlinx.android.synthetic.main.activity_registration.*
import kotlinx.android.synthetic.main.content_registration.*

class RegistrationActivity : AppCompatActivity() {

    //Activity起動時に登録されている一括登録イベント
    private lateinit var registeredEvents: RealmResults<EventModel>

    private val eventRegister: EventRegistrationDAO = EventRegistrationDAO()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        setSupportActionBar(toolbar)

        //既に登録されているイベントをカレンダーに反映
        registeredEvents = eventRegister.getEventByTitle("Holiday")

        calendarRegistration.selectedDates = MainActivity().getEventRegisteredDates(registeredEvents)

        //TODO 登録するイベントを変更(あらかじめ作って保存しておく。後回し)
        fab.setOnClickListener { view ->
            Snackbar.make(view, "登録するイベントを変更できるようになります。", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        //イベントを保存してMainActivityに戻る
        buttonCompleteRegistration.setOnClickListener {
            // プリファレンスに選択された日付を保存
            eventRegister.saveEventsOnSelectedDates(calendarRegistration.selectedDates)

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        eventRegister.closeRealmInstance()
    }
}
