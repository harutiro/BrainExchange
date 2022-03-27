package io.github.com.harutiro.brainexchange.date

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*

open class ProfileDateClass: RealmObject{
    @PrimaryKey open var id: String = ""
    open var facebookId: String = ""
    open var brainImageUrl: String = ""
    open var userName: String = ""
    open var favNumbers:String = ""

    open var freeComment: String = ""
    open var buildings:String = ""

}

