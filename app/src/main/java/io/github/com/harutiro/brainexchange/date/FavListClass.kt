package io.github.com.harutiro.brainexchange.date

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class FavListClass :RealmObject{
    @PrimaryKey
    open var id: String = ""
    open var favNumber:Int = -1
}