package designpattern.behavioral

/**
 * State Pattern Implementation
 *
 * WHAT IS THE STATE PATTERN?
 * Allows an object to alter its behavior when its internal state changes.
 * The object will appear to change its class.
 *
 * KEY CONCEPTS:
 * - State: Interface for different states
 * - ConcreteState: Specific state implementations
 * - Context: Contains a reference to the current state
 * - State changes based on business logic
 *
 * REAL-WORLD ANALOGY:
 * Traffic light:
 *   - Red state: Stop
 *   - Yellow state: Prepare to stop
 *   - Green state: Go
 *   - Light behaves differently based on current state
 *
 * BENEFITS:
 * - Encapsulates state-specific behavior
 * - Eliminates complex conditional statements
 * - Makes it easy to add new states
 * - Follows Single Responsibility Principle
 *
 * WHEN TO USE:
 * - Object behavior depends on its state
 * - Large conditional statements based on state
 * - State machines and workflows
 */

// ============================================================================
// STATE INTERFACE
// ============================================================================

interface State {
    fun onEnter(context: Context)
    fun onExit(context: Context)
    fun handle(context: Context)
}

// ============================================================================
// CONCRETE STATES
// ============================================================================

class GreenState : State {
    override fun onEnter(context: Context) {
        println("🟢 Light turned GREEN")
        println("🚗 Traffic: GO!")
    }

    override fun onExit(context: Context) {
        println("🟢 Leaving GREEN state")
    }

    override fun handle(context: Context) {
        context.state = YellowState()
        context.state!!.onEnter(context)
    }
}

class YellowState : State {
    override fun onEnter(context: Context) {
        println("🟡 Light turned YELLOW")
        println("🚗 Traffic: PREPARE TO STOP!")
    }

    override fun onExit(context: Context) {
        println("🟡 Leaving YELLOW state")
    }

    override fun handle(context: Context) {
        context.state = RedState()
        context.state!!.onEnter(context)
    }
}

class RedState : State {
    override fun onEnter(context: Context) {
        println("🔴 Light turned RED")
        println("🚗 Traffic: STOP!")
    }

    override fun onExit(context: Context) {
        println("🔴 Leaving RED state")
    }

    override fun handle(context: Context) {
        context.state = GreenState()
        context.state!!.onEnter(context)
    }
}

// ============================================================================
// CONTEXT
// ============================================================================

class TrafficLight {
    var state: State? = RedState()
        set(value) {
            field?.onExit(this)
            field = value
        }

    init {
        state!!.onEnter(this)
    }

    fun change() {
        state?.handle(this)
    }
}

// ============================================================================
// ANOTHER EXAMPLE: DOCUMENT STATE
// ============================================================================

interface DocumentState {
    fun publish(doc: Document)
    fun archive(doc: Document)
    fun delete(doc: Document)
    fun getState(): String
}

class DraftState : DocumentState {
    override fun publish(doc: Document) {
        println("📄 Publishing document from DRAFT")
        doc.state = PublishedState()
    }

    override fun archive(doc: Document) {
        println("❌ Cannot archive DRAFT document")
    }

    override fun delete(doc: Document) {
        println("🗑️ Deleting DRAFT document")
        doc.state = DeletedState()
    }

    override fun getState(): String = "DRAFT ✏️"
}

class PublishedState : DocumentState {
    override fun publish(doc: Document) {
        println("❌ Document is already PUBLISHED")
    }

    override fun archive(doc: Document) {
        println("📄 Archiving PUBLISHED document")
        doc.state = ArchivedState()
    }

    override fun delete(doc: Document) {
        println("❌ Cannot delete PUBLISHED document")
    }

    override fun getState(): String = "PUBLISHED ✅"
}

class ArchivedState : DocumentState {
    override fun publish(doc: Document) {
        println("❌ Cannot publish ARCHIVED document")
    }

    override fun archive(doc: Document) {
        println("❌ Document is already ARCHIVED")
    }

    override fun delete(doc: Document) {
        println("🗑️ Deleting ARCHIVED document")
        doc.state = DeletedState()
    }

    override fun getState(): String = "ARCHIVED 📦"
}

class DeletedState : DocumentState {
    override fun publish(doc: Document) {
        println("❌ Cannot publish DELETED document")
    }

    override fun archive(doc: Document) {
        println("❌ Cannot archive DELETED document")
    }

    override fun delete(doc: Document) {
        println("❌ Document is already DELETED")
    }

    override fun getState(): String = "DELETED 🗑️"
}

class Document(initialState: DocumentState = DraftState()) {
    var state: DocumentState = initialState
        set(value) {
            field = value
            println("   ➜ New state: ${value.getState()}\n")
        }

    private val title = "My Document"

    fun publish() {
        println("📄 Attempting to publish: $title (Current: ${state.getState()})")
        state.publish(this)
    }

    fun archive() {
        println("📄 Attempting to archive: $title (Current: ${state.getState()})")
        state.archive(this)
    }

    fun delete() {
        println("📄 Attempting to delete: $title (Current: ${state.getState()})")
        state.delete(this)
    }

    fun getStatus(): String = state.getState()
}

// ============================================================================
// ANOTHER EXAMPLE: PLAYER STATE
// ============================================================================

interface PlayerState {
    fun play(player: MediaPlayer)
    fun pause(player: MediaPlayer)
    fun stop(player: MediaPlayer)
    fun next(player: MediaPlayer)
    fun getState(): String
}

class PlayingState : PlayerState {
    override fun play(player: MediaPlayer) {
        println("❌ Already playing")
    }

    override fun pause(player: MediaPlayer) {
        println("⏸️ Pausing playback")
        player.state = PausedState()
    }

    override fun stop(player: MediaPlayer) {
        println("⏹️ Stopping playback")
        player.state = StoppedState()
    }

    override fun next(player: MediaPlayer) {
        println("⏭️ Skipping to next track")
    }

    override fun getState(): String = "PLAYING ▶️"
}

class PausedState : PlayerState {
    override fun play(player: MediaPlayer) {
        println("▶️ Resuming playback")
        player.state = PlayingState()
    }

    override fun pause(player: MediaPlayer) {
        println("❌ Already paused")
    }

    override fun stop(player: MediaPlayer) {
        println("⏹️ Stopping playback")
        player.state = StoppedState()
    }

    override fun next(player: MediaPlayer) {
        println("⏭️ Skipping to next track")
    }

    override fun getState(): String = "PAUSED ⏸️"
}

class StoppedState : PlayerState {
    override fun play(player: MediaPlayer) {
        println("▶️ Starting playback")
        player.state = PlayingState()
    }

    override fun pause(player: MediaPlayer) {
        println("❌ Nothing to pause")
    }

    override fun stop(player: MediaPlayer) {
        println("❌ Already stopped")
    }

    override fun next(player: MediaPlayer) {
        println("❌ Cannot skip while stopped")
    }

    override fun getState(): String = "STOPPED ⏹️"
}

class MediaPlayer(initialState: PlayerState = StoppedState()) {
    var state: PlayerState = initialState

    fun play() {
        state.play(this)
    }

    fun pause() {
        state.pause(this)
    }

    fun stop() {
        state.stop(this)
    }

    fun next() {
        state.next(this)
    }

    fun getStatus(): String = state.getState()
}

// ============================================================================
// DEMO
// ============================================================================

fun demonstrateState() {
    println("=== State Pattern Demo ===\n")

    // Example 1: Traffic Light
    println("--- Traffic Light State Machine ---")
    val trafficLight = TrafficLight()

    for (i in 1..5) {
        println("\n🚦 Change $i:")
        trafficLight.change()
    }

    // Example 2: Document State
    println("\n--- Document Workflow ---")
    val document = Document()

    println("\n🔄 Workflow transitions:")
    document.publish()
    document.publish()
    document.archive()
    document.delete()
    document.delete()

    // Example 3: Media Player
    println("\n--- Media Player States ---")
    val player = MediaPlayer()

    println("Current: ${player.getStatus()}")
    player.play()
    println("Current: ${player.getStatus()}")

    player.next()

    player.pause()
    println("Current: ${player.getStatus()}")

    player.play()
    println("Current: ${player.getStatus()}")

    player.stop()
    println("Current: ${player.getStatus()}")

    player.pause()
}

// Custom type alias for Context interface
typealias Context = TrafficLight

