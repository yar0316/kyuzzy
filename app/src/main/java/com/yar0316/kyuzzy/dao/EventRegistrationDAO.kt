package com.yar0316.kyuzzy.dao

import com.yar0316.kyuzzy.models.EventModel
import com.yar0316.kyuzzy.models.EventTypeModel
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
    private  val realm: Realm = Realm.getDefaultInstance()

    /**
     * 登録されたすべてのイベントを取得
     * @return Realmから取得したイベント一覧
     */
    fun getAllEvents(): RealmResults<EventModel>{
        return realm.where(EventModel::class.java).findAll()
    }

    /**
     *
     * @param eventTitle イベントのタイトル
     * @return
     */
    fun getEventsByTitle(eventTitle: String): RealmResults<EventModel>{
        val registeredEvents: RealmResults<EventModel> = getAllEvents()
        var registeredEventType: EventTypeModel? = realm.where(EventTypeModel::class.java).equalTo(EVENT_TITLE,eventTitle).findFirst()
        if(registeredEventType == null){
            registeredEventType = EventTypeModel()
        }
        registeredEvents.filter {
            registeredEventType.eventTitle == it.eventType[0]?.eventTitle
        }
        return registeredEvents
    }


    /**
     * 選択された日付で休日イベントを登録する
     * 同月のすでに登録されているイベントは削除してから追加
     * @param selectedDays 選択された日付
     */
     fun saveEventsOnSelectedDates(selectedDays: List<Calendar>, eventType: EventTypeModel) {
        //同月既に登録されてるイベントをいったん削除
        val selectedMonth: Int = selectedDays[0].get(Calendar.MONTH)
        resetDefaultEvents(selectedMonth, eventType.eventTitle)
        //データベースにイベントを保管
        selectedDays.forEach { it ->
            var eventToRegist = EventModel()
            eventToRegist.eventDate = it.time
            eventToRegist.eventType.add(eventType)

            realm.beginTransaction()
            realm.copyToRealm(eventToRegist)
            realm.commitTransaction()
        }
    }

    fun saveEventType(eventTitle: String, eventIconId: Int, eventRemark: String){
         val eventType = EventTypeModel()
        eventType.eventTitle = eventTitle
        eventType.eventIconId = eventIconId
        eventType.eventRemark = eventRemark

        realm.beginTransaction()
        realm.copyToRealm(eventType)
        realm.commitTransaction()
    }

    /**
     * 変更を反映するため、
     * イベント登録時に、すでに登録されていたイベントをリセットする
     * @param selectedMonth 登録する月
     */
    private fun resetDefaultEvents(selectedMonth: Int, eventTitle: String){
        val registeredEvents: RealmResults<EventModel> = getEventsByTitle(eventTitle)

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