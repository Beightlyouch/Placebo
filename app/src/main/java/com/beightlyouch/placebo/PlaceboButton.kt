package com.beightlyouch.placebo

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*

open class PlaceboButton : RealmObject() {
    @PrimaryKey
    var id: Long = 0
    var dateTime: Date = Date()
    var title: String = ""  //タイトル
    var count: Long = 0
}