package com.sachin.shopping.demo.data.di

import android.content.Context
import androidx.room3.Room
import com.sachin.shopping.demo.data.local.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "app-database.db"
        )
            .fallbackToDestructiveMigration() // STOPSHIP: Remove this line in production, as it will delete the database on schema changes
            .build()
    }

    @Singleton
    @Provides
    fun provideCartItemDao(appDatabase: AppDatabase) = appDatabase.cartItemDao()

    @Singleton
    @Provides
    fun provideProductDao(appDatabase: AppDatabase) = appDatabase.productDao()
}
