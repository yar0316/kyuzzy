package com.yar0316.kyuzzy.models

import io.realm.RealmObject
import java.util.*

open class EventModel: RealmObject() {
    var eventDate: Date = Date()
    var eventTitle: String = ""
    var eventRemark: String = ""
}