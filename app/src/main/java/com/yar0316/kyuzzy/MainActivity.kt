package com.yar0316.kyuzzy

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import com.applandeo.materialcalendarview.EventDay
import com.yar0316.kyuzzy.dao.EventRegistrationDAO
import com.yar0316.kyuzzy.models.EventModel
import io.realm.RealmResults
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    //navbarのアイテムが選択された時の動作を規定 TODO 2019/1/29 機能検討
    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_month -> {
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_add -> {
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_day -> {
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    private val eventRegister = EventRegistrationDAO()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val eventsList: MutableList<EventDay> = mutableListOf()
        val eventsRegisteredInBulk: RealmResults<EventModel> = eventRegister.getAllEvents()

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        buttonRegistration.setOnClickListener {
            val intent = Intent(this, RegistrationActivity::class.java)
            startActivity(intent)
        }

        //登録されたイベントをカレンダーに反映
        getEventRegisteredDates(eventsRegisteredInBulk).forEach {
            eventsList.add(
                EventDay(
                    it,
                    getEventIcon(eventsRegisteredInBulk)
                )
            )
        }
        calendarView.setEvents(eventsList)
    }

    override fun onDestroy() {
        super.onDestroy()
        eventRegister.closeRealmInstance()
    }

    /**
     * 一括登録用イベントが登録されている日付一覧をRealmResultsから取得する
     * @return 登録されている日付のリスト
     */
    fun getEventRegisteredDates(registeredEvents: RealmResults<EventModel>): MutableList<Calendar> {
        val eventDates: MutableList<Calendar> = mutableListOf()
        if (registeredEvents.size == 0) {
            return eventDates
        }
        registeredEvents.forEach {
            val tmpDate: Date = it.eventDate
            val tmpCal: Calendar = Calendar.getInstance()
            tmpCal.time = tmpDate
            eventDates.add(tmpCal)
        }
        return eventDates
    }

    private fun getEventIcon(registeredEvents: RealmResults<EventModel>): Int {
        if (registeredEvents.size == 0) {
            return R.drawable.icon_holiday_dafault
        }
        return registeredEvents[0]!!.eventType[0]!!.eventIconId
    }


}
