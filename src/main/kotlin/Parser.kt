package np.com.sudanchapagain.jsonparser

class Parser(private val lexer: Lexer) {
    private var token: Token = lexer.nextToken()

    private fun nextToken() {
        token = lexer.nextToken()
    }

    fun parseJson(): JsonValue {
        return when (token.type) {
            TokenType.LEFT_BRACE -> parseObject()
            TokenType.LEFT_BRACKET -> parseArray()
            else -> throw Exception("Invalid JSON start")
        }
    }

    private fun parseObject(): JsonValue.JsonObject {
        val obj = mutableMapOf<String, JsonValue>()
        nextToken() // skip '{'

        if (token.type == TokenType.RIGHT_BRACE) {
            nextToken()
            return JsonValue.JsonObject(obj)
        }

        while (true) {
            if (token.type != TokenType.STRING) throw Exception("Expected string key in object")
            val key = token.value
            nextToken()

            if (token.type != TokenType.COLON) throw Exception("Expected ':' after key")
            nextToken()

            val value = parseValue()
            obj[key] = value

            when (token.type) {
                TokenType.COMMA -> nextToken()
                TokenType.RIGHT_BRACE -> {
                    nextToken(); break
                }

                else -> throw Exception("Expected ',' or '}' in object")
            }
        }

        return JsonValue.JsonObject(obj)
    }

    private fun parseArray(): JsonValue.JsonArray {
        val arr = mutableListOf<JsonValue>()
        nextToken() // skip '['

        if (token.type == TokenType.RIGHT_BRACKET) {
            nextToken()
            return JsonValue.JsonArray(arr)
        }

        while (true) {
            arr.add(parseValue())
            when (token.type) {
                TokenType.COMMA -> nextToken()
                TokenType.RIGHT_BRACKET -> {
                    nextToken(); break
                }

                else -> throw Exception("Expected ',' or ']' in array")
            }
        }

        return JsonValue.JsonArray(arr)
    }

    private fun parseValue(): JsonValue {
        return when (token.type) {
            TokenType.STRING -> JsonValue.JsonString(token.value).also { nextToken() }
            TokenType.NUMBER -> {
                val str = token.value
                val num: Number = if (str.contains('.') || str.contains('e', ignoreCase = true)) {
                    str.toDouble()
                } else {
                    str.toInt()
                }
                nextToken()
                JsonValue.JsonNumber(num)
            }

            TokenType.BOOLEAN -> {
                val b = token.value == "true"
                nextToken()
                JsonValue.JsonBoolean(b)
            }

            TokenType.NULL -> {
                nextToken(); JsonValue.JsonNull
            }

            TokenType.LEFT_BRACE -> parseObject()
            TokenType.LEFT_BRACKET -> parseArray()
            else -> throw Exception("Unexpected token: ${token.value}")
        }
    }
}
