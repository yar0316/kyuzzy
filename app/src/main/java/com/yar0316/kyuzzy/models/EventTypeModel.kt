package com.yar0316.kyuzzy.models

import io.realm.annotations.Required

class EventTypeModel {
    @Required
    var eventTitle: String = ""
    @Required
    var eventIconId: String = ""
    @Required
    var eventRemark: String = ""

}