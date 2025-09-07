import np.com.sudanchapagain.jsonparser.JsonValue
import np.com.sudanchapagain.jsonparser.Lexer
import np.com.sudanchapagain.jsonparser.Parser

val jsonInput = """
        {
            "name": "Nepal",
            "age": 0,
            "country": true,
            "districts": ["Kathmandu", "Lalitpur"],
            "languages": ["Nepali", "Maithili", "Tharu"],
            "address": {
                "continent": "Asia",
                "location": "South Asia",
                "neighbors": ["India", "China", "Bangladesh"]
            },
            "currency": null
        }
    """.trimIndent()

fun main() {
    val lexer = Lexer(jsonInput)
    val parser = Parser(lexer)
    val result: JsonValue = parser.parseJson()

    val indent = 4
    prettyPrint(result)
}

private fun prettyPrint(value: JsonValue, indent: Int = 0) {
    val indentStr = "    "
    val currentIndent = indentStr.repeat(indent)

    when (value) {
        is JsonValue.JsonObject -> {
            if (value.value.isEmpty()) {
                print("{}")
                return
            }
            println("{")

            val lastKey = value.value.keys.last()
            for ((key, v) in value.value) {
                print(indentStr.repeat(indent + 1) + "\"$key\": ")
                prettyPrint(v, indent + 1)

                if (key != lastKey) {
                    println(",")
                } else {
                    println()
                }
            }
            print("$currentIndent}")
        }

        is JsonValue.JsonArray -> {
            if (value.value.isEmpty()) {
                print("[]")
                return
            }
            println("[")

            val lastIndex = value.value.size - 1
            value.value.forEachIndexed { i, item ->
                print(indentStr.repeat(indent + 1))
                prettyPrint(item, indent + 1)

                if (i != lastIndex) {
                    println(",")
                } else {
                    println()
                }
            }
            print("$currentIndent]")
        }

        is JsonValue.JsonString -> print("\"${value.value.replace("\"", "\\\"")}\"")
        is JsonValue.JsonNumber -> print(value.value)
        is JsonValue.JsonBoolean -> print(if (value.value) "true" else "false")
        JsonValue.JsonNull -> print("null")
    }
}
