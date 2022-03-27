package io.github.com.harutiro.brainexchange.date

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*

open class ProfileDateClass(
    @PrimaryKey open var id: String? = UUID.randomUUID().toString(),
    open var facebookId: String = "",
    open var brainImageUrl: String = "",
    open var userName: String = "",
    open var favList: String = "",

    ): RealmObject