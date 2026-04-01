package designpattern.behavioral

/**
 * Strategy Pattern Implementation
 *
 * WHAT IS THE STRATEGY PATTERN?
 * Defines a family of algorithms, encapsulates each one, and makes them interchangeable.
 * Lets the algorithm vary independently from clients that use it.
 *
 * KEY CONCEPTS:
 * - Strategy: Interface for different algorithms
 * - ConcreteStrategy: Different algorithm implementations
 * - Context: Uses the strategy
 * - Client: Chooses which strategy to use
 *
 * REAL-WORLD ANALOGY:
 * Different payment methods:
 *   - Strategy: Payment interface
 *   - ConcreteStrategy: Credit card, PayPal, Bitcoin payment
 *   - Context: Checkout process
 *   - Client chooses payment method
 *
 * BENEFITS:
 * - Easy to switch algorithms at runtime
 * - Eliminates conditional statements
 * - Follows Open/Closed Principle
 * - Encapsulates algorithm variations
 *
 * WHEN TO USE:
 * - Multiple algorithms for a task
 * - Algorithm selection at runtime
 * - Eliminate complex if-else chains
 */

// ============================================================================
// STRATEGY INTERFACE
// ============================================================================

interface PaymentStrategy {
    fun pay(amount: Double): Boolean
}

// ============================================================================
// CONCRETE STRATEGIES
// ============================================================================

class CreditCardPayment(
    private val cardNumber: String,
    private val cvv: String
) : PaymentStrategy {
    override fun pay(amount: Double): Boolean {
        println("💳 Processing credit card payment: $$amount")
        println("💳 Card: $cardNumber")
        return true
    }
}

class PayPalPayment(private val email: String) : PaymentStrategy {
    override fun pay(amount: Double): Boolean {
        println("🌐 Processing PayPal payment: $$amount")
        println("🌐 Account: $email")
        return true
    }
}

class BitcoinPayment(private val walletAddress: String) : PaymentStrategy {
    override fun pay(amount: Double): Boolean {
        println("₿ Processing Bitcoin payment: $amount BTC")
        println("₿ Wallet: $walletAddress")
        return true
    }
}

class BankTransferPayment(
    private val accountNumber: String,
    private val bankCode: String
) : PaymentStrategy {
    override fun pay(amount: Double): Boolean {
        println("🏦 Processing bank transfer: $$amount")
        println("🏦 Account: $accountNumber (Bank Code: $bankCode)")
        return true
    }
}

// ============================================================================
// CONTEXT
// ============================================================================

class ShoppingCart(private var paymentStrategy: PaymentStrategy? = null) {
    private val items = mutableListOf<String>()
    private var total: Double = 0.0

    fun addItem(item: String, price: Double) {
        items.add(item)
        total += price
        println("➕ Added $item to cart ($$price)")
    }

    fun setPaymentStrategy(strategy: PaymentStrategy) {
        this.paymentStrategy = strategy
        println("✅ Payment strategy set")
    }

    fun checkout(): Boolean {
        if (paymentStrategy == null) {
            println("❌ Please set a payment strategy first")
            return false
        }

        println("\n📋 Checkout Summary:")
        println("Items: ${items.size}")
        println("Total: $$total\n")

        return paymentStrategy!!.pay(total)
    }

    fun displayCart() {
        println("🛒 Shopping Cart:")
        items.forEach { println("  - $it") }
        println("Total: $$total")
    }
}

// ============================================================================
// ANOTHER EXAMPLE: SORTING STRATEGIES
// ============================================================================

interface SortingStrategy {
    fun sort(numbers: MutableList<Int>)
}

class BubbleSortStrategy : SortingStrategy {
    override fun sort(numbers: MutableList<Int>) {
        println("🫧 Bubble Sort Algorithm")
        for (i in numbers.indices) {
            for (j in 0 until numbers.size - 1 - i) {
                if (numbers[j] > numbers[j + 1]) {
                    val temp = numbers[j]
                    numbers[j] = numbers[j + 1]
                    numbers[j + 1] = temp
                }
            }
        }
    }
}

class QuickSortStrategy : SortingStrategy {
    override fun sort(numbers: MutableList<Int>) {
        println("⚡ Quick Sort Algorithm")
        quickSort(numbers, 0, numbers.size - 1)
    }

    private fun quickSort(arr: MutableList<Int>, low: Int, high: Int) {
        if (low < high) {
            val pi = partition(arr, low, high)
            quickSort(arr, low, pi - 1)
            quickSort(arr, pi + 1, high)
        }
    }

    private fun partition(arr: MutableList<Int>, low: Int, high: Int): Int {
        val pivot = arr[high]
        var i = low - 1
        for (j in low until high) {
            if (arr[j] < pivot) {
                i++
                val temp = arr[i]
                arr[i] = arr[j]
                arr[j] = temp
            }
        }
        val temp = arr[i + 1]
        arr[i + 1] = arr[high]
        arr[high] = temp
        return i + 1
    }
}

class MergeSortStrategy : SortingStrategy {
    override fun sort(numbers: MutableList<Int>) {
        println("🔀 Merge Sort Algorithm")
        if (numbers.size > 1) {
            mergeSort(numbers, 0, numbers.size - 1)
        }
    }

    private fun mergeSort(arr: MutableList<Int>, left: Int, right: Int) {
        if (left < right) {
            val mid = (left + right) / 2
            mergeSort(arr, left, mid)
            mergeSort(arr, mid + 1, right)
            merge(arr, left, mid, right)
        }
    }

    private fun merge(arr: MutableList<Int>, left: Int, mid: Int, right: Int) {
        val leftArr = arr.subList(left, mid + 1).toMutableList()
        val rightArr = arr.subList(mid + 1, right + 1).toMutableList()

        var i = 0
        var j = 0
        var k = left

        while (i < leftArr.size && j < rightArr.size) {
            if (leftArr[i] <= rightArr[j]) {
                arr[k++] = leftArr[i++]
            } else {
                arr[k++] = rightArr[j++]
            }
        }

        while (i < leftArr.size) {
            arr[k++] = leftArr[i++]
        }

        while (j < rightArr.size) {
            arr[k++] = rightArr[j++]
        }
    }
}

class NumberSorter(private var strategy: SortingStrategy = BubbleSortStrategy()) {
    fun setStrategy(strategy: SortingStrategy) {
        this.strategy = strategy
    }

    fun sortNumbers(numbers: MutableList<Int>) {
        println("Sorting: $numbers")
        strategy.sort(numbers)
        println("Result: $numbers\n")
    }
}

// ============================================================================
// DEMO
// ============================================================================

fun demonstrateStrategy() {
    println("=== Strategy Pattern Demo ===\n")

    // Example 1: Payment Strategies
    println("--- Payment Strategies ---")
    val cart = ShoppingCart()
    cart.addItem("Laptop", 999.99)
    cart.addItem("Mouse", 29.99)

    // Pay with credit card
    println("\n🔄 Switching to Credit Card Payment:")
    cart.setPaymentStrategy(CreditCardPayment("1234-5678-9012-3456", "123"))
    cart.checkout()

    // Pay with PayPal
    println("\n🔄 Switching to PayPal Payment:")
    cart.setPaymentStrategy(PayPalPayment("user@example.com"))
    cart.checkout()

    // Pay with Bitcoin
    println("\n🔄 Switching to Bitcoin Payment:")
    cart.setPaymentStrategy(BitcoinPayment("1A1z7agoat8Bt16ySrfxupVSKCAFAcKAqN"))
    cart.checkout()

    // Example 2: Sorting Strategies
    println("\n--- Sorting Strategies ---")
    val sorter = NumberSorter()
    val numbers = mutableListOf(64, 34, 25, 12, 22, 11, 90)

    sorter.setStrategy(BubbleSortStrategy())
    sorter.sortNumbers(numbers.toMutableList())

    sorter.setStrategy(QuickSortStrategy())
    sorter.sortNumbers(numbers.toMutableList())

    sorter.setStrategy(MergeSortStrategy())
    sorter.sortNumbers(numbers.toMutableList())
}

