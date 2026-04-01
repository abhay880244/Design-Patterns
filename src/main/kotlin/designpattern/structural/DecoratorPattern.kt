package designpattern.structural

/**
 * Decorator Pattern Implementation
 *
 * WHAT IS THE DECORATOR PATTERN?
 * Attaches additional responsibilities to an object dynamically.
 * Decorators provide a flexible alternative to subclassing for extending functionality.
 *
 * KEY CONCEPTS:
 * - Component: Original object interface
 * - Concrete Component: Original object
 * - Decorator: Wraps component and adds functionality
 * - Each decorator wraps another decorator/component
 *
 * REAL-WORLD ANALOGY:
 * Decorating a Christmas tree:
 *   - Tree (Component) is the original object
 *   - Add lights (Decorator 1)
 *   - Add tinsel (Decorator 2)
 *   - Add ornaments (Decorator 3)
 *   - Each decoration wraps the previous one
 *
 * BENEFITS:
 * - More flexible than subclassing
 * - Add responsibilities at runtime
 * - Combine multiple decorators
 * - Follows Single Responsibility Principle
 * - Follows Open/Closed Principle
 *
 * WHEN TO USE:
 * - Adding features to objects without modifying them
 * - Need multiple combinations of features
 * - Subclassing would create too many classes
 */

// ============================================================================
// COMPONENT INTERFACE
// ============================================================================

interface Coffee {
    fun getDescription(): String
    fun getCost(): Double
}

// ============================================================================
// CONCRETE COMPONENT
// ============================================================================

class SimpleCoffee : Coffee {
    override fun getDescription(): String = "Simple Coffee"
    override fun getCost(): Double = 2.0
}

// ============================================================================
// DECORATOR ABSTRACT CLASS
// ============================================================================

abstract class CoffeeDecorator(protected val coffee: Coffee) : Coffee {
    override fun getDescription(): String = coffee.getDescription()
    override fun getCost(): Double = coffee.getCost()
}

// ============================================================================
// CONCRETE DECORATORS
// ============================================================================

class MilkDecorator(coffee: Coffee) : CoffeeDecorator(coffee) {
    override fun getDescription(): String = "${coffee.getDescription()}, Milk"
    override fun getCost(): Double = coffee.getCost() + 0.50
}

class SugarDecorator(coffee: Coffee) : CoffeeDecorator(coffee) {
    override fun getDescription(): String = "${coffee.getDescription()}, Sugar"
    override fun getCost(): Double = coffee.getCost() + 0.25
}

class WhippedCreamDecorator(coffee: Coffee) : CoffeeDecorator(coffee) {
    override fun getDescription(): String = "${coffee.getDescription()}, Whipped Cream"
    override fun getCost(): Double = coffee.getCost() + 1.00
}

class VanillaDecorator(coffee: Coffee) : CoffeeDecorator(coffee) {
    override fun getDescription(): String = "${coffee.getDescription()}, Vanilla"
    override fun getCost(): Double = coffee.getCost() + 0.75
}

class CaramelDecorator(coffee: Coffee) : CoffeeDecorator(coffee) {
    override fun getDescription(): String = "${coffee.getDescription()}, Caramel"
    override fun getCost(): Double = coffee.getCost() + 0.60
}

// ============================================================================
// ANOTHER EXAMPLE: UI COMPONENT DECORATORS
// ============================================================================

interface UIComponent {
    fun render(): String
}

class SimpleButton : UIComponent {
    override fun render(): String = "Button"
}

abstract class UIComponentDecorator(protected val component: UIComponent) : UIComponent {
    override fun render(): String = component.render()
}

class BorderDecorator(component: UIComponent) : UIComponentDecorator(component) {
    override fun render(): String = "║ ${component.render()} ║"
}

class ShadowDecorator(component: UIComponent) : UIComponentDecorator(component) {
    override fun render(): String = "▓ ${component.render()} ▓"
}

class HighlightDecorator(component: UIComponent) : UIComponentDecorator(component) {
    override fun render(): String = "► ${component.render()} ◄"
}

class ColorDecorator(component: UIComponent, private val color: String) : UIComponentDecorator(component) {
    override fun render(): String = "[$color] ${component.render()} [/color]"
}

// ============================================================================
// DEMO
// ============================================================================

fun demonstrateDecorator() {
    println("=== Decorator Pattern Demo ===\n")

    // Example 1: Coffee Decorators
    println("--- Coffee Decorator Example ---")

    val simpleCoffee = SimpleCoffee()
    println("${simpleCoffee.getDescription()}: \$${simpleCoffee.getCost()}")

    val coffeeWithMilk = MilkDecorator(simpleCoffee)
    println("${coffeeWithMilk.getDescription()}: \$${coffeeWithMilk.getCost()}")

    val coffeeWithMilkAndSugar = SugarDecorator(coffeeWithMilk)
    println("${coffeeWithMilkAndSugar.getDescription()}: \$${coffeeWithMilkAndSugar.getCost()}")

    val deluxeCoffee = CaramelDecorator(
        WhippedCreamDecorator(
            VanillaDecorator(
                MilkDecorator(simpleCoffee)
            )
        )
    )
    println("${deluxeCoffee.getDescription()}: \$${deluxeCoffee.getCost()}\n")

    // Example 2: UI Component Decorators
    println("--- UI Component Decorator Example ---")

    val button = SimpleButton()
    println("Simple: ${button.render()}")

    val borderedButton = BorderDecorator(button)
    println("Bordered: ${borderedButton.render()}")

    val shadowButton = ShadowDecorator(button)
    println("Shadow: ${shadowButton.render()}")

    val decoratedButton = HighlightDecorator(
        ColorDecorator(
            BorderDecorator(button),
            "RED"
        )
    )
    println("Fully Decorated: ${decoratedButton.render()}\n")

    // Example 3: Multiple decorators
    println("--- Building Complex Components ---")

    val complexButton = ShadowDecorator(
        ColorDecorator(
            BorderDecorator(
                HighlightDecorator(button)
            ),
            "BLUE"
        )
    )
    println("Complex Button: ${complexButton.render()}")
}

