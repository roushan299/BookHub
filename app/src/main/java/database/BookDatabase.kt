package database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.roushan.bookhub.Book


@Database(entities = [BookEntity::class], version = 1)
abstract class BookDatabase :RoomDatabase(){

    abstract fun bookDao(): BookDao
}