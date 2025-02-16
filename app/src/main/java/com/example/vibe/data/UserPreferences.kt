package com.example.vibe.data

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.*


// ✅ Create a DataStore instance
private val Context.dataStore by preferencesDataStore("user_prefs")

object UserPreferences {
    private val USER_ID_KEY = intPreferencesKey("user_id")
    private val USER_NAME_KEY = stringPreferencesKey("user_name")
    private val USER_EMAIL_KEY = stringPreferencesKey("user_email")
    private val USER_AGE_KEY = intPreferencesKey("user_age")
    private val USER_GENDER_KEY = stringPreferencesKey("user_gender")
    private val USER_FACEBOOK_KEY = stringPreferencesKey("user_facebook")
    private val USER_WHATSAPP_KEY = stringPreferencesKey("user_whatsapp")
    private val USER_INSTAGRAM_KEY = stringPreferencesKey("user_instagram")
    private val USER_BIO_KEY = stringPreferencesKey("user_bio")
    private val USER_PHOTO_URL_KEY = stringPreferencesKey("user_photo_url")
    private val USER_CREATED_AT_KEY = stringPreferencesKey("user_created_at")
    private val USER_IS_VERIFIED_KEY = booleanPreferencesKey("user_is_verified")

    /** ✅ Save User Data */
    suspend fun saveUser(context: Context, userData: MoreUserData) {
        context.dataStore.edit { preferences ->
            preferences[USER_ID_KEY] = userData.id
            preferences[USER_NAME_KEY] = userData.name
            preferences[USER_EMAIL_KEY] = userData.email
            preferences[USER_AGE_KEY] = userData.age
            preferences[USER_GENDER_KEY] = userData.gender
            preferences[USER_FACEBOOK_KEY] = userData.facebook ?: ""
            preferences[USER_WHATSAPP_KEY] = userData.whatsapp ?: ""
            preferences[USER_INSTAGRAM_KEY] = userData.instagram ?: ""
            preferences[USER_BIO_KEY] = userData.bio ?: ""
            preferences[USER_PHOTO_URL_KEY] = userData.photourl ?: ""
            preferences[USER_CREATED_AT_KEY] = userData.created_at
            preferences[USER_IS_VERIFIED_KEY] = userData.is_verified
        }
    }

    /** ✅ Get User Data as a Flow */
    fun getUserFlow(context: Context): Flow<MoreUserData?> {
        return context.dataStore.data.map { preferences ->
            val id = preferences[USER_ID_KEY] ?: -1
            if (id == -1) return@map null

            MoreUserData(
                id = id,
                name = preferences[USER_NAME_KEY] ?: "Unknown",
                email = preferences[USER_EMAIL_KEY] ?: "Unknown",
                age = preferences[USER_AGE_KEY] ?: 0,
                gender = preferences[USER_GENDER_KEY] ?: "Unknown",
                facebook = preferences[USER_FACEBOOK_KEY] ?: "",
                whatsapp = preferences[USER_WHATSAPP_KEY] ?: "",
                instagram = preferences[USER_INSTAGRAM_KEY] ?: "",
                bio = preferences[USER_BIO_KEY] ?: "",
                photourl = preferences[USER_PHOTO_URL_KEY] ?: "",
                created_at = preferences[USER_CREATED_AT_KEY] ?: "",
                is_verified = preferences[USER_IS_VERIFIED_KEY] ?: false
            )
        }
    }

    /** ✅ Clear User Data */
    suspend fun clearUser(context: Context) {
        context.dataStore.edit { it.clear() }
    }
}