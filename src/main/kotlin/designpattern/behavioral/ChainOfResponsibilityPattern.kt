package designpattern.behavioral

/**
 * Chain of Responsibility Pattern Implementation
 *
 * WHAT IS THE CHAIN OF RESPONSIBILITY PATTERN?
 * Passes requests along a chain of handlers where each handler decides either
 * to process the request or pass it along the chain.
 *
 * KEY CONCEPTS:
 * - Handler: Interface for processing requests
 * - ConcreteHandler: Processes request or passes to next handler
 * - Client: Initiates request
 * - Chain: Linked handlers
 *
 * REAL-WORLD ANALOGY:
 * Help desk ticket routing:
 *   - Level 1 Support: Handles simple issues
 *   - Level 2 Support: Handles technical issues
 *   - Manager: Handles complex issues
 *   - Each level either resolves or escalates
 *
 * BENEFITS:
 * - Decouples sender from receiver
 * - Multiple handlers process request
 * - Easy to add new handlers
 * - Dynamic chain at runtime
 *
 * WHEN TO USE:
 * - Multiple handlers for a request
 * - Handler is unknown beforehand
 * - Event handling systems
 * - Logging frameworks
 */

// ============================================================================
// HANDLER INTERFACE
// ============================================================================

interface SupportHandler {
    fun handle(ticket: SupportTicket): Boolean
    fun setNextHandler(handler: SupportHandler)
}

// ============================================================================
// SUPPORT TICKET
// ============================================================================

data class SupportTicket(
    val id: Int,
    val title: String,
    val priority: Priority,
    val description: String
) {
    enum class Priority {
        LOW, MEDIUM, HIGH, CRITICAL
    }
}

// ============================================================================
// CONCRETE HANDLERS
// ============================================================================

class Level1SupportHandler : SupportHandler {
    private var nextHandler: SupportHandler? = null

    override fun handle(ticket: SupportTicket): Boolean {
        return when {
            ticket.priority == SupportTicket.Priority.LOW -> {
                println("✅ Level 1 Support: Resolved ticket #${ticket.id} - ${ticket.title}")
                println("   📝 Solution: Reset password / Clear cache\n")
                true
            }
            else -> {
                println("⏭️ Level 1 Support: Escalating ticket #${ticket.id} to Level 2\n")
                nextHandler?.handle(ticket) ?: false
            }
        }
    }

    override fun setNextHandler(handler: SupportHandler) {
        nextHandler = handler
    }
}

class Level2SupportHandler : SupportHandler {
    private var nextHandler: SupportHandler? = null

    override fun handle(ticket: SupportTicket): Boolean {
        return when {
            ticket.priority in listOf(
                SupportTicket.Priority.LOW,
                SupportTicket.Priority.MEDIUM
            ) -> {
                println("✅ Level 2 Support: Resolved ticket #${ticket.id} - ${ticket.title}")
                println("   📝 Solution: Updated configuration / Patched issue\n")
                true
            }
            else -> {
                println("⏭️ Level 2 Support: Escalating ticket #${ticket.id} to Manager\n")
                nextHandler?.handle(ticket) ?: false
            }
        }
    }

    override fun setNextHandler(handler: SupportHandler) {
        nextHandler = handler
    }
}

class ManagerHandler : SupportHandler {
    private var nextHandler: SupportHandler? = null

    override fun handle(ticket: SupportTicket): Boolean {
        return when {
            ticket.priority in listOf(
                SupportTicket.Priority.LOW,
                SupportTicket.Priority.MEDIUM,
                SupportTicket.Priority.HIGH
            ) -> {
                println("✅ Manager: Resolved ticket #${ticket.id} - ${ticket.title}")
                println("   📝 Solution: System redesign / Major update\n")
                true
            }
            else -> {
                println("⏭️ Manager: Escalating ticket #${ticket.id} to Director\n")
                nextHandler?.handle(ticket) ?: false
            }
        }
    }

    override fun setNextHandler(handler: SupportHandler) {
        nextHandler = handler
    }
}

class DirectorHandler : SupportHandler {
    private var nextHandler: SupportHandler? = null

    override fun handle(ticket: SupportTicket): Boolean {
        println("✅ Director: Emergency handling of ticket #${ticket.id} - ${ticket.title}")
        println("   📝 Solution: Full system overhaul / Crisis management\n")
        return true
    }

    override fun setNextHandler(handler: SupportHandler) {
        nextHandler = handler
    }
}

// ============================================================================
// ANOTHER EXAMPLE: HTTP REQUEST LOGGING
// ============================================================================

data class Request(
    val method: String,
    val url: String,
    val body: String = ""
)

interface RequestHandler {
    fun handle(request: Request)
    fun setNext(handler: RequestHandler): RequestHandler
}

class AuthenticationHandler : RequestHandler {
    private var nextHandler: RequestHandler? = null

    override fun handle(request: Request) {
        println("🔐 Authentication Handler: Checking credentials...")
        if (request.url.contains("login")) {
            println("   ✅ User authenticated\n")
        }
        nextHandler?.handle(request)
    }

    override fun setNext(handler: RequestHandler): RequestHandler {
        nextHandler = handler
        return handler
    }
}

class ValidationHandler : RequestHandler {
    private var nextHandler: RequestHandler? = null

    override fun handle(request: Request) {
        println("✔️ Validation Handler: Validating request...")
        if (request.body.isNotEmpty()) {
            println("   ✅ Request body is valid\n")
        }
        nextHandler?.handle(request)
    }

    override fun setNext(handler: RequestHandler): RequestHandler {
        nextHandler = handler
        return handler
    }
}

class LoggingHandler : RequestHandler {
    private var nextHandler: RequestHandler? = null

    override fun handle(request: Request) {
        println("📝 Logging Handler: Logging request...")
        println("   ${request.method} ${request.url}\n")
        nextHandler?.handle(request)
    }

    override fun setNext(handler: RequestHandler): RequestHandler {
        nextHandler = handler
        return handler
    }
}

class ProcessingHandler : RequestHandler {
    private var nextHandler: RequestHandler? = null

    override fun handle(request: Request) {
        println("⚙️ Processing Handler: Processing request...")
        println("   ✅ Request processed successfully\n")
        nextHandler?.handle(request)
    }

    override fun setNext(handler: RequestHandler): RequestHandler {
        nextHandler = handler
        return handler
    }
}

// ============================================================================
// DEMO
// ============================================================================

fun demonstrateChainOfResponsibility() {
    println("=== Chain of Responsibility Pattern Demo ===\n")

    // Example 1: Support Ticket Chain
    println("--- Support Ticket Routing ---")

    // Build the chain
    val level1 = Level1SupportHandler()
    val level2 = Level2SupportHandler()
    val manager = ManagerHandler()
    val director = DirectorHandler()

    level1.setNextHandler(level2)
    level2.setNextHandler(manager)
    manager.setNextHandler(director)

    // Create tickets
    val tickets = listOf(
        SupportTicket(1, "Password Reset", SupportTicket.Priority.LOW, "User forgot password"),
        SupportTicket(2, "Database Connection Issue", SupportTicket.Priority.MEDIUM, "Cannot connect to database"),
        SupportTicket(3, "Application Crash", SupportTicket.Priority.HIGH, "App crashes on startup"),
        SupportTicket(4, "Data Breach", SupportTicket.Priority.CRITICAL, "Suspected security breach")
    )

    // Process tickets
    println("Processing support tickets:\n")
    tickets.forEach { ticket ->
        println("🎫 Incoming ticket #${ticket.id} (Priority: ${ticket.priority})")
        level1.handle(ticket)
    }

    // Example 2: HTTP Request Handler Chain
    println("--- HTTP Request Handler Chain ---")

    val authHandler = AuthenticationHandler()
    val validationHandler = ValidationHandler()
    val loggingHandler = LoggingHandler()
    val processingHandler = ProcessingHandler()

    authHandler
        .setNext(validationHandler)
        .setNext(loggingHandler)
        .setNext(processingHandler)

    val requests = listOf(
        Request("GET", "/api/users", ""),
        Request("POST", "/api/login", "{\"username\": \"john\", \"password\": \"secret\"}"),
        Request("PUT", "/api/users/1", "{\"name\": \"Jane\"}")
    )

    println("\nProcessing HTTP requests:\n")
    requests.forEach { request ->
        println("📨 Incoming request:")
        authHandler.handle(request)
    }
}

