package com.sachin.shopping.demo.data.local

import androidx.room3.Database
import androidx.room3.RoomDatabase
import com.sachin.shopping.demo.data.local.dao.CartItemDao
import com.sachin.shopping.demo.data.local.entity.CartItemEntity

@Database(
    entities = [
        CartItemEntity::class,
    ],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun cartItemDao(): CartItemDao
}