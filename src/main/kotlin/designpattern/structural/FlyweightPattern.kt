package designpattern.structural

/**
 * Flyweight Pattern Implementation
 *
 * WHAT IS THE FLYWEIGHT PATTERN?
 * Uses sharing to support a large number of fine-grained objects efficiently.
 * Reduces memory usage by sharing common state among multiple objects.
 *
 * KEY CONCEPTS:
 * - Intrinsic State: Shared and immutable (shared among all instances)
 * - Extrinsic State: Unique to each object (can be passed as parameter)
 * - Flyweight: Stores intrinsic state
 * - Flyweight Factory: Creates and caches flyweights
 *
 * REAL-WORLD ANALOGY:
 * Text editor with character objects:
 *   - Each character (A, B, C, etc.) is a flyweight
 *   - Only one instance of each character type exists
 *   - Position, color, font-size (extrinsic) are passed separately
 *   - Saves huge memory in large documents
 *
 * BENEFITS:
 * - Significantly reduces memory usage
 * - Improves performance with many similar objects
 * - Centralizes creation logic
 * - Trade-off: slight increase in CPU usage
 *
 * WHEN TO USE:
 * - Large number of similar objects
 * - Memory is a concern
 * - Objects have intrinsic state that can be shared
 * - Games (particle systems, terrain), text editors, web browsers
 */

// ============================================================================
// FLYWEIGHT INTERFACE
// ============================================================================

interface TerrainType {
    fun render(x: Int, y: Int)
    fun getTextureName(): String
}

// ============================================================================
// CONCRETE FLYWEIGHTS
// ============================================================================

class GrassTerrainType : TerrainType {
    override fun render(x: Int, y: Int) {
        println("🌱 Grass at ($x, $y)")
    }

    override fun getTextureName(): String = "grass.png"
}

class WaterTerrainType : TerrainType {
    override fun render(x: Int, y: Int) {
        println("💧 Water at ($x, $y)")
    }

    override fun getTextureName(): String = "water.png"
}

class RockTerrainType : TerrainType {
    override fun render(x: Int, y: Int) {
        println("🪨 Rock at ($x, $y)")
    }

    override fun getTextureName(): String = "rock.png"
}

class SandTerrainType : TerrainType {
    override fun render(x: Int, y: Int) {
        println("🏜️ Sand at ($x, $y)")
    }

    override fun getTextureName(): String = "sand.png"
}

// ============================================================================
// FLYWEIGHT FACTORY
// ============================================================================

class TerrainTypeFactory {
    private val terrainTypes = mutableMapOf<String, TerrainType>()

    fun getTerrainType(type: String): TerrainType {
        return terrainTypes.getOrPut(type) {
            when (type.lowercase()) {
                "grass" -> {
                    println("✨ Creating Grass Terrain Type (Flyweight)")
                    GrassTerrainType()
                }
                "water" -> {
                    println("✨ Creating Water Terrain Type (Flyweight)")
                    WaterTerrainType()
                }
                "rock" -> {
                    println("✨ Creating Rock Terrain Type (Flyweight)")
                    RockTerrainType()
                }
                "sand" -> {
                    println("✨ Creating Sand Terrain Type (Flyweight)")
                    SandTerrainType()
                }
                else -> throw IllegalArgumentException("Unknown terrain type: $type")
            }
        }
    }

    fun printStats() {
        println("\n📊 Flyweight Factory Stats:")
        println("Total cached flyweights: ${terrainTypes.size}")
        terrainTypes.forEach { (type, _) ->
            println("  - $type")
        }
    }
}

// ============================================================================
// EXTRINSIC STATE (Position, dimensions)
// ============================================================================

data class TerrainCell(
    val x: Int,
    val y: Int,
    val terrainType: TerrainType
) {
    fun render() {
        terrainType.render(x, y)
    }
}

// ============================================================================
// GAME MAP (Uses flyweights)
// ============================================================================

class GameMap(
    private val width: Int,
    private val height: Int,
    private val factory: TerrainTypeFactory
) {
    private val terrain = Array(width) { Array(height) { TerrainCell(0, 0, factory.getTerrainType("grass")) } }

    fun setTerrain(x: Int, y: Int, type: String) {
        if (x in 0 until width && y in 0 until height) {
            terrain[x][y] = TerrainCell(x, y, factory.getTerrainType(type))
        }
    }

    fun render() {
        println("\n🗺️ Game Map ($width x $height):")
        for (x in 0 until width) {
            for (y in 0 until height) {
                terrain[x][y].render()
            }
        }
    }

    fun printMemoryUsage() {
        println("\n💾 Memory Usage:")
        println("Total cells: ${width * height}")
        println("Unique flyweights: ${factory}")
        factory.printStats()
    }
}

// ============================================================================
// ANOTHER EXAMPLE: TEXT DOCUMENT WITH CHARACTER FLYWEIGHTS
// ============================================================================

interface CharacterFlyweight {
    fun display(fontSize: Int, color: String)
    fun getCharacter(): Char
}

class CharacterImpl(private val character: Char) : CharacterFlyweight {
    override fun display(fontSize: Int, color: String) {
        println("$character [Size: $fontSize, Color: $color]")
    }

    override fun getCharacter(): Char = character
}

class CharacterFactory {
    private val characters = mutableMapOf<Char, CharacterFlyweight>()

    fun getCharacter(char: Char): CharacterFlyweight {
        return characters.getOrPut(char) {
            println("✨ Creating flyweight for character: $char")
            CharacterImpl(char)
        }
    }

    fun printCacheSize() {
        println("Cache size: ${characters.size}")
    }
}

// ============================================================================
// DEMO
// ============================================================================

fun demonstrateFlyweight() {
    println("=== Flyweight Pattern Demo ===\n")

    // Example 1: Game Terrain
    println("--- Game Terrain Flyweights ---")
    val factory = TerrainTypeFactory()

    val gameMap = GameMap(5, 5, factory)
    gameMap.setTerrain(0, 0, "water")
    gameMap.setTerrain(1, 1, "water")
    gameMap.setTerrain(2, 2, "grass")
    gameMap.setTerrain(3, 3, "rock")
    gameMap.setTerrain(4, 4, "sand")

    gameMap.render()
    gameMap.printMemoryUsage()

    // Example 2: Character Flyweights
    println("\n--- Character Flyweights in Text Document ---")
    val charFactory = CharacterFactory()

    val text = "Hello"
    println("Creating text: $text")
    for (char in text) {
        charFactory.getCharacter(char)
    }

    println("\nDisplaying text:")
    for (char in text) {
        val flyweight = charFactory.getCharacter(char)
        flyweight.display(12, "BLACK")
    }

    println("\nReusing flyweights for same characters:")
    charFactory.printCacheSize()

    val text2 = "Hello"
    for (char in text2) {
        charFactory.getCharacter(char)
    }
    charFactory.printCacheSize()
}

