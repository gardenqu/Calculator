package com.qgdev.calculator

import kotlin.math.pow

class CalculatorEngine {
    private val expression = StringBuilder()



    fun append(value: String) {
        expression.append(value)
    }

    fun clear() {
        expression.clear()
    }

    fun calculate(): String {
        return try {
            val exp = expression.toString()
                .replace('×', '*')
                .replace('÷', '/')
                .replace('∙', '*')
                .replace('•', '*')
                .replace('x', '*')
                .replace('X', '*')

            val result = evaluateSimple(exp)
            result.toString()
        } catch (e: Exception) {
            "0"
        }
    }


    private fun precedence(op: Char): Int {
        return when (op) {
            '+', '-' -> 1
            '*', '/' -> 2
            else -> -1
        }
    }

    private fun applyOp(a: Double, b: Double, op: Char): Double {
        return when (op) {
            '+' -> a + b
            '-' -> a - b
            '*' -> a * b
            '/' -> a / b
            else -> 0.0
        }
    }

    private fun evaluateSimple(expr: String): Double {
        val values = ArrayDeque<Double>()
        val ops = ArrayDeque<Char>()

        var i = 0
        while (i < expr.length) {
            val c = expr[i]

            when {
                c.isWhitespace() -> { i++ } // skip spaces

                // Number (possibly negative if unary minus)
                c.isDigit() || (c == '-' && (i == 0 || expr[i-1] in "+-*/")) -> {
                    var num = ""
                    num += c
                    i++
                    while (i < expr.length && (expr[i].isDigit() || expr[i] == '.')) {
                        num += expr[i]
                        i++
                    }
                    values.addLast(num.toDouble())
                    continue
                }


                c in "+-*/" -> {
                    while (ops.isNotEmpty() && precedence(ops.last()) >= precedence(c)) {
                        val b = values.removeLast()
                        val a = values.removeLast()
                        val op = ops.removeLast()
                        values.addLast(applyOp(a, b, op))
                    }
                    ops.addLast(c)
                }
            }
            i++
        }

        while (ops.isNotEmpty()) {
            val b = values.removeLast()
            val a = values.removeLast()
            val op = ops.removeLast()
            values.addLast(applyOp(a, b, op))
        }

        return values.last()
    }



    override fun toString(): String {
        return expression.toString()
    }

    fun input(token: String): String {
        return when (token) {
            "=" -> {
                val rawResult = calculate()
                // Convert to double to check if it's an integer
                val formattedResult = rawResult.toDoubleOrNull()?.let {
                    if (it % 1.0 == 0.0) it.toInt().toString() else it.toString()
                } ?: rawResult

                expression.clear()
                expression.append(formattedResult) // keep result for further calculations
                formattedResult
            }
            "AC" -> {
                clear()
                "0"
            }
            "." -> {
                // Prevent multiple decimals in the current number
                val lastNumber = expression.split(Regex("[+\\-*/]")).lastOrNull() ?: ""
                if (lastNumber.contains(".")) {
                    // already has a decimal, ignore this input
                    expression.toString()
                } else {
                    append(".")
                    expression.toString()
                }
            }
            else -> {
                append(token)
                expression.toString()
            }
        }
    }

}
