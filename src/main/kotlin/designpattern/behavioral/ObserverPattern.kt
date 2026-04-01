package designpattern.behavioral

/**
 * Observer Pattern Implementation
 *
 * WHAT IS THE OBSERVER PATTERN?
 * The Observer pattern defines a one-to-many relationship between objects so that when one
 * object (Subject) changes state, all its dependents (Observers) are notified automatically.
 *
 * KEY CONCEPTS:
 * - Subject: The object being observed (holds state and maintains a list of observers)
 * - Observer: Objects that want to be notified when the subject changes
 * - Notification: Automatic update when subject state changes
 *
 * REAL-WORLD ANALOGY:
 * Think of a newspaper subscription:
 *   - Newspaper = Subject (publishes news)
 *   - Subscribers = Observers (receive the news)
 *   - When news happens, all subscribers are automatically notified
 *
 * BENEFITS:
 * - Loose coupling: Subject doesn't need to know details about observers
 * - Dynamic relationships: Observers can be added/removed at runtime
 * - Automatic updates: No need for polling, changes push to observers
 *
 * WHEN TO USE:
 * - Event handling systems
 * - Model-View-Controller (MVC) architectures
 * - Stock price notifications
 * - UI updates when data changes
 */

// ============================================================================
// OBSERVER INTERFACE
// ============================================================================

/**
 * Observer interface that all observers must implement
 */
interface Observer {
    /**
     * Called when the subject's state changes
     * @param event The notification data
     */
    fun update(event: String)
}

// ============================================================================
// SUBJECT INTERFACE
// ============================================================================

/**
 * Subject interface that manages observers
 */
interface Subject {
    fun attach(observer: Observer)
    fun detach(observer: Observer)
    fun notifyObservers()
}

// ============================================================================
// CONCRETE IMPLEMENTATION: Stock Price Subject
// ============================================================================

/**
 * ConcreteSubject: Represents a stock that observers want to track
 */
class Stock(private val symbol: String) : Subject {
    private var price: Double = 0.0
    private val observers = mutableListOf<Observer>()

    override fun attach(observer: Observer) {
        if (!observers.contains(observer)) {
            observers.add(observer)
            println("📌 Observer attached to stock $symbol")
        }
    }

    override fun detach(observer: Observer) {
        if (observers.remove(observer)) {
            println("📌 Observer detached from stock $symbol")
        }
    }

    override fun notifyObservers() {
        observers.forEach { observer ->
            observer.update("Stock $symbol price changed to $$price")
        }
    }

    fun setPrice(newPrice: Double) {
        if (price != newPrice) {
            price = newPrice
            println("\n📊 Stock $symbol price updated to $$price")
            notifyObservers()
        }
    }

    fun getPrice(): Double = price
}

// ============================================================================
// CONCRETE OBSERVERS
// ============================================================================

/**
 * ConcreteObserver: Investor watching stock prices
 */
class Investor(private val name: String) : Observer {
    override fun update(event: String) {
        println("📱 $name received notification: $event")
    }
}

/**
 * ConcreteObserver: Trading Bot that executes trades based on price changes
 */
class TradingBot(private val botName: String) : Observer {
    override fun update(event: String) {
        println("🤖 $botName executing trades: $event")
    }
}

/**
 * ConcreteObserver: News Agency reporting on market changes
 */
class NewsAgency(private val agencyName: String) : Observer {
    override fun update(event: String) {
        println("📰 $agencyName reporting: $event")
    }
}

// ============================================================================
// DEMONSTRATION
// ============================================================================

fun main() {
    println("=== OBSERVER PATTERN EXAMPLE ===\n")

    // Create a stock subject
    val appleStock = Stock("AAPL")

    // Create observers
    val investor1 = Investor("John")
    val investor2 = Investor("Sarah")
    val tradingBot = TradingBot("HighFrequencyBot")
    val newsAgency = NewsAgency("Bloomberg")

    // Attach observers to the stock
    println("Step 1: Attaching observers")
    appleStock.attach(investor1)
    appleStock.attach(investor2)
    appleStock.attach(tradingBot)
    appleStock.attach(newsAgency)

    // Change stock price - all observers are notified
    println("\nStep 2: Stock price changes")
    appleStock.setPrice(150.50)

    println("\nStep 3: Another price change")
    appleStock.setPrice(152.75)

    // Detach an observer
    println("\nStep 4: Investor1 unsubscribes")
    appleStock.detach(investor1)

    println("\nStep 5: Another price change (investor1 won't be notified)")
    appleStock.setPrice(155.00)

    println("\n=== BENEFITS DEMONSTRATED ===")
    println("✓ Loose Coupling: Stock doesn't know implementation details of observers")
    println("✓ Dynamic: Observers can attach/detach at runtime")
    println("✓ Automatic Updates: Observers notified without polling")
    println("✓ One-to-Many: Single stock notifies multiple observers")
}

/**
 * OUTPUT EXAMPLE:
 * === OBSERVER PATTERN EXAMPLE ===
 *
 * Step 1: Attaching observers
 * 📌 Observer attached to stock AAPL
 * 📌 Observer attached to stock AAPL
 * 📌 Observer attached to stock AAPL
 * 📌 Observer attached to stock AAPL
 *
 * Step 2: Stock price changes
 *
 * 📊 Stock AAPL price updated to $150.5
 * 📱 John received notification: Stock AAPL price changed to $150.5
 * 📱 Sarah received notification: Stock AAPL price changed to $150.5
 * 🤖 HighFrequencyBot executing trades: Stock AAPL price changed to $150.5
 * 📰 Bloomberg reporting: Stock AAPL price changed to $150.5
 *
 * Step 3: Another price change
 *
 * 📊 Stock AAPL price updated to $152.75
 * 📱 John received notification: Stock AAPL price changed to $152.75
 * 📱 Sarah received notification: Stock AAPL price changed to $152.75
 * 🤖 HighFrequencyBot executing trades: Stock AAPL price changed to $152.75
 * 📰 Bloomberg reporting: Stock AAPL price changed to $152.75
 *
 * Step 4: Investor1 unsubscribes
 * 📌 Observer detached from stock AAPL
 *
 * Step 5: Another price change (investor1 won't be notified)
 *
 * 📊 Stock AAPL price updated to $155.0
 * 📱 Sarah received notification: Stock AAPL price changed to $155.0
 * 🤖 HighFrequencyBot executing trades: Stock AAPL price changed to $155.0
 * 📰 Bloomberg reporting: Stock AAPL price changed to $155.0
 *
 * === BENEFITS DEMONSTRATED ===
 * ✓ Loose Coupling: Stock doesn't know implementation details of observers
 * ✓ Dynamic: Observers can attach/detach at runtime
 * ✓ Automatic Updates: Observers notified without polling
 * ✓ One-to-Many: Single stock notifies multiple observers
 */

