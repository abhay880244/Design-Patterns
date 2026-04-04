package designpattern.solid

/**
 * SOLID Principles: Violations and Solutions
 */

// --- 1. Single Responsibility Principle (SRP) ---
// A class should have only one reason to change.

// VIOLATION: This class handles calculation, printing, and persistence.
class InvoiceViolation(val amount: Double) {
    fun calculateTotal(): Double = amount * 1.18
    fun printInvoice() = println("Invoice: $amount")
    fun saveToDatabase() = println("Saving to DB...")
}

// SOLUTION: Split into separate classes with distinct responsibilities.
class Invoice(val amount: Double) {
    fun calculateTotal(): Double = amount * 1.18
}

class InvoicePrinter {
    fun print(invoice: Invoice) = println("Invoice Amount: ${invoice.amount}")
}

class InvoiceRepository {
    fun save(invoice: Invoice) = println("Saving invoice ${invoice.amount} to DB...")
}


// --- 2. Open/Closed Principle (OCP) ---
// Software entities should be open for extension but closed for modification.

// VIOLATION: Adding a new discount type requires modifying this class (adding another 'when' case).
class DiscountCalculatorViolation {
    fun applyDiscount(amount: Double, type: String): Double {
        return when (type) {
            "Festive" -> amount * 0.8
            "VIP" -> amount * 0.5
            else -> amount
        }
    }
}

// SOLUTION: Use an interface. New discounts can be added without changing existing code.
interface DiscountStrategy {
    fun applyDiscount(amount: Double): Double
}

class FestiveDiscount : DiscountStrategy {
    override fun applyDiscount(amount: Double) = amount * 0.8
}

class DiscountCalculator(private val strategy: DiscountStrategy) {
    fun calculate(amount: Double) = strategy.applyDiscount(amount)
}


// --- 3. Liskov Substitution Principle (LSP) ---
// Objects of a superclass should be replaceable with objects of its subclasses.

// VIOLATION: Penguin is a Bird but cannot fly. Throwing an exception breaks the contract.
open class BirdViolation {
    open fun fly() = println("Flying...")
}

class PenguinViolation : BirdViolation() {
    override fun fly() = throw UnsupportedOperationException("Penguins can't fly!")
}

// SOLUTION: Refactor the hierarchy so only flying birds have the fly() capability.
abstract class Bird {
    abstract fun eat()
}

abstract class FlyingBird : Bird() {
    abstract fun fly()
}

class Eagle : FlyingBird() {
    override fun eat() = println("Eagle eating")
    override fun fly() = println("Eagle flying")
}

class Penguin : Bird() {
    override fun eat() = println("Penguin eating")
}


// --- 4. Interface Segregation Principle (ISP) ---
// No client should be forced to depend on methods it does not use.

// VIOLATION: A Robot is forced to implement eat() even though it doesn't eat.
interface WorkerViolation {
    fun work()
    fun eat()
}

class RobotViolation : WorkerViolation {
    override fun work() = println("Robot working")
    override fun eat() = throw UnsupportedOperationException("Robots don't eat")
}

// SOLUTION: Split the fat interface into smaller, specific ones.
interface Workable {
    fun work()
}

interface Eatable {
    fun eat()
}

class HumanWorker : Workable, Eatable {
    override fun work() = println("Human working")
    override fun eat() = println("Human eating")
}

class RobotWorker : Workable {
    override fun work() = println("Robot working")
}


// --- 5. Dependency Inversion Principle (DIP) ---
// High-level modules should not depend on low-level modules. Both should depend on abstractions.

// VIOLATION: NotificationService depends directly on the concrete EmailSender class.
class EmailSenderViolation {
    fun send(msg: String) = println("Email: $msg")
}

class NotificationServiceViolation {
    private val sender = EmailSenderViolation() // Hard dependency
    fun notify(msg: String) = sender.send(msg)
}

// SOLUTION: Depend on an interface (abstraction).
interface MessageSender {
    fun send(msg: String)
}

class EmailSender : MessageSender {
    override fun send(msg: String) = println("Email: $msg")
}

class NotificationService(private val sender: MessageSender) {
    fun notify(msg: String) = sender.send(msg)
}

fun main() {
    println("--- SOLID Principles Solutions ---")
    
    // SRP
    val invoice = Invoice(100.0)
    InvoicePrinter().print(invoice)
    InvoiceRepository().save(invoice)
    
    // OCP
    val calculator = DiscountCalculator(FestiveDiscount())
    println("Discounted price: ${calculator.calculate(100.0)}")
    
    // LSP
    val eagle: FlyingBird = Eagle()
    eagle.fly()
    val penguin: Bird = Penguin()
    penguin.eat()
    
    // ISP
    val robot = RobotWorker()
    robot.work()
    val human = HumanWorker()
    human.eat()
    
    // DIP
    val notification = NotificationService(EmailSender())
    notification.notify("Hello SOLID!")
}
