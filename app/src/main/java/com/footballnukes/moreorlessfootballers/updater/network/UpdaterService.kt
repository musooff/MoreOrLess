package com.footballnukes.moreorlessfootballers.updater.network

import com.footballnukes.moreorlessfootballers.updater.model.InstaResult
import com.footballnukes.moreorlessfootballers.updater.model.WikiTitle
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.google.gson.GsonBuilder
import io.reactivex.Flowable


/**
 * Created by musooff on 04/11/2018.
 */

class UpdaterService {

    companion object {
        const val BASE_URL = "https://gramblast.com/"
        const val WIKI_URL = "https://en.wikipedia.org/w/"
    }

    private fun startService(): UpdaterAPI {
        val gson = GsonBuilder()
                .setLenient()
                .create()
        val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()

        return retrofit.create(UpdaterAPI::class.java)
    }

    private fun startServiceWiki(): UpdaterAPI {
        val gson = GsonBuilder()
                .setLenient()
                .create()
        val retrofit = Retrofit.Builder()
                .baseUrl(WIKI_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()

        return retrofit.create(UpdaterAPI::class.java)
    }

    fun getInstaInfo(username: String): Flowable<InstaResult> {
        return startService().instaInfo(username)
    }

    fun getWikiImage(titles: String): Flowable<WikiTitle>{
        return startServiceWiki().wikiImage(titles)
    }

    fun getImageDetails(titles: String): Flowable<WikiTitle>{
        return startServiceWiki().wikiImageInfo(titles)
    }
}