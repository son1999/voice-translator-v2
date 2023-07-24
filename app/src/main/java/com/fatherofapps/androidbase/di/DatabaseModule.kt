package com.fatherofapps.androidbase.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.fatherofapps.androidbase.common.AppSharePreference
import com.fatherofapps.androidbase.data.database.AppDatabase
import com.fatherofapps.androidbase.data.database.daos.CustomerDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {


    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase{
        return Room.databaseBuilder(context,AppDatabase::class.java,"app_db").build()
    }

    @Provides
    fun provideCustomerDao(appDatabase: AppDatabase): CustomerDao{
        return appDatabase.customerDao()
    }

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences{
        return context.getSharedPreferences(AppSharePreference.APP_SHARE_KEY, Context.MODE_PRIVATE)
    }

}