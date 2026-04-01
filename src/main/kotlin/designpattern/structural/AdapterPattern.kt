package designpattern.structural

/**
 * Adapter Pattern Implementation
 *
 * WHAT IS THE ADAPTER PATTERN?
 * Converts the interface of a class into another interface that clients expect.
 * Allows incompatible interfaces to work together.
 *
 * KEY CONCEPTS:
 * - Target: The interface the client expects
 * - Adapter: Converts the Adaptee interface to Target interface
 * - Adaptee: The interface that needs to be adapted
 * - Client: Uses the Target interface
 *
 * REAL-WORLD ANALOGY:
 * Electrical adapters:
 *   - You have a US plug (Adaptee)
 *   - You need a UK socket (Target)
 *   - Adapter converts the US plug to work with UK socket
 *
 * BENEFITS:
 * - Reuses existing classes with incompatible interfaces
 * - Increases code flexibility
 * - Follows Single Responsibility Principle
 * - Follows Open/Closed Principle
 *
 * WHEN TO USE:
 * - Integrating legacy code with new code
 * - Using third-party libraries with incompatible interfaces
 * - Creating a common interface for multiple implementations
 */

// ============================================================================
// LEGACY SYSTEM (Adaptee)
// ============================================================================

/**
 * Old banking system API that we need to adapt
 */
class LegacyBankingSystem {
    fun withdrawCash(amount: Double): String {
        return "Withdrawal of $$amount processed (Legacy System)"
    }

    fun depositCash(amount: Double): String {
        return "Deposit of $$amount processed (Legacy System)"
    }
}

// ============================================================================
// EXPECTED INTERFACE (Target)
// ============================================================================

interface ModernPaymentProcessor {
    fun processPayment(amount: Double, description: String): Boolean
    fun refund(transactionId: String, amount: Double): Boolean
    fun getBalance(): Double
}

// ============================================================================
// ADAPTER
// ============================================================================

/**
 * Class Adapter: Inherits from the Adaptee
 */
class BankingSystemAdapter : ModernPaymentProcessor {
    private val legacyBank = LegacyBankingSystem()
    private var balance: Double = 1000.0

    override fun processPayment(amount: Double, description: String): Boolean {
        println("Processing payment: $description")
        val result = legacyBank.withdrawCash(amount)
        println(result)
        balance -= amount
        return true
    }

    override fun refund(transactionId: String, amount: Double): Boolean {
        println("Refunding transaction: $transactionId")
        val result = legacyBank.depositCash(amount)
        println(result)
        balance += amount
        return true
    }

    override fun getBalance(): Double {
        return balance
    }
}

// ============================================================================
// ANOTHER EXAMPLE: USB ADAPTER
// ============================================================================

interface USB {
    fun transferData(data: String): String
}

interface LightningPort {
    fun transferWithLightning(data: String): String
}

class OldDeviceWithUSB : USB {
    override fun transferData(data: String): String {
        return "Transferring via USB: $data"
    }
}

/**
 * Object Adapter: Uses composition instead of inheritance
 */
class LightningToUSBAdapter(private val usbDevice: USB) : LightningPort {
    override fun transferWithLightning(data: String): String {
        val usbData = usbDevice.transferData(data)
        return "🔌 $usbData (converted from Lightning)"
    }
}

// ============================================================================
// DEMO
// ============================================================================

fun demonstrateAdapter() {
    println("=== Adapter Pattern Demo ===\n")

    // Example 1: Banking System Adapter
    println("--- Banking System Adapter ---")
    val paymentProcessor: ModernPaymentProcessor = BankingSystemAdapter()

    println("Initial Balance: ${paymentProcessor.getBalance()}")
    paymentProcessor.processPayment(100.0, "Coffee purchase")
    println("Balance after payment: ${paymentProcessor.getBalance()}")

    paymentProcessor.refund("TXN001", 50.0)
    println("Balance after refund: ${paymentProcessor.getBalance()}\n")

    // Example 2: USB to Lightning Adapter
    println("--- USB to Lightning Adapter ---")
    val oldDevice = OldDeviceWithUSB()
    val adapter = LightningToUSBAdapter(oldDevice)

    val data = "Hello from Lightning"
    println(adapter.transferWithLightning(data))
}

