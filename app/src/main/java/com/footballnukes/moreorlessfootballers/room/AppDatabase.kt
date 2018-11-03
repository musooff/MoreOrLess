package com.footballnukes.moreorlessfootballers.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.footballnukes.moreorlessfootballers.game.Player
import com.footballnukes.moreorlessfootballers.room.dao.PlayerDao

/**
 * Created by musooff on 03/11/2018.
 */
@Database(entities = [Player::class], version = 1)
abstract class AppDatabase: RoomDatabase() {

    abstract fun playerDao(): PlayerDao

    companion object {
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase?{

            if (INSTANCE == null){
                INSTANCE = Room.databaseBuilder(context,AppDatabase::class.java, "app_database")
                        .build()
            }
            return INSTANCE
        }
    }
}