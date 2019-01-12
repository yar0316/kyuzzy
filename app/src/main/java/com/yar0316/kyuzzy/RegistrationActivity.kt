package com.yar0316.kyuzzy

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import com.yar0316.kyuzzy.models.EventModel
import io.realm.Realm
import io.realm.RealmResults

import kotlinx.android.synthetic.main.activity_registration.*
import kotlinx.android.synthetic.main.content_registration.*
import java.util.*

class RegistrationActivity : AppCompatActivity() {


    private lateinit var realm: Realm
    //Activity起動時に登録されている一括登録イベント
    private lateinit var registeredEvents: RealmResults<EventModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        setSupportActionBar(toolbar)

        //Realmとの接続を取得
        realm = Realm.getDefaultInstance()

        //既に登録されているイベントをカレンダーに反映
        registeredEvents = realm.where(EventModel::class.java).equalTo("eventTitle", "Holiday").findAll()
        calendarRegistration.selectedDates = MainActivity().getEventRegisteredDates(registeredEvents)

        //TODO 登録するイベントを変更(あらかじめ作って保存しておく。後回し)
        fab.setOnClickListener { view ->
            Snackbar.make(view, "登録するイベントを変更できるようになります。", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        //イベントを保存してMainActivityに戻る
        buttonCompleteRegistration.setOnClickListener {
            // プリファレンスに選択された日付を保存
            saveSelectedDates(calendarRegistration.selectedDates)

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }


    /**
     * 選択された日付で休日イベントを登録する
     * 同月のすでに登録されているイベントは削除してから追加
     * @param selectedDays 選択された日付
     */
    private fun saveSelectedDates(selectedDays: List<Calendar>){
        //同月既に登録されてるイベントをいったん削除
        val selectedMonth: Int = selectedDays[0].get(Calendar.MONTH)
        resetDefaultEvents(selectedMonth)
        //データベースにイベントを保管
        realm.executeTransaction {
            selectedDays.forEach {it ->
                var registEvent:EventModel = realm.createObject(EventModel::class.java)
                registEvent.eventDate = it.time
                //以下暫定(TODO 後ほどRealmに追加)
                registEvent.eventTitle = "Holiday"
                registEvent.eventRemark = "サンプル休日イベント"
            }
        }
    }

    /**
     * 変更を反映するため、
     * イベント登録時に、すでに登録されていたイベントをリセットする
     * @param selectedMonth 登録する月
     */
    private fun resetDefaultEvents(selectedMonth: Int){
        //今回選択したイベントの月でフィルターをかけて該当のイベントを削除
        registeredEvents.filter {
            val tmpCal:Calendar = Calendar.getInstance()
            tmpCal.time = it.eventDate
            tmpCal.get(Calendar.MONTH) == selectedMonth
        }
        registeredEvents.forEach { it.deleteFromRealm() }
    }
}
