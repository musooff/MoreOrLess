package com.footballnukes.moreorlessfootballers.game

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by musooff on 03/11/2018.
 */

@Entity
class Player() {
    @PrimaryKey
    var id: Int = 0
    var name:String? = null
    var instagramId: String? = null
    var instagramFollowers: Long = 0
    var wikipediaImageUrl:String? = null
    var authorName: String? = null
    var wikiImageCredits: String? = null
    var wikiImageTitle: String? = null
    var licenseShorName: String? = null
    var licenceUrl: String? = null
    var facebookId: String? =null
    var facebbookFollowers: Long = 0
    var jerseyNumber: Int = 0
    var value: Long = 0
    var goals: Int = 0
    var fifaRanking: Int = 0
    var appearance: Int = 0
    var twitterId: String? = null
    var twitterFollowers: Long = 0
    var imageUrl: String? = null
    var beautyImageUrl: String? = null
    var club: String? = null
    var netWorth: Long = 0
    var age: Int = 0
    var position: String? = null
}