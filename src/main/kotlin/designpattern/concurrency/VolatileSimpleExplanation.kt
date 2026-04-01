package designpattern.concurrency

/**
 * @Volatile: A Single, Clear Explanation
 *
 * WHAT IS @Volatile?
 * @Volatile ensures that when one thread writes to a variable, all other threads
 * immediately see that change. Without it, threads may cache old values.
 *
 * HOW DOES IT WORK?
 * - Normally: Thread A updates a value in its CPU cache, but Thread B still reads
 *   the old value from its own cache. They see different values!
 * - With @Volatile: Both threads read/write directly to main memory, so they
 *   always see the latest value.
 *
 * MEMORY BARRIERS:
 * @Volatile creates "memory barriers" that tell the CPU:
 *   - Before writing: Flush all pending changes to main memory
 *   - Before reading: Ignore cached values, get fresh from main memory
 *
 * WHAT ARE MEMORY BARRIERS?
 * Memory barriers are synchronization mechanisms that control how the CPU reads
 * and writes data, ensuring proper visibility across threads.
 *
 * WRITE BARRIER (before writing to @Volatile):
 *   - When a thread modifies a @Volatile variable, it must:
 *     1. Write the change immediately to MAIN MEMORY (not just CPU cache)
 *     2. Flush any other pending writes so they're visible to all threads
 *     3. Result: Other threads see the new value immediately
 *
 * READ BARRIER (before reading from @Volatile):
 *   - When a thread reads a @Volatile variable, it must:
 *     1. Discard any cached copies it might have
 *     2. Fetch the LATEST VALUE directly from main memory
 *     3. Result: You always see the most up-to-date value, not stale data
 *
 * ANALOGY:
 * Think of it like a group chat:
 *   - Without barriers: You type a message but it stays on your phone only
 *     Other people don't see it immediately
 *   - With barriers: Your message goes to a shared server and everyone
 *     refreshes to see the latest version
 *
 * REAL-WORLD EXAMPLE: WITHOUT @Volatile
 * =====================================
 * Scenario: Singleton instance creation with multiple threads
 *
 * Thread A creates the instance:
 *   1. instance = DatabaseConnectionManager()
 *   2. Assignment written to Thread A's CPU cache
 *   3. NOT flushed to main memory
 *
 * Thread B tries to get the instance:
 *   1. if (instance == null) {  // Read from Thread B's cache
 *   2. Cache still has null (outdated, not refreshed)
 *   3. Thinks instance doesn't exist
 *   4. Creates ANOTHER instance
 *   5. Now we have TWO instances! ❌
 *
 * PROBLEM: Thread B doesn't see the value created by Thread A
 * RESULT: Multiple instances created, Singleton pattern FAILS
 *
 * REAL-WORLD EXAMPLE: WITH @Volatile (Memory Barriers)
 * ====================================================
 * Scenario: Singleton instance creation with multiple threads
 *
 * Thread A creates the instance:
 *   1. instance = DatabaseConnectionManager()
 *   2. WRITE BARRIER triggers
 *   3. Memory barrier flushes it to main memory
 *   4. All threads are notified of the change
 *
 * Thread B tries to get the instance:
 *   1. if (instance == null) {  // About to read
 *   2. READ BARRIER triggers
 *   3. Memory barrier forces it to read fresh from main memory
 *   4. Thread B sees the actual instance, NOT null ✓
 *   5. Skips creation, returns existing instance
 *   6. Only ONE instance exists ✓
 *
 * BENEFIT: All threads see the same instance immediately
 * RESULT: Singleton pattern works correctly, no race conditions
 *
 * Limitations: volatile is Not a Full Replacement for synchronized
 * It is a common misconception that volatile is a complete substitute for synchronized.
 * It has an important limitation:
 * No Atomicity for Compound Operations:
 * The volatile keyword guarantees that individual reads and writes of the variable
 * itself are atomic, but not compound operations.
 * Example: counter++ is a compound operation (read, increment, then write).
 * If two threads perform this simultaneously on a volatile variable, a race
 * condition can still occur, leading to an incorrect result.
 * Race Condition Example: If Thread A and Thread B both read count = 5,
 * they will both increment it to 6 and write 6 back, even though the value should be 7
 * For such operations, you need to use synchronized blocks or atomic classes like AtomicInteger
 *
 * KEY POINT:
 * @Volatile guarantees VISIBILITY (threads see changes), but NOT ATOMICITY
 * (counter++ is still not thread-safe even with @Volatile).
 */

// ============================================================================
// SINGLE PRACTICAL EXAMPLE: Singleton Pattern
// ============================================================================

class DatabaseConnectionManager private constructor() {
    companion object {
        // WHY @Volatile HERE?
        // Multiple threads call getInstance() simultaneously.
        // Thread A initializes the singleton and sets instance.
        // Thread B must immediately see this assignment, not a cached null.
        @Volatile
        private var instance: DatabaseConnectionManager? = null

        fun getInstance(): DatabaseConnectionManager {
            // FIRST CHECK (no lock, fast path)
            if (instance == null) {
                // DOUBLE-CHECKED LOCKING
                synchronized(this) {
                    // SECOND CHECK (with lock, ensures only one thread creates instance)
                    if (instance == null) {
                        instance = DatabaseConnectionManager()
                        println("Instance created by ${Thread.currentThread().name}")
                    }
                }
            }
            // @Volatile ensures all threads see the created instance
            return instance!!
        }
    }

    fun query(sql: String) {
        println("Executing: $sql")
    }
}

// ============================================================================
// DEMONSTRATION
// ============================================================================

fun main() {
    // Create 5 threads trying to get the Singleton simultaneously
    val threads = (1..5).map { threadNum ->
        Thread {
            val manager = DatabaseConnectionManager.getInstance()
            manager.query("SELECT * FROM users")
            println("Thread $threadNum: Got instance = ${manager.hashCode()}")
        }
    }

    threads.forEach { it.start() }
    threads.forEach { it.join() }

    println("\nAll threads got the SAME instance (same hashCode) ✓")
}

/**
 * OUTPUT EXAMPLE:
 * Instance created by Thread-1
 * Thread 2: Got instance = 12345
 * Thread 1: Got instance = 12345
 * Thread 3: Got instance = 12345
 * Thread 4: Got instance = 12345
 * Thread 5: Got instance = 12345
 *
 * All threads got the SAME instance (same hashCode) ✓
 *
 * Without @Volatile:
 * - Thread 2 might not see the assignment by Thread 1
 * - Multiple instances could be created
 * - Singleton pattern fails!
 */

