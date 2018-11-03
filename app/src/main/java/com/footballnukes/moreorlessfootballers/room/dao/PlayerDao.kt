package com.footballnukes.moreorlessfootballers.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.footballnukes.moreorlessfootballers.game.Player
import io.reactivex.Single

/**
 * Created by musooff on 03/11/2018.
 */

@Dao
interface PlayerDao {
    @Query("SELECT * FROM Player")
    fun getAll(): Single<List<Player>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(players: List<Player>)

    @Query("SELECT count(*) FROM Player")
    fun getCount(): Int
}