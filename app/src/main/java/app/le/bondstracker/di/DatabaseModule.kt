package app.le.bondstracker.di

import android.content.Context
import androidx.room.Room
import app.le.bondstracker.data.local.AppDatabase
import app.le.bondstracker.data.local.dao.BondDao
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "bonds_tracker.db"
        ).fallbackToDestructiveMigration().build()

    @Provides
    @Singleton
    fun provideBondDao(db: AppDatabase): BondDao = db.bondDao()

    @Provides
    @Singleton
    fun provideGson(): Gson = Gson()
}
