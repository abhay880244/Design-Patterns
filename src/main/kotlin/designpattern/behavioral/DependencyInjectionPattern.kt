package designpattern.behavioral

/**
 * Dependency Injection Pattern Implementation
 * A design pattern that allows dependencies to be injected into a class
 * rather than the class creating them itself.
 *
 * Benefits:
 * - Loose coupling between classes
 * - Easier to test (mock dependencies)
 * - More maintainable and flexible code
 */

// ============== Interface/Abstraction Layer ==============
interface EmailService {
    fun sendEmail(to: String, message: String)
}

interface Database {
    fun save(data: String)
    fun retrieve(): String
}

interface LoggerService {
    fun log(message: String)
}

// ============== Concrete Implementations ==============
class SmtpEmailService : EmailService {
    override fun sendEmail(to: String, message: String) {
        println("[SMTP] Sending email to $to: $message")
    }
}

class GmailEmailService : EmailService {
    override fun sendEmail(to: String, message: String) {
        println("[GMAIL] Sending email to $to: $message")
    }
}

class MySQLDatabase : Database {
    override fun save(data: String) {
        println("[MySQL] Saving data: $data")
    }

    override fun retrieve(): String {
        println("[MySQL] Retrieving data from database")
        return "Data from MySQL"
    }
}

class MongoDatabase : Database {
    override fun save(data: String) {
        println("[MongoDB] Saving document: $data")
    }

    override fun retrieve(): String {
        println("[MongoDB] Retrieving document from database")
        return "Data from MongoDB"
    }
}

class ConsoleLogger : LoggerService {
    override fun log(message: String) {
        println("[LOG] $message")
    }
}

// ============== Service with Dependency Injection ==============
// Constructor Injection (most common and recommended)
class UserService(
    private val emailService: EmailService,
    private val database: Database,
    private val logger: LoggerService
) {
    fun registerUser(email: String, name: String) {
        logger.log("Registering user: $name with email: $email")
        database.save("User: $name, Email: $email")
        emailService.sendEmail(email, "Welcome, $name!")
    }

    fun getUser(): String {
        val userData = database.retrieve()
        logger.log("Retrieved user data: $userData")
        return userData
    }
}

// ============== Property Injection Example ==============
class NotificationService {
    lateinit var emailService: EmailService
    lateinit var logger: LoggerService

    fun notify(email: String, message: String) {
        logger.log("Sending notification")
        emailService.sendEmail(email, message)
    }
}

// ============== Setter Injection Example ==============
class ReportService {
    private var database: Database? = null
    private var logger: LoggerService? = null

    fun setDatabase(db: Database) {
        this.database = db
    }

    fun setLogger(log: LoggerService) {
        this.logger = log
    }

    fun generateReport(): String {
        logger?.log("Generating report...")
        val data = database?.retrieve() ?: "No data"
        println("Report generated with: $data")
        return data
    }
}

// ============== Simple Service Locator/Container ==============
class DIContainer {
    private val services = mutableMapOf<String, Any>()

    fun register(key: String, service: Any) {
        services[key] = service
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> get(key: String): T {
        return services[key] as? T
            ?: throw IllegalArgumentException("Service not found: $key")
    }
}

// ============== Demo/Usage ==============
fun main() {
    println("=== Dependency Injection Pattern Demo ===\n")

    // Setup: Create concrete implementations
    val smtpEmail = SmtpEmailService()
    val mysqlDb = MySQLDatabase()
    val consoleLogger = ConsoleLogger()

    // Constructor Injection
    println("--- Constructor Injection ---")
    val userService = UserService(smtpEmail, mysqlDb, consoleLogger)
    userService.registerUser("john@example.com", "John Doe")
    userService.getUser()

    println("\n--- Switching Implementations (Easy because of DI) ---")
    val gmailEmail = GmailEmailService()
    val mongoDb = MongoDatabase()
    val userService2 = UserService(gmailEmail, mongoDb, consoleLogger)
    userService2.registerUser("jane@example.com", "Jane Smith")

    // Setter Injection
    println("\n--- Setter Injection ---")
    val reportService = ReportService()
    reportService.setDatabase(mysqlDb)
    reportService.setLogger(consoleLogger)
    reportService.generateReport()

    // Property Injection
    println("\n--- Property Injection ---")
    val notificationService = NotificationService()
    notificationService.emailService = smtpEmail
    notificationService.logger = consoleLogger
    notificationService.notify("admin@example.com", "System alert!")

    // Service Locator Pattern (DIContainer)
    println("\n--- Service Locator Pattern ---")
    val container = DIContainer()
    container.register("emailService", gmailEmail)
    container.register("database", mongoDb)
    container.register("logger", consoleLogger)

    val email: EmailService = container.get("emailService")
    val db: Database = container.get("database")
    val logger: LoggerService = container.get("logger")

    val userService3 = UserService(email, db, logger)
    userService3.registerUser("alice@example.com", "Alice Johnson")
}

