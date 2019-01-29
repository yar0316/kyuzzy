package com.yar0316.kyuzzy

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import com.yar0316.kyuzzy.dao.EventRegistrationDAO
import com.yar0316.kyuzzy.models.EventModel
import com.yar0316.kyuzzy.models.EventTypeModel
import io.realm.RealmResults

import kotlinx.android.synthetic.main.activity_registration.*
import kotlinx.android.synthetic.main.content_registration.*

class RegistrationActivity : AppCompatActivity() {

    //Activity起動時に登録されている一括登録イベント
    private lateinit var registeredEvents: RealmResults<EventModel>

    private lateinit var eventType: EventTypeModel

    private val eventRegister: EventRegistrationDAO = EventRegistrationDAO()

    private val EVENT_TITLE: String = "eventTitle"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        setSupportActionBar(toolbar)

        val pref = PreferenceManager.getDefaultSharedPreferences(this)

        //既に登録されているイベントをカレンダーに反映
        registeredEvents = eventRegister.getEventsByTitle(pref.getString(EVENT_TITLE, "Holiday"))

        calendarRegistration.selectedDates = MainActivity().getEventRegisteredDates(registeredEvents)

        //TODO 登録するイベントを変更(あらかじめ作って保存しておく。後回し)
        fab.setOnClickListener { view ->
            Snackbar.make(view, "登録するイベントを変更できるようになります。", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        //イベントを保存してMainActivityに戻る
        buttonCompleteRegistration.setOnClickListener {
            //仮にここでEventTypeを初期化
            eventType = EventTypeModel()
            // Realmに選択されたイベントを保存
            eventRegister.saveEventsOnSelectedDates(calendarRegistration.selectedDates, eventType)

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        saveEventTitle(eventType.eventTitle)
        eventRegister.closeRealmInstance()
    }

    private fun saveEventTitle(eventTitle: String){
        val pref = PreferenceManager.getDefaultSharedPreferences(this)
        val prefEditor = pref.edit()

        prefEditor.clear()
            .putString(EVENT_TITLE,eventTitle)
            .apply()
    }

}
