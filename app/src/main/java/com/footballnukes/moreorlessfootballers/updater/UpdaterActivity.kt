package com.footballnukes.moreorlessfootballers.updater

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.util.Pair
import android.view.Window
import android.widget.TextView
import android.widget.Toast
import com.footballnukes.moreorlessfootballers.R
import com.footballnukes.moreorlessfootballers.beautifiers.GameButton
import com.footballnukes.moreorlessfootballers.game.Player
import com.footballnukes.moreorlessfootballers.room.AppDatabase
import com.footballnukes.moreorlessfootballers.room.dao.PlayerDao
import com.footballnukes.moreorlessfootballers.updater.model.InstaResult
import com.footballnukes.moreorlessfootballers.updater.network.UpdaterService
import com.google.firebase.database.FirebaseDatabase
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_updater.*
import org.jsoup.Jsoup
import android.os.AsyncTask
import com.footballnukes.moreorlessfootballers.updater.model.WikiTitle
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import io.reactivex.Completable
import io.reactivex.Observable
import org.apache.commons.lang3.StringUtils
import org.intellij.lang.annotations.Flow
import org.json.JSONObject
import org.jsoup.nodes.Document
import java.io.BufferedInputStream
import java.io.IOException
import java.lang.IllegalStateException
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLDecoder
import java.util.concurrent.Callable


/**
 * Created by musooff on 04/11/2018.
 */


class UpdaterActivity: Activity() {

    companion object {
        const val TRANSFERMARKT_URL = "http://www.transfermarkt.com/spieler-statistik/wertvollstespieler/marktwertetop?land_id=0&ausrichtung=alle&spielerposition_id=alle&altersklasse=alle&plus=1&page="

    }

    private var transferPageCount = 1
    private var fromId = 0
    private var fromName = ""
    private var errorLog: HashMap<Int, String> = hashMapOf()
    private lateinit var playerDao: PlayerDao

    private var players: MutableList<Player> = mutableListOf()

    private val mRootRef = FirebaseDatabase.getInstance().reference
    private val playersRef = mRootRef.child("totally_players")
    private val newPlayersRef = mRootRef.child("name_players")

    private val disposable = CompositeDisposable()

    private lateinit var dialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_updater)

        playerDao = AppDatabase.getInstance(applicationContext)!!.playerDao()

        get_players.setOnClickListener{
            getPlayers()
        }
        check_if_wrong.setOnClickListener{
            checkIfWrong()
        }

        insta_updater.setOnClickListener {
            now_updating.text = "Updating Instagram"
            updateInsta()
        }

        transfer_updater.setOnClickListener{
            now_updating.text = "Updating TransferMark"
            updateTranferMarkt(TRANSFERMARKT_URL + transferPageCount)

        }

        error_log.setOnClickListener{
            if (!errorLog.isEmpty()) showErrorLog()
            else Toast.makeText(this, "No error found", Toast.LENGTH_SHORT).show()
        }
        skip.setOnClickListener {
            fromId++
        }

        wiki_images.setOnClickListener {
            getWikiImageInfos()
        }
    }

    private fun getWikiImageInfos(){
        newPlayersRef.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                players = mutableListOf()
                for (child: DataSnapshot in p0.children){
                    players.add(child.getValue(Player::class.java)!!)
                }
                disposable.add(Flowable.just(players)
                        .subscribeOn(Schedulers.io())
                        .flatMap { players -> Flowable.fromIterable(players) }
                        .filter { player -> player.wikipediaImageUrl != null }
                        .flatMap ({ player -> UpdaterService().getImageDetails(getImageTitle(player.wikipediaImageUrl!!))},
                                {player, wikiTitle -> Pair(player, wikiTitle)})
                        .subscribe {
                            val extmetadata = it.second.query!!.pages!!.values.toMutableList()[0].imageinfo[0].extmetadata
                            val player = it.first
                            fromId = player.id
                            fromName = player.name!!
                            updateInstaLog()
                            newPlayersRef.child(player.name!!).child("licenceUrl").setValue(extmetadata!!.LicenseUrl?.value)
                            newPlayersRef.child(player.name!!).child("licenseShortName").setValue(extmetadata.LicenseShortName?.value)
                            newPlayersRef.child(player.name!!).child("artist").setValue(getImageCredits(extmetadata.Artist?.value))
                            newPlayersRef.child(player.name!!).child("credit").setValue(getImageCredits(extmetadata.Credit?.value))
                        }
                )

            }
        })
    }

    private fun getImageTitle(url: String): String{
        var result = url.substring(url.lastIndexOf('/')+1)
        result = URLDecoder.decode(result, "utf-8")
        return "File:$result"
    }

    private fun getImageCredits(url: String?): String?{
        return when {
            url!!.startsWith("<a") -> Jsoup.parse(url).select("a").first().text()
            url.startsWith("<span") -> Jsoup.parse(url).select("span").first().text()
            else -> url
        }
    }

    private fun getWikiImages(){
        newPlayersRef.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                players = mutableListOf()
                for (child: DataSnapshot in p0.children){
                    players.add(child.getValue(Player::class.java)!!)
                }
                disposable.add(Flowable.just(players)
                        .subscribeOn(Schedulers.io())
                        .flatMap { players -> Flowable.fromIterable(players) }
                        .flatMap ({ player -> UpdaterService().getWikiImage(player.wikiName!!)},
                            {player, wikiTitle -> Pair(player, wikiTitle)})
                        .subscribe {
                            val wikipediaImageUrl = it.second.query!!.pages!!.values.toMutableList()[0].original?.source
                            val player = it.first
                            fromId = player.id
                            fromName = player.name!!
                            updateInstaLog()
                            newPlayersRef.child(player.name!!).child("wikipediaImageUrl").setValue(wikipediaImageUrl)
                        }
                )

            }
        })
    }



    private fun getWikiSubUrl(){
        newPlayersRef.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onCancelled(dataSnapshot: DatabaseError) {
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                players = mutableListOf()
                for (child: DataSnapshot in dataSnapshot.children){
                    players.add(child.getValue(Player::class.java)!!)
                }
                disposable.add(Observable.just(players)
                        .subscribeOn(Schedulers.io())
                        .flatMap { players: MutableList<Player> -> Observable.fromIterable(players) }
                        .flatMap( { player: Player -> googleSearch(player.name!!.replace(" ", "+"))},
                                {player: Player, wiki: String -> Pair(player, wiki)}
                        )

                        .subscribe{ pair ->
                            val wiki = pair.second
                            val player = pair.first
                            fromId = player.id
                            fromName = player.name!!
                            updateInstaLog()
                            newPlayersRef.child(player.name!!).child("wikiName").setValue(wikipediaTitle(wiki))
                        }
                )
            }
        })
    }


    private fun wikipediaTitle(total: String): String {
        var result = total.substring(0, total.indexOf("- Wikipedia") - 1)
        result = result.replace(" ", "_")
        return result
    }

    private fun updateInsta(){
        disposable.add(Flowable.just(players.subList(fromId, 499))
                .subscribeOn(Schedulers.io())
                .flatMap { playerList: MutableList<Player> -> Flowable.fromIterable(playerList)}
                .filter { player -> player.instagramId != null }
                .flatMap( { player: Player -> UpdaterService().getInstaInfo(player.instagramId!!)},
                        {player: Player, instaResult: InstaResult -> Pair(player, instaResult)})
                .doOnComplete{
                    now_updating.text = "Instagram has been updated"
                    player.text = ""
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { pair ->
                            if (pair.second.success){
                                newPlayersRef.child(pair.first.name!!).child("instagramFollowers")
                                        .setValue(longOf(pair.second.data!!.followers!!))
                                fromId ++
                                fromName = pair.first.name!!
                                updateInstaLog()

                            }
                            else{
                                errorLog[pair.first.id] = pair.first.name!!
                            }


                        },
                        {
                            updateInstaLog(true)
                        }))
    }

    private fun getPlayers(){
        newPlayersRef.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                players = mutableListOf()
                for (child: DataSnapshot in dataSnapshot.children){
                    players.add(child.getValue(Player::class.java)!!)
                }
                now_updating.text = "Players have been downloaded"
            }

        })
    }

    private fun longOf(numberStr: String): Long{
        return numberStr.replace(",", "").toLong()
    }

    private fun updateInstaLog(error: Boolean = false) {
        if (!error) player.text = "$fromId. "+fromName
        else player.text = "Error at $fromId "+fromName
    }



    private fun showErrorLog(){
        dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.error_dialog)
        val text = dialog.findViewById(R.id.errors) as TextView
        var errors = ""
        for (id: Int in errorLog.keys){
            errors += "$id. " + errorLog[id]+"\n"
        }
        text.text = errors

        val dialogButton = dialog.findViewById(R.id.okay) as GameButton
        dialogButton.setOnClickListener{
            dialog.dismiss()
        }
        dialog.show()
    }

    override fun onBackPressed() {
        if (::dialog.isInitialized && dialog.isShowing){
            dialog.dismiss()
        }
        disposable.clear()
        disposable.dispose()
        super.onBackPressed()
    }

    private fun googleInstaIds(){
        mRootRef.child("totally_players").addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onCancelled(dataSnapshot: DatabaseError) {
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                players = mutableListOf()
                for (child: DataSnapshot in dataSnapshot.children){
                    players.add(child.getValue(Player::class.java)!!)
                }
                disposable.add(Observable.just(players)
                        .subscribeOn(Schedulers.io())
                        .flatMap { players: MutableList<Player> -> Observable.fromIterable(players) }
                        .flatMap( { player: Player -> googleSearch(player.name!!.replace(" ", "+"))},
                                {player: Player, username: String -> Pair(player, username)}
                        )

                        .subscribe{ pair ->
                            val username = pair.second
                            val player = pair.first
                            fromId = player.id
                            fromName = player.name!!
                            updateInstaLog()
                            if (username.contains("Instagram photos") || username.contains("Instagram 사진")){
                                playersRef.child(player.id.toString()).child("instagramId").setValue(extractInstagUsername(username))
                            }
                        }
                )
            }
        })
    }


    private fun googleSearch(name: String): Observable<String> {
        return Observable.fromCallable {
            val strUrl = "https://www.google.co.uk/search?q=$name+wikipedia"
            val doc = Jsoup.connect(strUrl)
                    .header("Accept-Language", "en").get()
            val table = doc!!.select("h3")
            table.first().text()
        }
    }

    private fun extractInstagUsername(title: String): String? {
        var string: String? = null
        try {
            string = StringUtils.substringBetween(title, "@", ")")
        }catch (e: IllegalStateException){
            Log.e("error", title)
        }
        return string
    }

    private fun updateTranferMarkt(url: String){
        disposable.add(
                Observable.fromCallable {
                    Jsoup.connect(url).get()
                }
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnComplete{
                            now_updating.text = "TransferMarkt has been updated"
                            player.text = ""
                        }
                        .subscribe{doc ->
                            player.text = "Updating page $transferPageCount"

                            val table = doc.select("a[href=\"#\"]")
                            //val tableClub = doc.select("a.vereinprofil_tooltip")
                            val tableValue = doc.select("td.rechts")
                            //val tablePos = doc.select("table.inline-table")
                            val tableAge = doc.select("tbody").select("td.zentriert")

                            for (i in 0..24){
                                val player = Player()
                                player.name = table.select("img")[i].attr("title")
                                player.age = tableAge[i*13+1].text().toInt()
                                player.appearance = tableAge[i*13+4].text().toInt()
                                player.goals = tableAge[i*13+5].text().toInt()
                                //player.position = tablePos[i].select("tr")[1].text()
                                //player.club = tableClub.select("img")[i].attr("alt")
                                player.value = valueToLong(tableValue.select("b")[i].text())
                                players.add(player)
                            }
                            if (transferPageCount == 20){
                                for (player: Player in players){
                                    newPlayersRef.child(player.name!!).child("age").setValue(player.age)
                                    newPlayersRef.child(player.name!!).child("appearance").setValue(player.appearance)
                                    newPlayersRef.child(player.name!!).child("goals").setValue(player.goals)
                                    newPlayersRef.child(player.name!!).child("value").setValue(player.value)
                                }
                                return@subscribe
                            }
                            transferPageCount++
                            updateTranferMarkt(TRANSFERMARKT_URL+transferPageCount)

                        }
        )
    }

    private fun valueToLong(string: String): Long {
        var string = StringUtils.substringBefore(string, ",")
        string += "000000"
        return string.toLong()
    }

    private fun checkIfWrong(){
        newPlayersRef.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                players = mutableListOf()
                for (child: DataSnapshot in p0.children){
                    players.add(child.getValue(Player::class.java)!!)
                }

                for(player: Player in players){
                    if (player.instagramFollowers < 10000){
                        errorLog[player.id] = player.name!!
                    }
                }

            }

        })
    }


}