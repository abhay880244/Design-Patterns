package designpattern.structural

/**
 * Composite Pattern Implementation
 *
 * WHAT IS THE COMPOSITE PATTERN?
 * Composes objects into tree structures to represent part-whole hierarchies.
 * Allows clients to treat individual objects and compositions uniformly.
 *
 * KEY CONCEPTS:
 * - Component: Common interface for all objects (leaves and composites)
 * - Leaf: Object with no children
 * - Composite: Object that can contain children (leaves or composites)
 * - Tree structure: Hierarchical arrangement of components
 *
 * REAL-WORLD ANALOGY:
 * File system:
 *   - Files (Leaf) - can't contain other files
 *   - Folders (Composite) - can contain files and folders
 *   - Both are treated uniformly (copy, delete, etc.)
 *
 * BENEFITS:
 * - Simplifies code by treating individuals and compositions uniformly
 * - Flexible tree structures
 * - Easy to add new component types
 * - Follows Open/Closed Principle
 *
 * WHEN TO USE:
 * - Hierarchical structures (tree-like)
 * - Want to treat individual and group objects the same way
 * - File systems, UI component hierarchies, organization structures
 */

// ============================================================================
// COMPONENT INTERFACE
// ============================================================================

interface FileSystemComponent {
    fun getName(): String
    fun getSize(): Long
    fun display(indent: String = "")
    fun delete()
}

// ============================================================================
// LEAF: FILE
// ============================================================================

class File(
    private val name: String,
    private val size: Long
) : FileSystemComponent {

    override fun getName(): String = name

    override fun getSize(): Long = size

    override fun display(indent: String) {
        println("$indent📄 $name (${size}B)")
    }

    override fun delete() {
        println("🗑️ Deleting file: $name")
    }
}

// ============================================================================
// COMPOSITE: FOLDER
// ============================================================================

class Folder(private val name: String) : FileSystemComponent {
    private val components = mutableListOf<FileSystemComponent>()

    fun addComponent(component: FileSystemComponent) {
        components.add(component)
        println("✅ Added ${component.getName()} to folder $name")
    }

    fun removeComponent(component: FileSystemComponent) {
        components.remove(component)
        println("❌ Removed ${component.getName()} from folder $name")
    }

    override fun getName(): String = name

    override fun getSize(): Long {
        var totalSize = 0L
        for (component in components) {
            totalSize += component.getSize()
        }
        return totalSize
    }

    override fun display(indent: String) {
        println("$indent📁 $name/")
        for (component in components) {
            component.display(indent + "  ")
        }
    }

    override fun delete() {
        println("🗑️ Deleting folder: $name (${components.size} items)")
        for (component in components) {
            component.delete()
        }
        components.clear()
    }
}

// ============================================================================
// ANOTHER EXAMPLE: ORGANIZATION STRUCTURE
// ============================================================================

interface Employee {
    fun getName(): String
    fun getSalary(): Double
    fun display(indent: String = "")
}

class Individual(
    private val name: String,
    private val salary: Double,
    private val position: String
) : Employee {

    override fun getName(): String = name

    override fun getSalary(): Double = salary

    override fun display(indent: String) {
        println("$indent👤 $name - $position (\$$salary)")
    }
}

class Department(
    private val name: String
) : Employee {
    private val members = mutableListOf<Employee>()

    fun addEmployee(employee: Employee) {
        members.add(employee)
    }

    fun removeEmployee(employee: Employee) {
        members.remove(employee)
    }

    override fun getName(): String = name

    override fun getSalary(): Double {
        var totalSalary = 0.0
        for (member in members) {
            totalSalary += member.getSalary()
        }
        return totalSalary
    }

    override fun display(indent: String) {
        println("$indent🏢 Department: $name")
        println("$indent   Total Salary: \$${getSalary()}")
        for (member in members) {
            member.display(indent + "  ")
        }
    }
}

// ============================================================================
// DEMO
// ============================================================================

fun demonstrateComposite() {
    println("=== Composite Pattern Demo ===\n")

    // Example 1: File System
    println("--- File System Structure ---")
    val root = Folder("Projects")

    val project1 = Folder("ProjectA")
    val file1 = File("main.kt", 5120)
    val file2 = File("config.xml", 2048)
    project1.addComponent(file1)
    project1.addComponent(file2)

    val project2 = Folder("ProjectB")
    val file3 = File("app.kt", 8192)
    val file4 = File("database.kt", 10240)
    val docs = Folder("Docs")
    val file5 = File("README.md", 1024)
    docs.addComponent(file5)
    project2.addComponent(file3)
    project2.addComponent(file4)
    project2.addComponent(docs)

    root.addComponent(project1)
    root.addComponent(project2)

    println()
    root.display()
    println("\nTotal size of all projects: ${root.getSize()} bytes\n")

    // Example 2: Organization Structure
    println("--- Organization Structure ---")
    val company = Department("TechCorp")

    val engineeringDept = Department("Engineering")
    engineeringDept.addEmployee(Individual("Alice Johnson", 100000.0, "Senior Developer"))
    engineeringDept.addEmployee(Individual("Bob Smith", 90000.0, "Developer"))
    engineeringDept.addEmployee(Individual("Carol White", 85000.0, "Junior Developer"))

    val salesDept = Department("Sales")
    salesDept.addEmployee(Individual("David Brown", 80000.0, "Sales Manager"))
    salesDept.addEmployee(Individual("Eve Davis", 70000.0, "Sales Executive"))

    company.addEmployee(engineeringDept)
    company.addEmployee(salesDept)

    println()
    company.display()
    println("\nTotal company salary: \$${company.getSalary()}\n")

    // Delete operation
    println("--- Deleting Resources ---")
    project1.delete()
}

