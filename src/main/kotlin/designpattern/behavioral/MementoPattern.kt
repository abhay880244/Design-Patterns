package designpattern.behavioral

/**
 * Memento Pattern Implementation
 *
 * WHAT IS THE MEMENTO PATTERN?
 * Captures and externalizes an object's internal state without violating encapsulation.
 * Allows restoring the object to this state later.
 *
 * KEY CONCEPTS:
 * - Originator: Object whose state needs to be saved
 * - Memento: Snapshot of originator's state (immutable)
 * - Caretaker: Manages mementos (no knowledge of state)
 * - Undo/Redo: Primary use case
 *
 * REAL-WORLD ANALOGY:
 * Saving video game progress:
 *   - Game (Originator) has current state
 *   - Save file (Memento) captures snapshot
 *   - Game manager (Caretaker) manages save files
 *   - Can restore to previous save point
 *
 * BENEFITS:
 * - Preserves encapsulation
 * - Supports undo/redo
 * - Simple to implement
 * - Restores object to previous state
 *
 * WHEN TO USE:
 * - Undo/Redo functionality
 * - Checkpointing and recovery
 * - Save/Load game state
 * - Transaction rollback
 */

// ============================================================================
// MEMENTO
// ============================================================================

data class GameMemento(
    val playerLevel: Int,
    val playerHealth: Int,
    val playerScore: Int,
    val position: Pair<Int, Int>,
    val inventory: List<String>
) {
    fun getDescription(): String {
        return "Level: $playerLevel, Health: $playerHealth, Score: $playerScore, Position: $position"
    }
}

// ============================================================================
// ORIGINATOR
// ============================================================================

class GameState {
    var playerLevel: Int = 1
        private set
    var playerHealth: Int = 100
        private set
    var playerScore: Int = 0
        private set
    var position: Pair<Int, Int> = Pair(0, 0)
        private set
    var inventory: List<String> = listOf()
        private set

    fun playGame(levels: Int) {
        playerLevel += levels
        playerHealth -= (levels * 10)
        playerScore += (levels * 100)
        position = Pair(position.first + levels, position.second + levels)
        inventory = inventory + "Item_Level_$playerLevel"
        println("🎮 Played $levels levels - Level: $playerLevel, Health: $playerHealth, Score: $playerScore")
    }

    fun saveGame(): GameMemento {
        println("💾 Saving game state...")
        return GameMemento(playerLevel, playerHealth, playerScore, position, inventory)
    }

    fun loadGame(memento: GameMemento) {
        println("📂 Loading game state from memento...")
        playerLevel = memento.playerLevel
        playerHealth = memento.playerHealth
        playerScore = memento.playerScore
        position = memento.position
        inventory = memento.inventory
        println("✅ Game state restored: ${memento.getDescription()}")
    }

    fun getStatus(): String {
        return "Level: $playerLevel, Health: $playerHealth, Score: $playerScore, Position: $position, Inventory: $inventory"
    }
}

// ============================================================================
// CARETAKER
// ============================================================================

class GameSaveManager {
    private val saveFiles = mutableMapOf<String, GameMemento>()
    private var currentSlot = "slot1"

    fun save(slotName: String, gameState: GameState) {
        saveFiles[slotName] = gameState.saveGame()
        currentSlot = slotName
        println("✅ Game saved to slot: $slotName")
    }

    fun load(slotName: String, gameState: GameState): Boolean {
        return if (saveFiles.containsKey(slotName)) {
            gameState.loadGame(saveFiles[slotName]!!)
            currentSlot = slotName
            true
        } else {
            println("❌ Save slot not found: $slotName")
            false
        }
    }

    fun listSaves() {
        println("\n📋 Available Save Files:")
        saveFiles.forEach { (slot, memento) ->
            println("  [$slot] - ${memento.getDescription()}")
        }
    }

    fun deleteSave(slotName: String) {
        if (saveFiles.remove(slotName) != null) {
            println("🗑️ Deleted save slot: $slotName")
        } else {
            println("❌ Save slot not found: $slotName")
        }
    }
}

// ============================================================================
// DEMO
// ============================================================================

fun demonstrateMemento() {
    println("=== Memento Pattern Demo ===\n")

    // Example 1: Game Save/Load
    println("--- Game Save/Load System ---")
    val gameState = GameState()
    val saveManager = GameSaveManager()

    println("Initial game state: ${gameState.getStatus()}\n")

    gameState.playGame(1)
    saveManager.save("slot1", gameState)

    gameState.playGame(2)
    saveManager.save("slot2", gameState)

    gameState.playGame(3)
    println("Current: ${gameState.getStatus()}")

    saveManager.listSaves()

    println("\n📂 Loading from slot1:")
    saveManager.load("slot1", gameState)
    println("Loaded: ${gameState.getStatus()}")
}
