package np.com.sudanchapagain.jsonparser

sealed class JsonValue {
    data class JsonObject(val value: Map<String, JsonValue>) : JsonValue()
    data class JsonArray(val value: List<JsonValue>) : JsonValue()
    data class JsonString(val value: String) : JsonValue()
    data class JsonNumber(val value: Number) : JsonValue()
    data class JsonBoolean(val value: Boolean) : JsonValue()
    object JsonNull : JsonValue()
}
