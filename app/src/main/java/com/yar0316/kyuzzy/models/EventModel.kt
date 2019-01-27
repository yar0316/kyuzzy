package com.yar0316.kyuzzy.models

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.Required
import java.util.*

/**
 * 一括登録・単体登録イベントともにこれを利用する(予定)
 * 2019/1/27 はじめは一括登録特化で！
 * @author yar0316
 */
open class EventModel: RealmObject() {
    @Required
    var eventDate: Date = Date()
    @Required
    var eventTitle: String = ""

    var eventRemark: String = ""

    var eventType = RealmList<EventTypeModel>()
}