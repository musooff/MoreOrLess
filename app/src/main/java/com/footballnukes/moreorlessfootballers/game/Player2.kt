package com.footballnukes.moreorlessfootballers.game

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.database.IgnoreExtraProperties

/**
 * Created by musooff on 03/11/2018.
 */

@IgnoreExtraProperties
class Player2{
    var id: String? = null
    var name:String? = null
    var instagram_id: String? = null
    var instagram_follower: String? = null
    var wikipedia_image_url:String? = null
    var author_name: String? = null
    var wiki_image_credits: String? = null
    var wiki_image_title: String? = null
    var license_short_name: String? = null
    var license_url: String? = null
    var facebook_id: String? =null
    var facebook_follower: String? = null
    var jersey_number: String? = null
    var value: String? = null
    var goals: String? = null
    var fifa_ranking: String? = null
    var appearance: String? = null
    var twitter_id: String? = null
    var twitter_follower: String? = null
    var image_url: String? = null
    var beauty_image_url: String? = null
    var club: String? = null
    var net_worth: String? = null
    var age: String? = null
    var position: String? = null

    constructor()
}