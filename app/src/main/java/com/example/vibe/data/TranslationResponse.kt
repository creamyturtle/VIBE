package com.example.vibe.data

import kotlinx.serialization.Serializable
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.Response

@Serializable
data class TranslationResponse(val translated_text: String)


interface TranslationApi {
    @GET("get_translation.php")
    suspend fun getTranslation(
        @Query("text") text: String,
        @Query("target_lang") targetLang: String
    ): Response<TranslationResponse>
}
