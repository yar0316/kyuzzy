package com.yar0316.kyuzzy.models

import io.realm.RealmObject
import io.realm.annotations.Required
import java.util.*

open class EventModel: RealmObject() {

    @Required
    var eventDate: Date = Date()
    @Required
    var eventTitle: String = ""
    var eventRemark: String = ""
}