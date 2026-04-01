package designpattern.concurrency

/**
 * Producer-Consumer Pattern Implementation
 *
 * WHAT IS THE PRODUCER-CONSUMER PATTERN?
 * A concurrency pattern where one or more producers create data and place it
 * in a buffer, while one or more consumers retrieve and process the data.
 *
 * KEY CONCEPTS:
 * - Producer: Creates data items
 * - Consumer: Processes data items
 * - Buffer: Shared storage (queue, list, etc.)
 * - Synchronization: Coordination between producers and consumers
 * - Thread Safety: Prevents race conditions
 *
 * REAL-WORLD ANALOGY:
 * Factory assembly line:
 *   - Workers (Producers) create products
 *   - Products placed on conveyor belt (Buffer)
 *   - Quality checkers (Consumers) inspect products
 *   - System manages synchronization
 *
 * BENEFITS:
 * - Decouples producers from consumers
 * - Improves performance through buffering
 * - Better thread utilization
 * - Handles varying production/consumption rates
 *
 * WHEN TO USE:
 * - Decoupling production and consumption
 * - Handling varying processing rates
 * - Task distribution among threads
 * - Pipeline processing
 */

import java.util.concurrent.BlockingQueue
import java.util.concurrent.LinkedBlockingQueue
import kotlin.random.Random

// ============================================================================
// DATA CLASS
// ============================================================================

data class Product(
    val id: Int,
    val name: String,
    val value: Double
)

// ============================================================================
// PRODUCER
// ============================================================================

class Producer(
    private val id: Int,
    private val buffer: BlockingQueue<Product>,
    private val delayMs: Long = 100
) : Runnable {

    override fun run() {
        try {
            repeat(5) { i ->
                val product = Product(
                    id = (id * 100) + i,
                    name = "Product-${id}-${i}",
                    value = Random.nextDouble(10.0, 100.0)
                )

                println("🏭 Producer-$id: Producing $product")
                buffer.put(product)

                Thread.sleep(delayMs)
            }
            println("✅ Producer-$id: Finished producing")
        } catch (e: InterruptedException) {
            println("❌ Producer-$id: Interrupted")
            Thread.currentThread().interrupt()
        }
    }
}

// ============================================================================
// CONSUMER
// ============================================================================

class Consumer(
    private val id: Int,
    private val buffer: BlockingQueue<Product>,
    private val delayMs: Long = 150
) : Runnable {

    override fun run() {
        try {
            while (true) {
                val product = buffer.take()
                println("👷 Consumer-$id: Consuming $product")

                // Simulate processing
                Thread.sleep(delayMs)
                println("   → Consumer-$id: Processed (Value: \$${String.format("%.2f", product.value)})")
            }
        } catch (e: InterruptedException) {
            println("✅ Consumer-$id: Finished consuming")
            Thread.currentThread().interrupt()
        }
    }
}

// ============================================================================
// PRODUCER-CONSUMER ORCHESTRATOR
// ============================================================================

class ProducerConsumerSystem(
    private val bufferSize: Int = 10
) {
    private val buffer: BlockingQueue<Product> = LinkedBlockingQueue(bufferSize)
    private val threads = mutableListOf<Thread>()

    fun addProducer(id: Int, delayMs: Long = 100) {
        val producer = Producer(id, buffer, delayMs)
        val thread = Thread(producer, "Producer-$id")
        threads.add(thread)
    }

    fun addConsumer(id: Int, delayMs: Long = 150) {
        val consumer = Consumer(id, buffer, delayMs)
        val thread = Thread(consumer, "Consumer-$id")
        threads.add(thread)
    }

    fun start() {
        println("\n🚀 Starting Producer-Consumer System")
        println("Buffer size: $bufferSize\n")
        threads.forEach { it.start() }
    }

    fun stop(timeoutMs: Long = 3000) {
        Thread.sleep(timeoutMs)
        println("\n🛑 Stopping system...")
        threads.forEach { it.interrupt() }
        threads.forEach { it.join(1000) }
        println("✅ System stopped")
    }

    fun getBufferSize(): Int = buffer.size
}

// ============================================================================
// ANOTHER EXAMPLE: WORK QUEUE
// ============================================================================

data class Task(
    val id: Int,
    val name: String,
    val duration: Long
)

class TaskProducer(
    private val queue: BlockingQueue<Task>
) : Runnable {
    override fun run() {
        try {
            repeat(10) { i ->
                val task = Task(
                    id = i,
                    name = "Task-$i",
                    duration = Random.nextLong(100, 500)
                )
                println("📝 TaskProducer: Creating $task")
                queue.put(task)
                Thread.sleep(200)
            }
            // Send poison pill to signal end
            queue.put(Task(-1, "STOP", 0))
            println("✅ TaskProducer: Finished")
        } catch (e: InterruptedException) {
            Thread.currentThread().interrupt()
        }
    }
}

class TaskWorker(
    private val id: Int,
    private val queue: BlockingQueue<Task>
) : Runnable {
    override fun run() {
        try {
            while (true) {
                val task = queue.take()
                if (task.id == -1) { // Poison pill
                    println("✅ TaskWorker-$id: Received stop signal")
                    queue.put(task) // Re-queue for other workers
                    break
                }

                println("⚙️ TaskWorker-$id: Processing $task")
                Thread.sleep(task.duration)
                println("   → TaskWorker-$id: Completed ${task.name}")
            }
        } catch (e: InterruptedException) {
            Thread.currentThread().interrupt()
        }
    }
}

// ============================================================================
// DEMO
// ============================================================================

fun demonstrateProducerConsumer() {
    println("=== Producer-Consumer Pattern Demo ===")

    // Example 1: Simple Producer-Consumer
    println("\n--- Example 1: Product Manufacturing ---")
    val system = ProducerConsumerSystem(bufferSize = 5)

    system.addProducer(1, 100)
    system.addProducer(2, 120)

    system.addConsumer(1, 150)
    system.addConsumer(2, 180)

    system.start()
    system.stop(4000)

    // Example 2: Task Queue with Poison Pill
    println("\n\n--- Example 2: Task Queue System ---")
    val taskQueue: BlockingQueue<Task> = LinkedBlockingQueue(10)

    val producer = Thread(TaskProducer(taskQueue), "TaskProducer")
    val worker1 = Thread(TaskWorker(1, taskQueue), "TaskWorker-1")
    val worker2 = Thread(TaskWorker(2, taskQueue), "TaskWorker-2")

    println("🚀 Starting task queue system\n")
    producer.start()
    worker1.start()
    worker2.start()

    producer.join()
    worker1.join(3000)
    worker2.join(3000)

    println("\n✅ Task queue system completed")
}

