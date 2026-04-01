package designpattern

import designpattern.behavioral.*
import designpattern.concurrency.*
import designpattern.creational.*
import designpattern.structural.*

/**
 * Main entry point for Design Patterns demonstrations
 *
 * This program demonstrates various design patterns organized by category:
 * - CREATIONAL: How objects are created
 * - STRUCTURAL: How classes and objects are composed
 * - BEHAVIORAL: How classes and objects interact
 * - CONCURRENCY: Multi-threaded patterns
 */

fun main() {
    println("╔════════════════════════════════════════════════════════════════════════════════╗")
    println("║                        DESIGN PATTERNS DEMONSTRATIONS                         ║")
    println("╚════════════════════════════════════════════════════════════════════════════════╝")

    val patterns = listOf(
        // Creational Patterns
        Pair("CREATIONAL", listOf(
            "1. Factory Pattern" to { demonstrateCreationalFactory() },
            "2. Abstract Factory Pattern" to { demonstrateAbstractFactory() },
            "3. Singleton Pattern" to { demonstrateCreationalSingleton() },
            "4. Builder Pattern" to { demonstrateBuilder() },
            "5. Prototype Pattern" to { demonstratePrototype() }
        )),

        // Structural Patterns
        Pair("STRUCTURAL", listOf(
            "1. Adapter Pattern" to { demonstrateAdapter() },
            "2. Bridge Pattern" to { demonstrateBridge() },
            "3. Composite Pattern" to { demonstrateComposite() },
            "4. Decorator Pattern" to { demonstrateDecorator() },
            "5. Facade Pattern" to { demonstrateFacade() },
            "6. Flyweight Pattern" to { demonstrateFlyweight() },
            "7. Proxy Pattern" to { demonstrateProxy() }
        )),

        // Behavioral Patterns
        Pair("BEHAVIORAL", listOf(
            "1. Observer Pattern" to { demonstrateObserver() },
            "2. Dependency Injection Pattern" to { demonstrateDependencyInjection() },
            "3. Strategy Pattern" to { demonstrateStrategy() },
            "4. Command Pattern" to { demonstrateCommand() },
            "5. State Pattern" to { demonstrateState() },
            "6. Template Method Pattern" to { demonstrateTemplateMethod() },
            "7. Chain of Responsibility Pattern" to { demonstrateChainOfResponsibility() },
            "8. Iterator Pattern" to { demonstrateIterator() },
            "9. Visitor Pattern" to { demonstrateVisitor() },
            "10. Mediator Pattern" to { demonstrateMediator() },
            "11. Memento Pattern" to { demonstrateMemento() },
            "12. Interpreter Pattern" to { demonstrateInterpreter() }
        )),

        // Concurrency Patterns
        Pair("CONCURRENCY", listOf(
            "1. Volatile Simple Explanation" to { demonstrateVolatile() },
            "2. Producer-Consumer Pattern" to { demonstrateProducerConsumer() },
            "3. Active Object Pattern" to { demonstrateActiveObject() }
        ))
    )

    println("\n📚 Available Design Patterns:\n")
    patterns.forEach { (category, patternsList) ->
        println("$category:")
        patternsList.forEach { (name, _) ->
            println("  - $name")
        }
        println()
    }

    println("\nSelect pattern to demonstrate (or enter 'all' to run all):")
    println("Format: CATEGORY.NUMBER (e.g., CREATIONAL.1)")
    print("> ")

    val choice = readLine()?.uppercase()?.trim() ?: return

    when {
        choice == "ALL" -> {
            patterns.forEach { (_, patternsList) ->
                patternsList.forEach { (_, demo) ->
                    demo()
                    println("\n" + "=".repeat(80) + "\n")
                }
            }
        }
        choice.contains(".") -> {
            val parts = choice.split(".")
            val category = parts[0]
            val patternNum = parts.getOrNull(1)?.toIntOrNull() ?: return

            val categoryData = patterns.find { it.first == category }
            if (categoryData != null) {
                val pattern = categoryData.second.getOrNull(patternNum - 1)
                if (pattern != null) {
                    pattern.second()
                } else {
                    println("❌ Pattern not found")
                }
            } else {
                println("❌ Category not found")
            }
        }
        else -> {
            println("❌ Invalid input")
        }
    }
}

// Implementation functions for all patterns
fun demonstrateCreationalFactory() {
    println("=== Factory Pattern ===")
    vehicleFactory.create(Vehicle.TYPE.CAR).also { println("Created: ${it::class.simpleName}") }
    vehicleFactory.create(Vehicle.TYPE.BIKE).also { println("Created: ${it::class.simpleName}") }
}

fun demonstrateCreationalSingleton() {
    println("=== Singleton Pattern ===")
    DatabaseConnection.connect()
    DatabaseConnection.disconnect()
    val logger = Logger.getInstance()
    logger.log("Test message")
}

fun demonstrateBuilder() {
    println("=== Builder Pattern ===")
    val businessLayer = BusinessLayer()
    businessLayer.main()
}

fun demonstrateObserver() {
    println("=== Observer Pattern ===\n")

    val stock = Stock("GOOGL")
    stock.attach(NewsAgency("World News"))
    stock.attach(Investor("Alice"))
    stock.attach(Investor("Bob"))

    stock.setPrice(100.0)
    stock.setPrice(105.0)
    stock.setPrice(98.0)
}

fun demonstrateDependencyInjection() {
    println("=== Dependency Injection Pattern ===\n")

    val smtpEmail = SmtpEmailService()
    val mysqlDb = MySQLDatabase()
    val consoleLogger = ConsoleLogger()

    println("--- Constructor Injection ---")
    val userService = UserService(smtpEmail, mysqlDb, consoleLogger)
    userService.registerUser("john@example.com", "John Doe")
    userService.getUser()

    println("\n--- Property Injection ---")
    val notificationService = NotificationService()
    notificationService.emailService = GmailEmailService()
    notificationService.logger = ConsoleLogger()
    notificationService.notify("jane@example.com", "Hello Jane!")

    println("\n--- Setter Injection ---")
    val reportService = ReportService()
    reportService.setDatabase(MongoDatabase())
    reportService.setLogger(ConsoleLogger())
    reportService.generateReport()

    println("\n--- Service Locator/Container ---")
    val container = DIContainer()
    container.register("emailService", SmtpEmailService())
    container.register("database", MySQLDatabase())
    container.register("logger", ConsoleLogger())

    println("Services registered and available in container")
}

fun demonstrateVolatile() {
    println("=== Volatile and Thread Visibility ===\n")

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

// Additional pattern functions
fun demonstrateStrategy() {
    designpattern.behavioral.demonstrateStrategy()
}

fun demonstrateCommand() {
    designpattern.behavioral.demonstrateCommand()
}

fun demonstrateState() {
    designpattern.behavioral.demonstrateState()
}

fun demonstrateTemplateMethod() {
    designpattern.behavioral.demonstrateTemplateMethod()
}

fun demonstrateChainOfResponsibility() {
    designpattern.behavioral.demonstrateChainOfResponsibility()
}

fun demonstrateIterator() {
    designpattern.behavioral.demonstrateIterator()
}

fun demonstrateVisitor() {
    designpattern.behavioral.demonstrateVisitor()
}

fun demonstrateMediator() {
    designpattern.behavioral.demonstrateMediator()
}

fun demonstrateMemento() {
    designpattern.behavioral.demonstrateMemento()
}

fun demonstrateInterpreter() {
    designpattern.behavioral.demonstrateInterpreter()
}
