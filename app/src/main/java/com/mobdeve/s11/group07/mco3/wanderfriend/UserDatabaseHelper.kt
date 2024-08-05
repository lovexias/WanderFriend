package com.mobdeve.s11.group07.mco3.wanderfriend

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class UserDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        val createUsersTableStatement = """
            CREATE TABLE $TABLE_USERS (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, 
                $COLUMN_NAME TEXT, 
                $COLUMN_AGE INTEGER, 
                $COLUMN_COUNTRY TEXT, 
                $COLUMN_TRAVELED_COUNTRIES TEXT
            )
        """
        db.execSQL(createUsersTableStatement)

        val createCountryTableStatement = """
            CREATE TABLE $TABLE_COUNTRIES (
                $COLUMN_COUNTRY_ID INTEGER PRIMARY KEY AUTOINCREMENT,  -- PK for Country
                $COLUMN_COUNTRY_NAME TEXT,
                $COLUMN_COUNTRY_FLAGS TEXT
            )
        """
        db.execSQL(createCountryTableStatement)

        val createCountryLogTableStatement = """
            CREATE TABLE $TABLE_COUNTRY_LOG (
                $COLUMN_LOG_ID INTEGER PRIMARY KEY AUTOINCREMENT,  -- PK for Log
                $COLUMN_LOG_COUNTRY_ID INTEGER,  -- FK referencing CountryID
                $COLUMN_LOG_DATE TEXT, 
                $COLUMN_LOG_CAPTION TEXT, 
                $COLUMN_LOG_PHOTO_URI TEXT,
                FOREIGN KEY($COLUMN_LOG_COUNTRY_ID) REFERENCES $TABLE_COUNTRIES($COLUMN_COUNTRY_ID)  -- Foreign Key Constraint
            )
        """
        db.execSQL(createCountryLogTableStatement)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_COUNTRIES")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_COUNTRY_LOG")
        onCreate(db)
    }

    fun addUser(user: User): Boolean {
        val db = this.writableDatabase
        return try {
            // Add traveled countries to the Country table
            user.traveledCountries.forEach { country ->
                addOrUpdateCountry(country)
            }

            val values = ContentValues().apply {
                put(COLUMN_NAME, user.name)
                put(COLUMN_AGE, user.age)
                put(COLUMN_COUNTRY, user.country)
                put(COLUMN_TRAVELED_COUNTRIES, Gson().toJson(user.traveledCountries))
            }

            val success: Long = if (isUserExists(db)) {
                db.update(TABLE_USERS, values, "$COLUMN_ID = ?", arrayOf(user.id.toString())).toLong()
            } else {
                db.insert(TABLE_USERS, null, values)
            }
            Log.d("UserDatabaseHelper", "User added/updated: ${user.name}, ${user.age}, ${user.country}, ${user.traveledCountries.map { it.name.common }}, Success: $success")
            success != -1L
        } catch (e: Exception) {
            Log.e("UserDatabaseHelper", "Error adding/updating user", e)
            false
        } finally {
            db.close()
        }
    }

    fun getUser(): User? {
        val db = this.readableDatabase
        return try {
            val query = "SELECT * FROM $TABLE_USERS LIMIT 1"
            val cursor = db.rawQuery(query, null)
            var user: User? = null
            if (cursor.moveToFirst()) {
                val id = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_ID))
                val name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME))
                val age = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_AGE))
                val country = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_COUNTRY))
                val traveledCountriesJson = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TRAVELED_COUNTRIES))
                val traveledCountriesType = object : TypeToken<List<Country>>() {}.type
                val traveledCountries: List<Country> = Gson().fromJson(traveledCountriesJson, traveledCountriesType)
                user = User(id, name, age, country, traveledCountries)
                Log.d("UserDatabaseHelper", "User retrieved: ${user.name}, ${user.age}, ${user.country}, ${user.traveledCountries.map { it.name.common }}")
            }
            cursor.close()
            user
        } catch (e: Exception) {
            Log.e("UserDatabaseHelper", "Error retrieving user", e)
            null
        } finally {
            db.close()
        }
    }

    // Add or update country in the Country table
    fun addOrUpdateCountry(country: Country): Long {
        val db = this.writableDatabase
        val existingCountry = getCountryByName(country.name.common)

        // If the country already exists, update it
        if (existingCountry != null) {
            Log.d("UserDatabaseHelper", "Country already exists: ${country.name.common}")
            return existingCountry.countryId
        }

        // Otherwise, insert the new country
        val values = ContentValues().apply {
            put(COLUMN_COUNTRY_NAME, country.name.common)
            put(COLUMN_COUNTRY_FLAGS, country.flags.png)
        }

        val countryId = db.insert(TABLE_COUNTRIES, null, values)
        Log.d("UserDatabaseHelper", "Country added: ${country.name.common} with Country ID: $countryId")
        return countryId
    }

    // Retrieve a country by name from the Country table
    fun getCountryByName(countryName: String): Country? {
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_COUNTRIES WHERE $COLUMN_COUNTRY_NAME = ?"
        val cursor: Cursor = db.rawQuery(query, arrayOf(countryName))
        return if (cursor.moveToFirst()) {
            val countryId = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_COUNTRY_ID))
            val name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_COUNTRY_NAME))
            val flags = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_COUNTRY_FLAGS))
            cursor.close()
            Country(countryId, Name(name), Flags(flags))
        } else {
            cursor.close()
            null
        }
    }

    // Ensure that the log is associated with the correct country ID
    fun addLog(log: CountryLog): Boolean {
        val db = this.writableDatabase
        return try {
            val values = ContentValues().apply {
                put(COLUMN_LOG_COUNTRY_ID, log.countryId)  // Use the countryId as foreign key
                put(COLUMN_LOG_DATE, log.date)
                put(COLUMN_LOG_CAPTION, log.caption)
                put(COLUMN_LOG_PHOTO_URI, log.photoUri)
            }
            val success = db.insert(TABLE_COUNTRY_LOG, null, values)
            Log.d("UserDatabaseHelper", "Log added: $log, Success: $success")
            Log.d("UserDatabaseHelper", "Log added with countryId: ${log.countryId}")  // Debug log
            success != -1L
        } catch (e: Exception) {
            Log.e("UserDatabaseHelper", "Error adding log", e)
            false
        } finally {
            db.close()
        }
    }

    // Retrieve logs specifically for the given country ID
    fun getLogsForCountry(countryId: Long): List<CountryLog> {
        val db = this.readableDatabase
        val logs = mutableListOf<CountryLog>()
        return try {
            val query = "SELECT * FROM $TABLE_COUNTRY_LOG WHERE $COLUMN_LOG_COUNTRY_ID = ?"
            val cursor = db.rawQuery(query, arrayOf(countryId.toString()))  // Query by countryId
            Log.d("UserDatabaseHelper", "Fetching logs for countryId: $countryId")  // Debug log
            if (cursor.moveToFirst()) {
                do {
                    val id = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_LOG_ID))
                    val date = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LOG_DATE))
                    val caption = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LOG_CAPTION))
                    val photoUri = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LOG_PHOTO_URI))
                    logs.add(CountryLog(id, countryId, date, caption, photoUri))
                    Log.d("UserDatabaseHelper", "Retrieved Log ID: $id for Country ID: $countryId")  // Debug log
                } while (cursor.moveToNext())
            }
            cursor.close()
            logs
        } catch (e: Exception) {
            Log.e("UserDatabaseHelper", "Error retrieving logs", e)
            logs
        } finally {
            db.close()
        }
    }


    fun getAllCountries(): List<Country> {
        val db = this.readableDatabase
        val countries = mutableListOf<Country>()
        val query = "SELECT * FROM $TABLE_COUNTRIES"
        val cursor = db.rawQuery(query, null)

        if (cursor.moveToFirst()) {
            do {
                val countryId = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_COUNTRY_ID))
                val name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_COUNTRY_NAME))
                val flags = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_COUNTRY_FLAGS))
                countries.add(Country(countryId, Name(name), Flags(flags)))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return countries
    }

    private fun isUserExists(db: SQLiteDatabase): Boolean {
        val query = "SELECT * FROM $TABLE_USERS"
        val cursor = db.rawQuery(query, null)
        val exists = cursor.count > 0
        cursor.close()
        return exists
    }

    companion object {
        private const val DATABASE_VERSION = 52
        private const val DATABASE_NAME = "userDatabase"
        const val TABLE_USERS = "users"
        const val COLUMN_ID = "id"
        const val COLUMN_NAME = "name"
        const val COLUMN_AGE = "age"
        const val COLUMN_COUNTRY = "country"
        const val COLUMN_TRAVELED_COUNTRIES = "traveledCountries"

        const val TABLE_COUNTRIES = "countries"
        const val COLUMN_COUNTRY_ID = "countryId"
        const val COLUMN_COUNTRY_NAME = "countryName"
        const val COLUMN_COUNTRY_FLAGS = "countryFlags"

        const val TABLE_COUNTRY_LOG = "CountryLog"
        const val COLUMN_LOG_ID = "logId"
        const val COLUMN_LOG_COUNTRY_ID = "logCountryId"
        const val COLUMN_LOG_DATE = "logDate"
        const val COLUMN_LOG_CAPTION = "logCaption"
        const val COLUMN_LOG_PHOTO_URI = "logPhotoUri"
    }
}
