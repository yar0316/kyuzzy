package com.yar0316.kyuzzy

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import com.yar0316.kyuzzy.models.EventModel
import io.realm.Realm
import io.realm.RealmResults
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    //navbarのアイテムが選択された時の動作を規定 TODO あとで週ごと日ごとのイベントリストに飛べるようにする
    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_month -> {
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_week -> {
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_day -> {
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Realm.init(this)

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        buttonRegistration.setOnClickListener {
            val intent = Intent(this,RegistrationActivity::class.java)
            startActivity(intent)
        }


    }

    /**
     * 一括登録用イベントが登録されている日付をRealmから取得する
     * @return 登録されている日付のリスト
     */
    fun getEventRegisteredDates(registeredEvents: RealmResults<EventModel>): MutableList<Calendar>{
        val eventDates: MutableList<Calendar> = mutableListOf()
        if (registeredEvents.size == 0){
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
}
