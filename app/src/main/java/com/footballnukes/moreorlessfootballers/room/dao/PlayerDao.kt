package com.footballnukes.moreorlessfootballers.room.dao

import androidx.lifecycle.LiveData
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

    @Query("SELECT * FROM Player WHERE id > :from")
    fun getAllFrom(from: Int): Single<List<Player>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(players: List<Player>)

    @Query("SELECT count(*) FROM Player")
    fun getCount(): Int


    @Query("SELECT * FROM Player")
    fun jobs(): LiveData<List<Player>>
}