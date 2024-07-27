package com.mobdeve.s11.group07.mco3.wanderfriend

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class UserDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        val createTableStatement = "CREATE TABLE $TABLE_USERS (" +
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_NAME TEXT, " +
                "$COLUMN_AGE INTEGER, " +
                "$COLUMN_COUNTRY TEXT, " +
                "$COLUMN_TRAVELED_COUNTRIES TEXT)"
        db.execSQL(createTableStatement)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        onCreate(db)
    }

    fun addUser(user: User): Boolean {
        val db = this.writableDatabase
        return try {
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

    private fun isUserExists(db: SQLiteDatabase): Boolean {
        val query = "SELECT * FROM $TABLE_USERS"
        val cursor = db.rawQuery(query, null)
        val exists = cursor.count > 0
        cursor.close()
        return exists
    }

    companion object {
        private const val DATABASE_VERSION = 17
        private const val DATABASE_NAME = "userDatabase"
        const val TABLE_USERS = "users"
        const val COLUMN_ID = "id"
        const val COLUMN_NAME = "name"
        const val COLUMN_AGE = "age"
        const val COLUMN_COUNTRY = "country"
        const val COLUMN_TRAVELED_COUNTRIES = "traveledCountries"
    }
}
