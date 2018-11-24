package com.footballnukes.moreorlessfootballers.updater.network

import com.footballnukes.moreorlessfootballers.updater.model.InstaResult
import com.footballnukes.moreorlessfootballers.updater.model.WikiTitle
import io.reactivex.Flowable
import retrofit2.http.Query
import retrofit2.http.GET



/**
 * Created by musooff on 04/11/2018.
 */

interface UpdaterAPI {

    @GET("instagram/info?")
    fun instaInfo(@Query("username") username: String): Flowable<InstaResult>

    @GET("api.php?action=query&prop=pageimages&piprop=original&format=json")
    fun wikiImage(@Query("titles") titles: String): Flowable<WikiTitle>

    @GET("api.php?action=query&prop=imageinfo&iiprop=extmetadata&format=json")
    fun wikiImageInfo(@Query("titles") titles: String): Flowable<WikiTitle>

}