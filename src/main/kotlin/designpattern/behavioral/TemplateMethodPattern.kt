package designpattern.behavioral

/**
 * Template Method Pattern Implementation
 *
 * WHAT IS THE TEMPLATE METHOD PATTERN?
 * Defines the skeleton of an algorithm in a base class but lets subclasses
 * override specific steps without changing the algorithm's structure.
 *
 * KEY CONCEPTS:
 * - Template Method: Defines overall algorithm structure
 * - Abstract Class: Contains template method and abstract steps
 * - Concrete Class: Implements specific steps
 * - Invariant steps: Same in all subclasses
 * - Variant steps: Different in each subclass
 *
 * REAL-WORLD ANALOGY:
 * Recipe for making coffee:
 *   1. Boil water (same)
 *   2. Brew coffee (same)
 *   3. Pour in cup (same)
 *   4. Add ingredients (varies) - milk, sugar, cream, etc.
 *   5. Serve (same)
 *
 * BENEFITS:
 * - Code reuse
 * - Enforces consistent algorithm structure
 * - Follows Hollywood Principle ("Don't call us, we'll call you")
 * - Easy to add new variations
 *
 * WHEN TO USE:
 * - Multiple classes with similar algorithms
 * - Want to avoid code duplication
 * - Control subclass extension at specific points
 */

// ============================================================================
// ABSTRACT CLASS WITH TEMPLATE METHOD
// ============================================================================

abstract class Recipe {
    // Template Method - Final to prevent overriding
    fun prepareRecipe() {
        println("\n🍳 Making ${getRecipeName()}")
        println("─".repeat(40))

        gatherIngredients()
        println()
        prepareIngredients()
        println()
        cook()
        println()
        plate()
        println()
        serve()

        println("─".repeat(40))
        println("✅ ${getRecipeName()} is ready to eat!\n")
    }

    // Template method steps
    protected open fun gatherIngredients() {
        println("📦 Gathering basic ingredients...")
    }

    protected abstract fun prepareIngredients()
    protected abstract fun cook()

    protected open fun plate() {
        println("🍽️ Plating the dish")
    }

    protected open fun serve() {
        println("🍴 Serving the dish")
    }

    protected abstract fun getRecipeName(): String
}

// ============================================================================
// CONCRETE IMPLEMENTATIONS
// ============================================================================

class PastaRecipe : Recipe() {
    override fun gatherIngredients() {
        super.gatherIngredients()
        println("📦 Gathering pasta ingredients: pasta, olive oil, garlic, tomatoes")
    }

    override fun prepareIngredients() {
        println("✂️ Chopping garlic and tomatoes")
        println("✂️ Measuring pasta")
    }

    override fun cook() {
        println("🔥 Boiling water")
        println("🔥 Cooking pasta for 10 minutes")
        println("🔥 Preparing tomato sauce")
        println("🔥 Combining pasta and sauce")
    }

    override fun getRecipeName(): String = "Pasta Carbonara"
}

class OmeletteRecipe : Recipe() {
    override fun gatherIngredients() {
        super.gatherIngredients()
        println("📦 Gathering omelette ingredients: eggs, cheese, butter, herbs")
    }

    override fun prepareIngredients() {
        println("✂️ Cracking eggs into a bowl")
        println("✂️ Whisking eggs")
        println("✂️ Shredding cheese")
        println("✂️ Chopping herbs")
    }

    override fun cook() {
        println("🔥 Heating butter in pan")
        println("🔥 Pouring eggs into pan")
        println("🔥 Adding cheese and herbs")
        println("🔥 Folding omelette")
    }

    override fun getRecipeName(): String = "Cheese & Herb Omelette"
}

class CakeRecipe : Recipe() {
    override fun gatherIngredients() {
        super.gatherIngredients()
        println("📦 Gathering cake ingredients: flour, sugar, eggs, butter, vanilla")
    }

    override fun prepareIngredients() {
        println("✂️ Sifting flour")
        println("✂️ Measuring sugar and butter")
        println("✂️ Cracking eggs")
    }

    override fun cook() {
        println("🔥 Preheating oven to 350°F")
        println("🔥 Mixing ingredients")
        println("🔥 Pouring batter into pan")
        println("🔥 Baking for 30 minutes")
        println("🔥 Cooling the cake")
    }

    override fun plate() {
        println("🍽️ Cutting cake into slices")
        println("🍽️ Adding frosting")
    }

    override fun getRecipeName(): String = "Vanilla Cake"
}

// ============================================================================
// ANOTHER EXAMPLE: DATA PROCESSING TEMPLATE
// ============================================================================

abstract class DataProcessor {
    fun process() {
        println("\n📊 Processing ${getDataSourceName()}")
        println("─".repeat(40))

        readData()
        validateData()
        transformData()
        saveData()

        println("─".repeat(40))
        println("✅ Data processing complete!\n")
    }

    protected abstract fun readData()

    protected open fun validateData() {
        println("✔️ Validating data...")
    }

    protected abstract fun transformData()
    protected abstract fun saveData()

    protected abstract fun getDataSourceName(): String
}

class CSVDataProcessor : DataProcessor() {
    override fun readData() {
        println("📂 Reading CSV file...")
        println("📂 Parsing rows and columns...")
    }

    override fun transformData() {
        println("🔄 Converting CSV data to objects...")
        println("🔄 Normalizing field values...")
    }

    override fun saveData() {
        println("💾 Saving to database...")
        println("💾 Data saved successfully")
    }

    override fun getDataSourceName(): String = "CSV File"
}

class JSONDataProcessor : DataProcessor() {
    override fun readData() {
        println("📂 Reading JSON file...")
        println("📂 Parsing JSON structure...")
    }

    override fun transformData() {
        println("🔄 Converting JSON to objects...")
        println("🔄 Mapping fields...")
    }

    override fun saveData() {
        println("💾 Saving to cache...")
        println("💾 Data cached successfully")
    }

    override fun getDataSourceName(): String = "JSON File"
}

class XMLDataProcessor : DataProcessor() {
    override fun readData() {
        println("📂 Reading XML file...")
        println("📂 Parsing XML nodes...")
    }

    override fun validateData() {
        super.validateData()
        println("✔️ Validating XML schema...")
    }

    override fun transformData() {
        println("🔄 Converting XML to objects...")
        println("🔄 Extracting attributes and values...")
    }

    override fun saveData() {
        println("💾 Saving to file system...")
        println("💾 Files saved successfully")
    }

    override fun getDataSourceName(): String = "XML File"
}

// ============================================================================
// DEMO
// ============================================================================

fun demonstrateTemplateMethod() {
    println("=== Template Method Pattern Demo ===")

    // Example 1: Recipe Templates
    println("\n--- Recipe Templates ---")
    val recipes: List<Recipe> = listOf(
        PastaRecipe(),
        OmeletteRecipe(),
        CakeRecipe()
    )

    recipes.forEach { it.prepareRecipe() }

    // Example 2: Data Processing Templates
    println("--- Data Processing Templates ---")
    val processors: List<DataProcessor> = listOf(
        CSVDataProcessor(),
        JSONDataProcessor(),
        XMLDataProcessor()
    )

    processors.forEach { it.process() }
}

