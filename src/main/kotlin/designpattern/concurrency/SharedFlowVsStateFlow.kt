package designpattern.concurrency

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

/**
 * StateFlow vs SharedFlow Comparison
 *
 * | Feature             | StateFlow                             | SharedFlow                            |
 * |---------------------|---------------------------------------|---------------------------------------|
 * | Initial Value       | Required                              | Not required                          |
 * | Replay              | Always 1 (Latest value)               | Configurable (default 0)              |
 * | Conflation          | Yes (drops intermediate duplicates)   | Optional (depends on configuration)   |
 * | Use Case            | UI State, data that always exists     | Events, triggers, multi-subscriber    |
 * | "Hotness"           | Hot (lives regardless of subscribers) | Hot (lives regardless of subscribers) |
 */

// ============================================================================
// STATEFLOW EXAMPLE
// ============================================================================

class StateFlowViewModel(scope: CoroutineScope) {
    // StateFlow MUST have an initial value
    private val _state = MutableStateFlow("Initial State")
    val state: StateFlow<String> = _state.asStateFlow()

    fun updateState(newValue: String) {
        _state.value = newValue
    }
}

// ============================================================================
// SHAREDFLOW EXAMPLE
// ============================================================================

class SharedFlowViewModel(scope: CoroutineScope) {
    // SharedFlow does NOT need an initial value
    // replay = 0 means new subscribers won't get past events
    private val _events = MutableSharedFlow<String>(replay = 0)
    val events: SharedFlow<String> = _events.asSharedFlow()

    fun triggerEvent(event: String) {
        scope.launch {
            _events.emit(event)
        }
    }
}

// ============================================================================
// DEMONSTRATION
// ============================================================================

suspend fun demonstrateStateFlow() = coroutineScope {
    println("\n--- StateFlow Demo ---")
    val viewModel = StateFlowViewModel(this)

    // Subscriber 1 starts immediately
    val job1 = launch {
        viewModel.state.collect { println("Subscriber 1 received state: $it") }
    }

    delay(100)
    viewModel.updateState("Updated State 1")
    delay(100)
    viewModel.updateState("Updated State 1") // Duplicate (Conflated - won't be emitted)
    viewModel.updateState("Updated State 2")

    delay(100)
    // Subscriber 2 starts late
    println("Subscriber 2 joining late...")
    val job2 = launch {
        viewModel.state.collect { println("Subscriber 2 received state: $it") }
    }

    delay(200)
    job1.cancel()
    job2.cancel()
}

suspend fun demonstrateSharedFlow() = coroutineScope {
    println("\n--- SharedFlow Demo ---")
    val viewModel = SharedFlowViewModel(this)

    // Subscriber 1 starts immediately
    val job1 = launch {
        viewModel.events.collect { println("Subscriber 1 received event: $it") }
    }

    delay(100)
    viewModel.triggerEvent("Event A")
    viewModel.triggerEvent("Event B")

    delay(100)
    // Subscriber 2 starts late
    println("Subscriber 2 joining late (replay = 0)...")
    val job2 = launch {
        viewModel.events.collect { println("Subscriber 2 received event: $it") }
    }

    delay(100)
    viewModel.triggerEvent("Event C")

    delay(200)
    job1.cancel()
    job2.cancel()
}

suspend fun demonstrateSharedFlowWithReplay() = coroutineScope {
    println("\n--- SharedFlow Demo (replay = 1) ---")
    val sharedFlow = MutableSharedFlow<String>(replay = 1)

    sharedFlow.emit("Replayed Message")
    
    println("Subscriber joining after emission...")
    val job = launch {
        sharedFlow.collect { println("Subscriber received: $it") }
    }

    delay(100)
    job.cancel()
}

fun main() = runBlocking {
    demonstrateStateFlow()
    demonstrateSharedFlow()
    demonstrateSharedFlowWithReplay()
    
    println("\nSummary:")
    println("1. StateFlow is for STATE (Current value matters, duplicates ignored).")
    println("2. SharedFlow is for EVENTS (Emissions matter, even if same value).")
    println("3. StateFlow always replays the last value to new subscribers.")
    println("4. SharedFlow replay is configurable.")
}
