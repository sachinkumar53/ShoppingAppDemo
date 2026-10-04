package com.sachin.shopping.demo.data.local

import androidx.room3.Database
import androidx.room3.RoomDatabase
import com.sachin.shopping.demo.data.local.dao.CartItemDao
import com.sachin.shopping.demo.data.local.dao.ProductDao
import com.sachin.shopping.demo.data.local.dao.ProductRemoteKeyDao
import com.sachin.shopping.demo.data.local.entity.CartItemEntity
import com.sachin.shopping.demo.data.local.entity.ProductEntity
import com.sachin.shopping.demo.data.local.entity.ProductRemoteKey

@Database(
    entities = [
        CartItemEntity::class,
        ProductEntity::class,
        ProductRemoteKey::class
    ],
    version = 2,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun cartItemDao(): CartItemDao
    abstract fun productDao(): ProductDao
    abstract fun remoteKeyDao(): ProductRemoteKeyDao
}
