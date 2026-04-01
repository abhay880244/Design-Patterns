package designpattern.behavioral

/**
 * Interpreter Pattern Implementation
 *
 * WHAT IS THE INTERPRETER PATTERN?
 * Defines a representation for a grammar and an interpreter to process it.
 * Useful for creating domain-specific languages (DSLs).
 *
 * KEY CONCEPTS:
 * - Expression: Interface for interpreting
 * - TerminalExpression: Leaf expressions
 * - NonTerminalExpression: Composite expressions
 * - Context: Contains info needed for interpretation
 * - Grammar: Rules for language
 *
 * REAL-WORLD ANALOGY:
 * Arithmetic calculator:
 *   - Expression: Math operations
 *   - Numbers: Terminal expressions (5, 10)
 *   - Operations: Non-terminal (+, -, *, /)
 *   - Interpreter evaluates the expression
 *
 * BENEFITS:
 * - Easy to define new grammar
 * - Simple to extend
 * - Separates grammar from interpretation
 * - Flexible language processing
 *
 * WHEN TO USE:
 * - Domain-specific languages
 * - Query languages
 * - Configuration file parsing
 * - Simple expression evaluation
 */

// ============================================================================
// EXPRESSION INTERFACE
// ============================================================================

interface Expression {
    fun interpret(): Int
}

// ============================================================================
// TERMINAL EXPRESSIONS
// ============================================================================

class NumberExpression(private val number: Int) : Expression {
    override fun interpret(): Int = number
}

// ============================================================================
// NON-TERMINAL EXPRESSIONS
// ============================================================================

class AddExpression(
    private val left: Expression,
    private val right: Expression
) : Expression {
    override fun interpret(): Int = left.interpret() + right.interpret()
}

class SubtractExpression(
    private val left: Expression,
    private val right: Expression
) : Expression {
    override fun interpret(): Int = left.interpret() - right.interpret()
}

class MultiplyExpression(
    private val left: Expression,
    private val right: Expression
) : Expression {
    override fun interpret(): Int = left.interpret() * right.interpret()
}

class DivideExpression(
    private val left: Expression,
    private val right: Expression
) : Expression {
    override fun interpret(): Int {
        val divisor = right.interpret()
        if (divisor == 0) throw IllegalArgumentException("Division by zero")
        return left.interpret() / divisor
    }
}

// ============================================================================
// ANOTHER EXAMPLE: BOOLEAN EXPRESSION INTERPRETER
// ============================================================================

interface BooleanExpression {
    fun interpret(): Boolean
}

class Variable(private val name: String, private val value: Boolean) : BooleanExpression {
    override fun interpret(): Boolean = value
}

class And(private val left: BooleanExpression, private val right: BooleanExpression) : BooleanExpression {
    override fun interpret(): Boolean = left.interpret() && right.interpret()
}

class Or(private val left: BooleanExpression, private val right: BooleanExpression) : BooleanExpression {
    override fun interpret(): Boolean = left.interpret() || right.interpret()
}

class Not(private val expression: BooleanExpression) : BooleanExpression {
    override fun interpret(): Boolean = !expression.interpret()
}

// ============================================================================
// SIMPLE PARSER (builds AST from string)
// ============================================================================

class ArithmeticParser(private val expression: String) {
    private var tokens = mutableListOf<String>()
    private var currentIndex = 0

    init {
        tokenize()
    }

    private fun tokenize() {
        val regex = Regex("\\d+|[+\\-*/()]")
        tokens = regex.findAll(expression)
            .map { it.value }
            .toMutableList()
    }

    fun parse(): Expression {
        currentIndex = 0
        return parseAddSub()
    }

    private fun parseAddSub(): Expression {
        var result = parseMulDiv()

        while (currentIndex < tokens.size && (tokens[currentIndex] == "+" || tokens[currentIndex] == "-")) {
            val operator = tokens[currentIndex]
            currentIndex++
            val right = parseMulDiv()
            result = if (operator == "+") {
                AddExpression(result, right)
            } else {
                SubtractExpression(result, right)
            }
        }

        return result
    }

    private fun parseMulDiv(): Expression {
        var result = parsePrimary()

        while (currentIndex < tokens.size && (tokens[currentIndex] == "*" || tokens[currentIndex] == "/")) {
            val operator = tokens[currentIndex]
            currentIndex++
            val right = parsePrimary()
            result = if (operator == "*") {
                MultiplyExpression(result, right)
            } else {
                DivideExpression(result, right)
            }
        }

        return result
    }

    private fun parsePrimary(): Expression {
        val token = tokens[currentIndex]
        currentIndex++

        return when {
            token == "(" -> {
                val result = parseAddSub()
                currentIndex++ // skip ")"
                result
            }
            token.all { it.isDigit() } -> NumberExpression(token.toInt())
            else -> throw IllegalArgumentException("Unknown token: $token")
        }
    }
}

// ============================================================================
// DEMO
// ============================================================================

fun demonstrateInterpreter() {
    println("=== Interpreter Pattern Demo ===\n")

    // Example 1: Arithmetic Expression Interpreter
    println("--- Arithmetic Expression Interpreter ---")

    val expressions = listOf(
        "10+5",
        "10+5*2",
        "(10+5)*2",
        "100-50/5",
        "20*3+10-5"
    )

    expressions.forEach { exprStr ->
        try {
            val parser = ArithmeticParser(exprStr)
            val ast = parser.parse()
            val result = ast.interpret()
            println("📊 '$exprStr' = $result")
        } catch (e: Exception) {
            println("❌ Error parsing '$exprStr': ${e.message}")
        }
    }

    // Example 2: Boolean Expression Interpreter
    println("\n--- Boolean Expression Interpreter ---")

    val isAdult = Variable("isAdult", true)
    val hasLicense = Variable("hasLicense", true)
    val hasBudget = Variable("hasBudget", true)
    val isWeekend = Variable("isWeekend", false)

    // Can drive if: isAdult AND hasLicense
    val canDrive = And(isAdult, hasLicense)
    println("🚗 Can drive? ${canDrive.interpret()}")

    // Can travel if: (isAdult AND hasLicense) AND (hasBudget OR isWeekend)
    val canTravel = And(
        canDrive,
        Or(hasBudget, isWeekend)
    )
    println("✈️ Can travel? ${canTravel.interpret()}")

    // Can stay home if: NOT isWeekend
    val canStayHome = Not(isWeekend)
    println("🏠 Can stay home? ${canStayHome.interpret()}")

    // Example 3: Complex Expression Building
    println("\n--- Complex Expression Building ---")

    val expr1 = AddExpression(
        NumberExpression(5),
        MultiplyExpression(
            NumberExpression(3),
            NumberExpression(4)
        )
    )
    println("📐 (5 + (3 * 4)) = ${expr1.interpret()}")

    val expr2 = SubtractExpression(
        MultiplyExpression(
            NumberExpression(10),
            NumberExpression(2)
        ),
        DivideExpression(
            NumberExpression(20),
            NumberExpression(4)
        )
    )
    println("📐 ((10 * 2) - (20 / 4)) = ${expr2.interpret()}")
}

