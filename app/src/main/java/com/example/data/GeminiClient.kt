package com.example.data

import android.util.Log
import com.example.BuildConfig
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

object GeminiClient {
    private const val TAG = "GeminiClient"
    private const val MODEL = "gemini-3.5-flash"
    
    private val client = OkHttpClient.Builder()
        .connectTimeout(25, TimeUnit.SECONDS)
        .readTimeout(25, TimeUnit.SECONDS)
        .writeTimeout(25, TimeUnit.SECONDS)
        .build()

    suspend fun getConciergeResponse(
        systemInstruction: String, 
        history: List<Pair<String, String>>, 
        userPrompt: String
    ): String = withContext(Dispatchers.IO) {
        val apiKey = BuildConfig.GEMINI_API_KEY
        if (apiKey.isEmpty() || apiKey == "MY_GEMINI_API_KEY") {
            return@withContext """
                Dear AuraStay Guest, 
                
                Our server-side AI Concierge is currently offline (API key placeholder detected). 
                
                However, I can share some instant pre-loaded luxury suggestions:
                • To order dining, go to "Room Services" and tap "Order Dining" (we recommend the Prime Tenderloin Steak!)
                • To request cleaning, select "Clean Request" under Housekeeping.
                • Our spa is open daily from 8:00 AM to 10:00 PM on the Penthouse level.
                
                Please enter a real Gemini API Key in the AI Studio Secrets panel to enable full natural language chat capabilities. Let me know if I can help you with anything else!
            """.trimIndent()
        }

        // Build contents list
        val contentsJson = StringBuilder("[")
        // Add chat history
        history.forEach { (role, text) ->
            val cleanText = escapeJsonString(text)
            contentsJson.append("{\"role\":\"$role\",\"parts\":[{\"text\":\"$cleanText\"}]},")
        }
        // Add current user prompt
        val escapedPrompt = escapeJsonString(userPrompt)
        contentsJson.append("{\"role\":\"user\",\"parts\":[{\"text\":\"$escapedPrompt\"}]}")
        contentsJson.append("]")

        val systemEscaped = escapeJsonString(systemInstruction)

        // API dynamic request body
        val jsonPayload = """
            {
              "contents": $contentsJson,
              "systemInstruction": {
                "role": "system",
                "parts": [
                  { "text": "$systemEscaped" }
                ]
              },
              "generationConfig": {
                "temperature": 0.7,
                "topP": 0.95
              }
            }
        """.trimIndent()

        val mediaType = "application/json; charset=utf-8".toMediaType()
        val body = jsonPayload.toRequestBody(mediaType)
        
        val url = "https://generativelanguage.googleapis.com/v1beta/models/$MODEL:generateContent?key=$apiKey"
        
        val request = Request.Builder()
            .url(url)
            .post(body)
            .build()

        try {
            client.newCall(request).execute().use { response ->
                val bodyString = response.body?.string() ?: ""
                if (!response.isSuccessful) {
                    Log.e(TAG, "Request failed code: ${response.code}, body: $bodyString")
                    return@withContext "I apologize, Guest! I had trouble accessing our main database services (Error code ${response.code}). Please let me know how I can assist you with local room services!"
                }

                val textResponse = extractTextFromResponse(bodyString)
                if (textResponse != null) {
                    return@withContext textResponse
                } else {
                    Log.e(TAG, "No text content found: $bodyString")
                    return@withContext "I received an empty reservation packet from my concierge stream, but I would be glad to help you schedule dining or check status."
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Network connection error in Concierge", e)
            return@withContext "Network delay detected. I am currently operating in offline-first mode to preserve your luxury experience. Feel free to request room features locally!"
        }
    }

    private fun escapeJsonString(text: String): String {
        return text.replace("\\", "\\\\")
            .replace("\"", "\\\"")
            .replace("\n", "\\n")
            .replace("\r", "\\r")
            .replace("\t", "\\t")
    }

    private fun extractTextFromResponse(json: String): String? {
        try {
            val textToken = "\"text\":"
            var index = json.indexOf(textToken)
            if (index == -1) return null
            
            index = json.indexOf("\"", index + textToken.length)
            if (index == -1) return null
            
            val start = index + 1
            var end = start
            while (end < json.length) {
                if (json[end] == '"' && json[end - 1] != '\\') {
                    break
                }
                end++
            }
            if (end == json.length) return null
            
            val rawResult = json.substring(start, end)
            return rawResult.replace("\\n", "\n")
                .replace("\\\"", "\"")
                .replace("\\\\", "\\")
        } catch (e: Exception) {
            Log.e(TAG, "Tokens parsing exception", e)
        }
        return null
    }
}
