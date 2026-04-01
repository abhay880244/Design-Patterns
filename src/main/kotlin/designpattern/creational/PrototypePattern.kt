package designpattern.creational

/**
 * Prototype Pattern Implementation
 *
 * WHAT IS THE PROTOTYPE PATTERN?
 * Creates new objects by copying an existing object (prototype) rather than
 * creating them from scratch. This is useful when object creation is expensive.
 *
 * KEY CONCEPTS:
 * - Prototype: Object that serves as a template
 * - Clone: Create a copy of the prototype
 * - Shallow Clone: Copies primitive values, references point to same objects
 * - Deep Clone: Copies everything including nested objects
 *
 * REAL-WORLD ANALOGY:
 * Making photocopies:
 *   - Original document = Prototype
 *   - Photocopy = Clone
 *   - Instead of writing the document again, you copy it
 *
 * BENEFITS:
 * - Faster object creation if copying is cheaper than creation
 * - Reduces constructor complexity
 * - Can clone complex objects easily
 * - Useful for object configuration templates
 *
 * WHEN TO USE:
 * - Creating many similar objects
 * - Object creation is expensive
 * - Need to avoid subclassing
 * - Configuration templates
 */

// ============================================================================
// CLONEABLE INTERFACE
// ============================================================================

interface Cloneable<T> {
    fun clone(): T
}

// ============================================================================
// CONCRETE PROTOTYPES
// ============================================================================

data class Employee(
    val id: Int,
    val name: String,
    val salary: Double,
    val department: String,
    val contact: Contact = Contact("", "")
) : Cloneable<Employee> {
    /**
     * Shallow clone: Copies primitive values but references are shared
     */
    override fun clone(): Employee {
        return this.copy()
    }

    /**
     * Deep clone: Copies everything including nested objects
     */
    fun deepClone(): Employee {
        return Employee(
            id = this.id,
            name = this.name,
            salary = this.salary,
            department = this.department,
            contact = Contact(
                email = this.contact.email,
                phone = this.contact.phone
            )
        )
    }
}

data class Contact(
    var email: String,
    var phone: String
)

data class ProjectConfig(
    val projectName: String,
    val version: String,
    val timeout: Long = 5000,
    val maxRetries: Int = 3,
    val database: DatabaseConfig = DatabaseConfig("localhost", 5432)
) : Cloneable<ProjectConfig> {

    override fun clone(): ProjectConfig {
        return this.copy()
    }

    fun deepClone(): ProjectConfig {
        return ProjectConfig(
            projectName = this.projectName,
            version = this.version,
            timeout = this.timeout,
            maxRetries = this.maxRetries,
            database = DatabaseConfig(
                host = this.database.host,
                port = this.database.port
            )
        )
    }
}

data class DatabaseConfig(
    var host: String,
    var port: Int
)

// ============================================================================
// PROTOTYPE REGISTRY (optional)
// ============================================================================

class PrototypeRegistry {
    private val registry = mutableMapOf<String, Any>()

    fun register(key: String, prototype: Any) {
        registry[key] = prototype
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Cloneable<T>> getClone(key: String): T? {
        return (registry[key] as? T)?.clone()
    }
}

// ============================================================================
// DEMO
// ============================================================================

fun main() {
    demonstratePrototype()
}
fun demonstratePrototype() {
    println("=== Prototype Pattern Demo ===\n")

    // Example 1: Employee Cloning
    println("--- Employee Cloning ---")
    val originalEmployee = Employee(
        id = 1,
        name = "John Doe",
        salary = 50000.0,
        department = "Engineering",
        contact = Contact("john@example.com", "123-456-7890")
    )
    println("Original Employee: $originalEmployee")

    val clonedEmployee = originalEmployee.deepClone()
    clonedEmployee.contact.email = "john.clone@example.com"
    println("Cloned Employee (modified): $clonedEmployee")
    println("Original Employee (unchanged): $originalEmployee\n")

    // Example 2: Project Configuration Cloning
    println("--- Project Configuration Cloning ---")
    val baseConfig = ProjectConfig(
        projectName = "MyApp",
        version = "1.0.0",
        timeout = 10000,
        maxRetries = 5
    )
    println("Base Config: $baseConfig")

    val productionConfig = baseConfig.deepClone().copy(
        projectName = "MyApp-Prod",
        timeout = 30000
    )
    println("Production Config: $productionConfig")

    val devConfig = baseConfig.deepClone().copy(
        projectName = "MyApp-Dev",
        timeout = 5000,
        maxRetries = 10
    )
    println("Dev Config: $devConfig\n")

    // Example 3: Prototype Registry
    println("--- Prototype Registry ---")
    val registry = PrototypeRegistry()

    // Register prototypes
    registry.register("employee_template", originalEmployee)
    registry.register("config_template", baseConfig)

    // Clone from registry
    val employeeFromRegistry: Employee? = registry.getClone<Employee>("employee_template")
    val configFromRegistry: ProjectConfig? = registry.getClone<ProjectConfig>("config_template")

    println("Employee from Registry: $employeeFromRegistry")
    println("Config from Registry: $configFromRegistry")
}
