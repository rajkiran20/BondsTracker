package app.le.bondstracker.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import app.le.bondstracker.data.local.dao.BondDao
import app.le.bondstracker.data.local.entity.BondEntity
import app.le.bondstracker.data.local.entity.PayoutEntity

@Database(
    entities = [BondEntity::class, PayoutEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun bondDao(): BondDao
}
