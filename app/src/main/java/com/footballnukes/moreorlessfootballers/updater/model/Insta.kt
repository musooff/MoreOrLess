package com.footballnukes.moreorlessfootballers.updater.model

/**
 * Created by musooff on 04/11/2018.
 */

class Insta {
    var username: String? = null
    var profile_picture: String? = null
    var total_posts: String? = null
    var followers: String? = null
    var following: String? = null
}

class InstaResult{
    var success: Boolean = false
    var data: Insta? = null
}