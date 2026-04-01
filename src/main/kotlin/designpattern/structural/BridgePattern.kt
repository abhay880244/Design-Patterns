package designpattern.structural

/**
 * Bridge Pattern Implementation
 *
 * WHAT IS THE BRIDGE PATTERN?
 * Decouples an abstraction from its implementation so that the two can vary independently.
 * Creates a bridge between abstraction and implementation layers.
 *
 * KEY CONCEPTS:
 * - Abstraction: High-level interface
 * - Implementor: Low-level interface for implementation
 * - The bridge connects the two, allowing them to vary independently
 *
 * REAL-WORLD ANALOGY:
 * A car with different engines:
 *   - Car (abstraction) has properties like start(), drive()
 *   - Engine (implementor) can be Electric, Petrol, Diesel
 *   - Bridge allows changing engine without changing car interface
 *
 * BENEFITS:
 * - Decouples abstraction from implementation
 * - Reduces class explosion (fewer subclasses)
 * - Follows Open/Closed Principle
 * - Improves code maintainability
 *
 * WHEN TO USE:
 * - Want to avoid permanent binding between abstraction and implementation
 * - Changes in implementation should not affect clients
 * - Need to share implementation among multiple objects
 */

// ============================================================================
// IMPLEMENTOR INTERFACE
// ============================================================================

interface Engine {
    fun start()
    fun stop()
    fun accelerate()
}

// ============================================================================
// CONCRETE IMPLEMENTORS
// ============================================================================

class PetrolEngine : Engine {
    override fun start() {
        println("🔥 Petrol engine starting... Vroom!")
    }

    override fun stop() {
        println("🔥 Petrol engine stopping...")
    }

    override fun accelerate() {
        println("🔥 Petrol engine accelerating... VROOOOM!")
    }
}

class ElectricEngine : Engine {
    override fun start() {
        println("⚡ Electric engine starting... Whisper...")
    }

    override fun stop() {
        println("⚡ Electric engine stopping...")
    }

    override fun accelerate() {
        println("⚡ Electric engine accelerating... ZZZZZAP!")
    }
}

class DieselEngine : Engine {
    override fun start() {
        println("🛢️ Diesel engine starting... Chug chug...")
    }

    override fun stop() {
        println("🛢️ Diesel engine stopping...")
    }

    override fun accelerate() {
        println("🛢️ Diesel engine accelerating... CHUGGA CHUGGA!")
    }
}

// ============================================================================
// ABSTRACTION
// ============================================================================

abstract class Car(protected val engine: Engine) {
    abstract fun drive()
    abstract fun refuel()
}

// ============================================================================
// REFINED ABSTRACTIONS
// ============================================================================

class Sedan(engine: Engine) : Car(engine) {
    override fun drive() {
        println("\n🚗 Sedan driving smoothly")
        engine.start()
        engine.accelerate()
        engine.stop()
    }

    override fun refuel() {
        println("⛽ Sedan refueling")
    }
}

class SUV(engine: Engine) : Car(engine) {
    override fun drive() {
        println("\n🚙 SUV driving off-road")
        engine.start()
        engine.accelerate()
        engine.accelerate()
        engine.stop()
    }

    override fun refuel() {
        println("⛽ SUV refueling")
    }
}

class SportsCar(engine: Engine) : Car(engine) {
    override fun drive() {
        println("\n🏎️ Sports car racing")
        engine.start()
        engine.accelerate()
        engine.accelerate()
        engine.accelerate()
        engine.stop()
    }

    override fun refuel() {
        println("⛽ Sports car refueling (premium fuel only)")
    }
}

// ============================================================================
// ANOTHER EXAMPLE: NOTIFICATION AND DEVICE BRIDGE
// ============================================================================

interface Notification {
    fun send(title: String, message: String)
}

class EmailNotification : Notification {
    override fun send(title: String, message: String) {
        println("📧 Sending Email - Title: $title, Message: $message")
    }
}

class SMSNotification : Notification {
    override fun send(title: String, message: String) {
        println("📱 Sending SMS - Title: $title, Message: $message")
    }
}

abstract class Messenger(protected val notification: Notification) {
    abstract fun sendAlert(message: String)
}

class SecurityMessenger(notification: Notification) : Messenger(notification) {
    override fun sendAlert(message: String) {
        println("🔒 Security Alert")
        notification.send("SECURITY ALERT", message)
    }
}

class MarketingMessenger(notification: Notification) : Messenger(notification) {
    override fun sendAlert(message: String) {
        println("📢 Marketing Message")
        notification.send("SPECIAL OFFER", message)
    }
}

// ============================================================================
// DEMO
// ============================================================================

fun demonstrateBridge() {
    println("=== Bridge Pattern Demo ===\n")

    // Example 1: Cars with different engines
    println("--- Cars with Different Engines ---")

    val petrolEngine = PetrolEngine()
    val electricEngine = ElectricEngine()
    val dieselEngine = DieselEngine()

    val sedan = Sedan(petrolEngine)
    val suv = SUV(electricEngine)
    val sportsCar = SportsCar(dieselEngine)

    sedan.drive()
    suv.drive()
    sportsCar.drive()

    // Example 2: Messengers with different notifications
    println("\n--- Messengers with Different Notifications ---")

    val emailNotification = EmailNotification()
    val smsNotification = SMSNotification()

    val securityMessenger = SecurityMessenger(emailNotification)
    val marketingMessenger = MarketingMessenger(smsNotification)

    securityMessenger.sendAlert("Unauthorized login attempt detected!")
    marketingMessenger.sendAlert("Get 50% off on all products this weekend!")

    // Switch notification method dynamically
    println("\n--- Switching Notification Method ---")
    val securityViaSMS = SecurityMessenger(smsNotification)
    securityViaSMS.sendAlert("Payment fraud detected on your account!")
}

