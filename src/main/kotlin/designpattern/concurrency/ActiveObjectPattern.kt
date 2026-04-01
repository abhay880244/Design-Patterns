package designpattern.concurrency

/**
 * Active Object Pattern Implementation
 *
 * WHAT IS THE ACTIVE OBJECT PATTERN?
 * Decouples method execution from method invocation to enhance concurrency
 * and simplify synchronized access to objects.
 *
 * KEY CONCEPTS:
 * - Proxy: Interface for method calls
 * - Scheduler: Manages method execution
 * - Queue: Holds pending method calls
 * - Servant: Real object that performs work
 * - Future: Represents result of asynchronous operation
 *
 * REAL-WORLD ANALOGY:
 * Restaurant orders:
 *   - Customer orders food (method invocation)
 *   - Order placed in kitchen queue (enqueued)
 *   - Chef prepares food when ready (asynchronous execution)
 *   - Food served when ready (future/promise)
 *
 * BENEFITS:
 * - Asynchronous method execution
 * - Thread-safe object access
 * - Better concurrency control
 * - Decouples caller from execution
 *
 * WHEN TO USE:
 * - Asynchronous operations
 * - Improving concurrency without explicit locking
 * - Task scheduling
 * - Message-based systems
 */

import java.util.concurrent.BlockingQueue
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.Future
import java.util.concurrent.FutureTask
import kotlin.random.Random

// ============================================================================
// ACTIVE OBJECT SERVANT
// ============================================================================

class DataProcessor(private val name: String) {
    fun processData(data: String): String {
        println("⚙️ [$name] Processing: $data")
        Thread.sleep(Random.nextLong(100, 500))
        return "Processed: $data"
    }

    fun calculateSum(a: Int, b: Int): Int {
        println("🔢 [$name] Calculating: $a + $b")
        Thread.sleep(Random.nextLong(100, 300))
        return a + b
    }

    fun fetchData(url: String): String {
        println("🌐 [$name] Fetching from: $url")
        Thread.sleep(Random.nextLong(200, 600))
        return "Data from $url"
    }
}

// ============================================================================
// ACTIVE OBJECT PROXY AND SCHEDULER
// ============================================================================

class ActiveObjectProxy(name: String) {
    private val processor = DataProcessor(name)
    private val commandQueue: BlockingQueue<() -> Unit> = LinkedBlockingQueue()
    private val thread: Thread

    init {
        thread = Thread { scheduler() }
        thread.isDaemon = false
        thread.start()
    }

    fun processData(data: String): Future<String> {
        val future = FutureTask { processor.processData(data) }
        commandQueue.put { future.run() }
        return future
    }

    fun calculateSum(a: Int, b: Int): Future<Int> {
        val future = FutureTask { processor.calculateSum(a, b) }
        commandQueue.put { future.run() }
        return future
    }

    fun fetchData(url: String): Future<String> {
        val future = FutureTask { processor.fetchData(url) }
        commandQueue.put { future.run() }
        return future
    }

    private fun scheduler() {
        try {
            while (!Thread.currentThread().isInterrupted) {
                val command = commandQueue.take()
                command()
            }
        } catch (_: InterruptedException) {
            println("✅ Scheduler interrupted")
            Thread.currentThread().interrupt()
        }
    }

    fun shutdown() {
        thread.interrupt()
        thread.join(1000)
    }
}

// ============================================================================
// ANOTHER EXAMPLE: ASYNC CALCULATOR
// ============================================================================

class Calculator {
    fun add(a: Int, b: Int): Int {
        println("🔢 Adding $a + $b")
        Thread.sleep(100)
        return a + b
    }

    fun multiply(a: Int, b: Int): Int {
        println("🔢 Multiplying $a * $b")
        Thread.sleep(150)
        return a * b
    }

    fun fibonacci(n: Int): Int {
        println("🔢 Computing fibonacci($n)")
        Thread.sleep(n * 10L)
        return if (n <= 1) n else fibonacci(n - 1) + fibonacci(n - 2)
    }
}

// ============================================================================
// ASYNC CALCULATOR PROXY
// ============================================================================

class AsyncCalculator {
    private val calculator = Calculator()
    private val commandQueue: BlockingQueue<() -> Unit> = LinkedBlockingQueue()
    private val thread: Thread

    init {
        thread = Thread { executeCommands() }
        thread.isDaemon = false
        thread.start()
    }

    fun <T> async(block: (Calculator) -> T): Future<T> {
        val future = FutureTask {
            block(calculator)
        }
        commandQueue.put { future.run() }
        return future
    }

    private fun executeCommands() {
        try {
            while (!Thread.currentThread().isInterrupted) {
                val command = commandQueue.take()
                command()
            }
        } catch (_: InterruptedException) {
            Thread.currentThread().interrupt()
        }
    }

    fun shutdown() {
        thread.interrupt()
        thread.join(1000)
    }
}

// ============================================================================
// DEMO
// ============================================================================

fun demonstrateActiveObject() {
    println("=== Active Object Pattern Demo ===\n")

    // Example 1: Data Processor with Active Object
    println("--- Data Processor with Active Object ---")
    val processor = ActiveObjectProxy("DataProcessor-1")

    println("\n📤 Sending requests (asynchronous):")
    val future1 = processor.processData("Sample Data 1")
    val future2 = processor.processData("Sample Data 2")
    val future3 = processor.calculateSum(10, 20)
    val future4 = processor.fetchData("http://example.com/api")

    println("\n📥 Waiting for results:")
    try {
        println("Result 1: ${future1.get()}")
        println("Result 2: ${future2.get()}")
        println("Result 3: ${future3.get()}")
        println("Result 4: ${future4.get()}")
    } catch (e: Exception) {
        println("❌ Error: ${e.message}")
    }

    processor.shutdown()

    // Example 2: Async Calculator
    println("\n\n--- Async Calculator ---")
    val asyncCalc = AsyncCalculator()

    println("\n📤 Submitting calculations:")
    val calcFuture1 = asyncCalc.async { calc -> calc.add(5, 3) }
    val calcFuture2 = asyncCalc.async { calc -> calc.multiply(4, 7) }
    val calcFuture3 = asyncCalc.async { calc -> calc.fibonacci(10) }

    println("\n📥 Retrieving calculation results:")
    try {
        println("5 + 3 = ${calcFuture1.get()}")
        println("4 * 7 = ${calcFuture2.get()}")
        println("fibonacci(10) = ${calcFuture3.get()}")
    } catch (e: Exception) {
        println("❌ Error: ${e.message}")
    }

    asyncCalc.shutdown()
    println("\n✅ Demo completed")
}
