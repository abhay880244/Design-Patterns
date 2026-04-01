package designpattern.creational

/**
 * Abstract Factory Pattern Implementation
 *
 * WHAT IS THE ABSTRACT FACTORY PATTERN?
 * Provides an interface for creating families of related or dependent objects
 * without specifying their concrete classes.
 *
 * KEY CONCEPTS:
 * - Abstract Factory: Interface for creating related objects
 * - Concrete Factory: Implements the abstract factory for specific families
 * - Abstract Product: Interface for a family of products
 * - Concrete Product: Implements the abstract product
 *
 * REAL-WORLD ANALOGY:
 * A furniture factory that can produce different styles:
 *   - Modern furniture factory creates modern chair, table, sofa
 *   - Victorian furniture factory creates victorian chair, table, sofa
 *   - Each factory knows how to create its own style
 *
 * BENEFITS:
 * - Ensures objects from the same family work together
 * - Decouples client from concrete classes
 * - Makes it easy to swap between different families
 * - Easier to maintain and extend
 *
 * WHEN TO USE:
 * - Multiple families of related products
 * - System should be independent of how products are created
 * - UI component libraries (different themes)
 * - Database drivers for different databases
 */

// ============================================================================
// ABSTRACT PRODUCTS
// ============================================================================

interface Button {
    fun render()
}

interface Checkbox {
    fun render()
}

interface TextBox {
    fun render()
}

// ============================================================================
// CONCRETE PRODUCTS - Windows Style
// ============================================================================

class WindowsButton : Button {
    override fun render() {
        println("🪟 Rendering Windows Button")
    }
}

class WindowsCheckbox : Checkbox {
    override fun render() {
        println("🪟 Rendering Windows Checkbox")
    }
}

class WindowsTextBox : TextBox {
    override fun render() {
        println("🪟 Rendering Windows TextBox")
    }
}

// ============================================================================
// CONCRETE PRODUCTS - Mac Style
// ============================================================================

class MacButton : Button {
    override fun render() {
        println("🍎 Rendering Mac Button")
    }
}

class MacCheckbox : Checkbox {
    override fun render() {
        println("🍎 Rendering Mac Checkbox")
    }
}

class MacTextBox : TextBox {
    override fun render() {
        println("🍎 Rendering Mac TextBox")
    }
}

// ============================================================================
// CONCRETE PRODUCTS - Linux Style
// ============================================================================

class LinuxButton : Button {
    override fun render() {
        println("🐧 Rendering Linux Button")
    }
}

class LinuxCheckbox : Checkbox {
    override fun render() {
        println("🐧 Rendering Linux Checkbox")
    }
}

class LinuxTextBox : TextBox {
    override fun render() {
        println("🐧 Rendering Linux TextBox")
    }
}

// ============================================================================
// ABSTRACT FACTORY
// ============================================================================

interface GUIFactory {
    fun createButton(): Button
    fun createCheckbox(): Checkbox
    fun createTextBox(): TextBox
}

// ============================================================================
// CONCRETE FACTORIES
// ============================================================================

class WindowsGUIFactory : GUIFactory {
    override fun createButton(): Button = WindowsButton()
    override fun createCheckbox(): Checkbox = WindowsCheckbox()
    override fun createTextBox(): TextBox = WindowsTextBox()
}

class MacGUIFactory : GUIFactory {
    override fun createButton(): Button = MacButton()
    override fun createCheckbox(): Checkbox = MacCheckbox()
    override fun createTextBox(): TextBox = MacTextBox()
}

class LinuxGUIFactory : GUIFactory {
    override fun createButton(): Button = LinuxButton()
    override fun createCheckbox(): Checkbox = LinuxCheckbox()
    override fun createTextBox(): TextBox = LinuxTextBox()
}

// ============================================================================
// APPLICATION CLASS (Uses Abstract Factory)
// ============================================================================

class Application(private val guiFactory: GUIFactory) {
    fun renderUI() {
        val button = guiFactory.createButton()
        val checkbox = guiFactory.createCheckbox()
        val textBox = guiFactory.createTextBox()

        println("\n--- Rendering UI ---")
        button.render()
        checkbox.render()
        textBox.render()
    }
}

// ============================================================================
// DEMO
// ============================================================================

fun main() {
    demonstrateAbstractFactory()
}
fun demonstrateAbstractFactory() {
    println("=== Abstract Factory Pattern Demo ===\n")

    // Windows UI
    println("Creating Windows UI:")
    val windowsFactory = WindowsGUIFactory()
    val windowsApp = Application(windowsFactory)
    windowsApp.renderUI()

    // Mac UI
    println("\nCreating Mac UI:")
    val macFactory = MacGUIFactory()
    val macApp = Application(macFactory)
    macApp.renderUI()

    // Linux UI
    println("\nCreating Linux UI:")
    val linuxFactory = LinuxGUIFactory()
    val linuxApp = Application(linuxFactory)
    linuxApp.renderUI()
}

