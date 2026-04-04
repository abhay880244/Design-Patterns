package designpattern.concurrency

import kotlinx.coroutines.*

/**
 * Coroutine Exception Handling Guide
 *
 * 1. PROPAGATION VS. CANCELLATION
 *    In a normal scope, an exception in a child:
 *    - Propagates to the parent (notifying it of the error).
 *    - Cancels the parent (which in turn cancels all other siblings).
 *
 * 2. THE SUPERVISOR DIFFERENCE
 *    A SupervisorJob or supervisorScope blocks this cancellation.
 *    - Child failure does NOT cancel the parent or siblings.
 *    - CRITICAL: Because the parent is NOT notified to handle the error, the
 *      child is now responsible for its own exception. If it doesn't handle it,
 *      it's treated as an "uncaught" exception (crashing the app or printing to stderr).
 *
 * 3. FOUR WAYS A CHILD HANDLES ITS OWN EXCEPTION (The "Responsibility"):
 *    A. Local try-catch: Wrap the code INSIDE the 'launch' or 'async' block.
 *    B. CoroutineExceptionHandler (CEH): Attach a handler to the coroutine's context.
 *       (Best for 'launch' as a global catch-all).
 *    C. runCatching { }: A Kotlin utility that wraps a block in a 'Result' object.
 *    D. async + await(): Wrap the 'await()' call in a try-catch (not the async block itself).
 *
 * 4. RULES FOR SUPERVISORS:
 *    Every child 'launch' inside a supervisor MUST have its own error handling:
 *    - SupervisorJob (Global): Add the CEH to the Scope context. All children inherit it.
 *    - supervisorScope (Local): Pass the CEH specifically to each child 'launch'.
 */

fun main() = runBlocking {
    println("=== Coroutine Exception Handling Examples ===")

    exampleTryCatch()
//    exampleCoroutineExceptionHandler()
//    exampleAsyncException()
//    exampleAsyncChildOfLaunchException()
//    exampleSupervisorJob()
//    exampleSupervisorScope()
    
    println("\nAll examples finished.")
}

/**
 * 1. Simple Try-Catch
 * The most straightforward way is to wrap the code inside the coroutine with try-catch.
 */
suspend fun exampleTryCatch() = coroutineScope {
    println("\n--- 1. Simple Try-Catch ---")
    val job = launch {
        try {
            println("Child coroutine working...")
            throw IllegalStateException("Oops!")
        } catch (e: Exception) {
            println("Caught in try-catch: ${e.message}")
        }
    }
    job.join()
}

/**
 * 2. CoroutineExceptionHandler
 * Used to catch exceptions that weren't caught by try-catch.
 * It only works when installed on a root coroutine (top-level launch).
 */
suspend fun exampleCoroutineExceptionHandler() {
    println("\n--- 2. CoroutineExceptionHandler ---")
    val handler = CoroutineExceptionHandler { _, exception ->
        println("Caught by ExceptionHandler: $exception")
    }

    val scope = CoroutineScope(Job() + handler)
    val job = scope.launch {
        println("Root coroutine throwing exception...")
        throw RuntimeException("Root failure")
    }
    job.join()
}

/**
 * 3. Exceptions in async
 * 'async' stores the exception and throws it when 'await()' is called.
 * We use supervisorScope so the child failure doesn't immediately crash the parent.
 * (For running without crashing the main thread)
 */
suspend fun exampleAsyncException() = supervisorScope {
    println("\n--- 3. Async Exception Handling (Root-ish) ---")
    // When async is a direct child of supervisorScope, we can catch it at await().
    val deferred = async<Unit> {
        println("Async working and failing...")
        throw ArithmeticException("Division by zero")
    }

    try {
        deferred.await()
    } catch (e: Exception) {
        println("Caught exception from await(): ${e.message}")
    }
}

/**
 * 3b. Async as a child of launch
 * This demonstrates that even if you don't call await(), an async block
 * inside a launch will fail the parent launch immediately.
 */
suspend fun exampleAsyncChildOfLaunchException() = supervisorScope {
    println("\n--- 3b. Async as child of launch (Immediate Propagation) ---")

    val handler = CoroutineExceptionHandler { _, exception ->
        println("Caught in CoroutineExceptionHandler: $exception")
    }
    
    val parentJob = launch(handler) {
        println("Parent launch: starting...")
        
        async {
            println("Child async: throwing exception...")
            throw IllegalStateException("Failure in child async")
        }

        try {
            println("Parent launch: waiting for something...")
            delay(1000)
            println("Parent launch: this will NOT be reached")
        } catch (e: CancellationException) {
            println("Parent launch: I was cancelled because my child (async) failed!")
        }
    }
    
    parentJob.join()
}

/**
 * 4. SupervisorJob + CoroutineExceptionHandler
 * Best for: Background tasks (like a ViewModel or Repository) that manage
 * multiple independent jobs.
 *
 * MECHANICS:
 * The scope context is (SupervisorJob + handler). 
 * When child1 fails, the SupervisorJob refuses to propagate the failure 
 * upward. Instead, child1 looks at its own context for a handler and
 * finds the one inherited from the scope.
 */
suspend fun exampleSupervisorJob() {
    println("\n--- 4. SupervisorJob ---")
    
    val handler = CoroutineExceptionHandler { _, exception ->
        println("Caught by CoroutineExceptionHandler: ${exception.message}")
    }
    
    val supervisor = SupervisorJob()
    // We combine SupervisorJob and ExceptionHandler
    val scope = CoroutineScope(Dispatchers.Default + supervisor + handler)

    val child1 = scope.launch {
        println("Child 1 failing...")
        throw RuntimeException("Child 1 error")
    }
    
    val child2 = scope.launch {
        delay(50)
        println("Child 2 still running despite Child 1's failure")
    }

    joinAll(child1, child2)
}

/**
 * 5. supervisorScope + CoroutineExceptionHandler
 * Best for: A specific function that needs to run multiple tasks where
 * one task's failure shouldn't stop the others.
 *
 * CRITICAL RULE:
 * A CoroutineExceptionHandler in a supervisorScope context (passed to the
 * builder itself) DOES NOT work. You MUST pass it to each individual child 
 * launch inside the scope.
 */
suspend fun exampleSupervisorScope() = coroutineScope {
    println("\n--- 5. supervisorScope ---")
    
    val handler = CoroutineExceptionHandler { _, exception ->
        println("Caught by supervisorScope handler: ${exception.message}")
    }

    supervisorScope {
        val child1 = launch(handler) {
            println("Child 1 failing...")
            throw RuntimeException("Child 1 error")
        }

        val child2 = launch(handler) {
            delay(50)
            println("Child 2 still running")
        }
        
        joinAll(child1, child2)
    }
}
