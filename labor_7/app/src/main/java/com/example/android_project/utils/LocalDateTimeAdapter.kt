package com.example.android_project.utils

import com.google.gson.*
import java.lang.reflect.Type
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class LocalDateTimeAdapter : JsonDeserializer<LocalDateTime>, JsonSerializer<LocalDateTime> {

    override fun deserialize(
        json: JsonElement,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): LocalDateTime? {
        if (json.isJsonNull || json.asString.isNullOrEmpty()) {
            return null
        }
        val dateString = json.asString
        return try {
            LocalDateTime.parse(dateString, DateTimeFormatter.ISO_DATE_TIME)
        } catch (e: Exception) {
            try {
                java.time.Instant.parse(dateString).atZone(java.time.ZoneId.systemDefault()).toLocalDateTime()
            } catch (e2: Exception) {
                try {
                    LocalDateTime.parse(dateString.replace("Z", ""))
                } catch (e3: Exception) {
                    e3.printStackTrace()
                    null
                }
            }
        }
    }

    override fun serialize(
        src: LocalDateTime,
        typeOfSrc: Type,
        context: JsonSerializationContext
    ): JsonElement {
        return JsonPrimitive(src.toString())
    }
}
