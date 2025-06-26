package np.com.sudanchapagain.jsonparser

class Lexer(private val input: String) {
    private var pos = 0
    private var current: Char? = input.getOrNull(pos)

    private fun advance() {
        pos++
        current = if (pos < input.length) input[pos] else null
    }

    private fun skipWhitespace() {
        while (current != null && current!!.isWhitespace()) {
            advance()
        }
    }

    fun nextToken(): Token {
        skipWhitespace()
        current ?: return Token(TokenType.EOF, "")

        return when (current) {
            '{' -> {
                advance()
                Token(TokenType.LEFT_BRACE, "{")
            }

            '}' -> {
                advance()
                Token(TokenType.RIGHT_BRACE, "}")
            }

            '[' -> {
                advance()
                Token(TokenType.LEFT_BRACKET, "[")
            }

            ']' -> {
                advance()
                Token(TokenType.RIGHT_BRACKET, "]")
            }

            ':' -> {
                advance()
                Token(TokenType.COLON, ":")
            }

            ',' -> {
                advance()
                Token(TokenType.COMMA, ",")
            }

            '"' -> readString()

            in '0'..'9', '-' -> readNumber()

            else -> if (current!!.isLetter()) {
                readKeyword()
            } else {
                throw Exception("Unexpected character: $current")
            }
        }
    }

    private fun readString(): Token {
        advance() // skip opening
        val sb = StringBuilder()

        while (current != null && current != '"') {
            sb.append(current)
            advance()
        }

        if (current != '"') throw Exception("Unterminated string")
        advance() // skip closing

        return Token(TokenType.STRING, sb.toString())
    }

    private fun readNumber(): Token {
        val sb = StringBuilder()

        if (current == '-') {
            sb.append('-')
            advance()
        }

        while (current != null && current!!.isDigit()) {
            sb.append(current)
            advance()
        }

        return Token(TokenType.NUMBER, sb.toString())
    }

    private fun readKeyword(): Token {
        val sb = StringBuilder()

        while (current != null && current!!.isLetter()) {
            sb.append(current)
            advance()
        }

        return when (val value = sb.toString()) {
            "true", "false" -> Token(TokenType.BOOLEAN, value)
            "null" -> Token(TokenType.NULL, value)
            else -> throw Exception("Unknown keyword: $value")
        }
    }
}
