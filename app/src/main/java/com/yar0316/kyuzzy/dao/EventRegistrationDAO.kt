package com.yar0316.kyuzzy.dao

import com.yar0316.kyuzzy.models.EventModel
import io.realm.Realm
import io.realm.RealmResults
import java.util.*

/**
 * イベントの登録や取得などに関するRealmとのやりとりを管理するDAO
 * あまりにでかくなるようなら分ける
 * @author yar0316
 */
class EventRegistrationDAO {
    companion object {
        const val EVENT_TITLE = "eventTitle"
    }
    val realm: Realm = Realm.getDefaultInstance()

    /**
     * イベント名からイベント一覧を取得
     * @param fieldValue イベント名
     * @return Realmから取得したイベント一覧
     */
    fun getEventByTitle(fieldValue: String): RealmResults<EventModel>{
        return realm.where(EventModel::class.java).equalTo(EVENT_TITLE, fieldValue).findAll()
    }

    /**
     * 選択された日付で休日イベントを登録する
     * 同月のすでに登録されているイベントは削除してから追加
     * @param selectedDays 選択された日付
     */
     fun saveEventsOnSelectedDates(selectedDays: List<Calendar>) {
        //同月既に登録されてるイベントをいったん削除
        val selectedMonth: Int = selectedDays[0].get(Calendar.MONTH)
        resetDefaultEvents(selectedMonth)
        //データベースにイベントを保管
        selectedDays.forEach { it ->
            var registEvent = EventModel()
            registEvent.eventDate = it.time
            //以下暫定(TODO 後ほどRealmに追加)
            registEvent.eventTitle = "Holiday"
            registEvent.eventRemark = "サンプル休日イベント"

            realm.beginTransaction()
            realm.copyToRealm(registEvent)
            realm.commitTransaction()
        }
    }

    /**
     * 変更を反映するため、
     * イベント登録時に、すでに登録されていたイベントをリセットする
     * @param selectedMonth 登録する月
     */
    private fun resetDefaultEvents(selectedMonth: Int){
        val registeredEvents: RealmResults<EventModel> = getEventByTitle("Holiday")
        //今回選択したイベントの月でフィルターをかけて該当のイベントを削除
        realm.beginTransaction()
        registeredEvents.filter {
            val tmpCal:Calendar = Calendar.getInstance()
            tmpCal.time = it.eventDate
            tmpCal.get(Calendar.MONTH) == selectedMonth
        }
        registeredEvents.deleteAllFromRealm()
        realm.commitTransaction()
    }

    fun closeRealmInstance(){
        realm.close()
    }
}