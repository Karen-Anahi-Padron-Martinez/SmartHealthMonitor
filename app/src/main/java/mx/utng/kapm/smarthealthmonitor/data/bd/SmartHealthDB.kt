package mx.utng.kapm.smarthealthmonitor.data.db

import android.content.Context
import androidx.room.*
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [LecturaFC::class],
    version = 2,
    exportSchema = false
)
abstract class SmartHealthDB : RoomDatabase() {
    abstract fun lecturaDao(): LecturaFCDao

    companion object {
        @Volatile
        private var INSTANCE: SmartHealthDB? = null

        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("DROP TABLE IF EXISTS lecturas_fc")
                db.execSQL("""
                    CREATE TABLE IF NOT EXISTS lecturas_fc (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        bpm INTEGER NOT NULL,
                        estado TEXT NOT NULL,
                        dispositivo TEXT NOT NULL,
                        hora TEXT NOT NULL,
                        sincronizado INTEGER NOT NULL DEFAULT 0
                    )
                """.trimIndent())
            }
        }

        fun getDatabase(context: Context): SmartHealthDB {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    SmartHealthDB::class.java,
                    "smarthealthmonitor_db"
                )
                .addMigrations(MIGRATION_1_2)
                .fallbackToDestructiveMigration()
                .build().also { INSTANCE = it }
            }
        }
    }
}