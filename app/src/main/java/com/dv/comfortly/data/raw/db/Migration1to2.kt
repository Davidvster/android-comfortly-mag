package com.dv.comfortly.data.raw.db

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

class Migration1to2 : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.beginTransaction()
        database.execSQL("""ALTER TABLE trip ADD COLUMN start_date INTEGER DEFAULT NULL""")
        database.execSQL("""ALTER TABLE trip ADD COLUMN end_date INTEGER DEFAULT NULL""")
        database.execSQL("""COMMIT;""")
    }
}
