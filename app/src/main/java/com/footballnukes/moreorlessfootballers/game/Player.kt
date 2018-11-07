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
    var instagramFollowers: Int = 0
    var wikipediaImageUrl:String? = null
    var artist: String? = null
    var wikiName: String? = null
    var wikiImageCredits: String? = null
    var wikiImageTitle: String? = null
    var licenseShortName: String? = null
    var licenceUrl: String? = null
    var facebookId: String? =null
    var facebookFollowers: Int = 0
    var jerseyNumber: Int = 0
    var value: Int = 0
    var goals: Int = 0
    var fifaRanking: Int = 0
    var appearance: Int = 0
    var twitterId: String? = null
    var twitterFollowers: Int = 0
    var club: String? = null
    var netWorth: Int = 0
    var age: Int = 0
    var position: String? = null
    var height: Double = 0.0
}