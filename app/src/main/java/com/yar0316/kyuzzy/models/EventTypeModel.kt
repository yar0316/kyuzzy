package com.yar0316.kyuzzy.models

import io.realm.RealmObject

import io.realm.annotations.Required

open class EventTypeModel: RealmObject() {
    @Required
    var eventTitle: String = "Holiday"

    var eventIconId: Int = 0x7f080077

    var eventRemark: String = ""

}