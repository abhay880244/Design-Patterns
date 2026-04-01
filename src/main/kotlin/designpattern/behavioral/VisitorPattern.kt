package designpattern.behavioral

/**
 * Visitor Pattern Implementation
 *
 * WHAT IS THE VISITOR PATTERN?
 * Represents an operation to be performed on elements of an object structure.
 * Lets you define new operations without changing the classes of elements.
 *
 * KEY CONCEPTS:
 * - Element: Object that accepts a visitor
 * - Visitor: Interface for visiting elements
 * - ConcreteVisitor: Implements specific operations
 * - ObjectStructure: Collection of elements
 *
 * REAL-WORLD ANALOGY:
 * Tax system for different entity types:
 *   - Visitor: Tax calculator
 *   - Elements: Company, Individual, Trust
 *   - Each element accepts visitor and computes taxes differently
 *
 * BENEFITS:
 * - Separates algorithm from object structure
 * - Easy to add new operations
 * - Centralizes operations in visitors
 * - Follows Single Responsibility Principle
 *
 * WHEN TO USE:
 * - Many distinct and unrelated operations on objects
 * - Need to avoid "polluting" object with operations
 * - Object structure is stable, operations change frequently
 */

// ============================================================================
// ELEMENT INTERFACE
// ============================================================================

interface ShapeElement {
    fun accept(visitor: ShapeVisitor)
    fun getName(): String
}

// ============================================================================
// CONCRETE ELEMENTS
// ============================================================================

class Circle(private val radius: Double) : ShapeElement {
    override fun accept(visitor: ShapeVisitor) {
        visitor.visitCircle(this)
    }

    override fun getName(): String = "Circle"

    fun getRadius(): Double = radius

    fun getArea(): Double = Math.PI * radius * radius
}

class Rectangle(private val width: Double, private val height: Double) : ShapeElement {
    override fun accept(visitor: ShapeVisitor) {
        visitor.visitRectangle(this)
    }

    override fun getName(): String = "Rectangle"

    fun getWidth(): Double = width

    fun getHeight(): Double = height

    fun getArea(): Double = width * height
}

class Triangle(private val side1: Double, private val side2: Double, private val side3: Double) : ShapeElement {
    override fun accept(visitor: ShapeVisitor) {
        visitor.visitTriangle(this)
    }

    override fun getName(): String = "Triangle"

    fun getSide1(): Double = side1
    fun getSide2(): Double = side2
    fun getSide3(): Double = side3

    fun getArea(): Double {
        val s = (side1 + side2 + side3) / 2
        return Math.sqrt(s * (s - side1) * (s - side2) * (s - side3))
    }
}

// ============================================================================
// VISITOR INTERFACE
// ============================================================================

interface ShapeVisitor {
    fun visitCircle(circle: Circle)
    fun visitRectangle(rectangle: Rectangle)
    fun visitTriangle(triangle: Triangle)
}

// ============================================================================
// CONCRETE VISITORS
// ============================================================================

class AreaCalculatorVisitor : ShapeVisitor {
    private var totalArea = 0.0

    override fun visitCircle(circle: Circle) {
        val area = circle.getArea()
        println("📐 Circle (radius=${circle.getRadius()}): Area = ${String.format("%.2f", area)}")
        totalArea += area
    }

    override fun visitRectangle(rectangle: Rectangle) {
        val area = rectangle.getArea()
        println("📐 Rectangle (${rectangle.getWidth()}x${rectangle.getHeight()}): Area = ${String.format("%.2f", area)}")
        totalArea += area
    }

    override fun visitTriangle(triangle: Triangle) {
        val area = triangle.getArea()
        println("📐 Triangle: Area = ${String.format("%.2f", area)}")
        totalArea += area
    }

    fun getTotalArea(): Double = totalArea
}

class PerimeterCalculatorVisitor : ShapeVisitor {
    private var totalPerimeter = 0.0

    override fun visitCircle(circle: Circle) {
        val perimeter = 2 * Math.PI * circle.getRadius()
        println("📏 Circle (radius=${circle.getRadius()}): Perimeter = ${String.format("%.2f", perimeter)}")
        totalPerimeter += perimeter
    }

    override fun visitRectangle(rectangle: Rectangle) {
        val perimeter = 2 * (rectangle.getWidth() + rectangle.getHeight())
        println("📏 Rectangle (${rectangle.getWidth()}x${rectangle.getHeight()}): Perimeter = ${String.format("%.2f", perimeter)}")
        totalPerimeter += perimeter
    }

    override fun visitTriangle(triangle: Triangle) {
        val perimeter = triangle.getSide1() + triangle.getSide2() + triangle.getSide3()
        println("📏 Triangle: Perimeter = ${String.format("%.2f", perimeter)}")
        totalPerimeter += perimeter
    }

    fun getTotalPerimeter(): Double = totalPerimeter
}

// ============================================================================
// ANOTHER EXAMPLE: EMPLOYEE REPORT
// ============================================================================

interface EmployeeElement {
    fun accept(visitor: EmployeeVisitor)
}

class Manager(private val name: String, private val salary: Double) : EmployeeElement {
    override fun accept(visitor: EmployeeVisitor) {
        visitor.visitManager(this)
    }

    fun getName(): String = name
    fun getSalary(): Double = salary
}

class Developer(private val name: String, private val salary: Double, private val skills: List<String>) : EmployeeElement {
    override fun accept(visitor: EmployeeVisitor) {
        visitor.visitDeveloper(this)
    }

    fun getName(): String = name
    fun getSalary(): Double = salary
    fun getSkills(): List<String> = skills
}

class Designer(private val name: String, private val salary: Double, private val toolExpertise: String) : EmployeeElement {
    override fun accept(visitor: EmployeeVisitor) {
        visitor.visitDesigner(this)
    }

    fun getName(): String = name
    fun getSalary(): Double = salary
    fun getToolExpertise(): String = toolExpertise
}

interface EmployeeVisitor {
    fun visitManager(manager: Manager)
    fun visitDeveloper(developer: Developer)
    fun visitDesigner(designer: Designer)
}

class SalaryVisitor : EmployeeVisitor {
    private var totalSalary = 0.0

    override fun visitManager(manager: Manager) {
        println("💰 Manager: ${manager.getName()} - Salary: \$${manager.getSalary()}")
        totalSalary += manager.getSalary()
    }

    override fun visitDeveloper(developer: Developer) {
        println("💰 Developer: ${developer.getName()} - Salary: \$${developer.getSalary()}")
        totalSalary += developer.getSalary()
    }

    override fun visitDesigner(designer: Designer) {
        println("💰 Designer: ${designer.getName()} - Salary: \$${designer.getSalary()}")
        totalSalary += designer.getSalary()
    }

    fun getTotalSalary(): Double = totalSalary
}

// ============================================================================
// DEMO
// ============================================================================

fun demonstrateVisitor() {
    println("=== Visitor Pattern Demo ===\n")

    // Example 1: Shape Visitors
    println("--- Shape Calculations ---")
    val shapes: List<ShapeElement> = listOf(
        Circle(5.0),
        Rectangle(4.0, 6.0),
        Triangle(3.0, 4.0, 5.0),
        Circle(3.0)
    )

    println("📊 Calculating Areas:")
    val areaVisitor = AreaCalculatorVisitor()
    shapes.forEach { it.accept(areaVisitor) }
    println("Total Area: ${String.format("%.2f", areaVisitor.getTotalArea())}\n")

    println("📊 Calculating Perimeters:")
    val perimeterVisitor = PerimeterCalculatorVisitor()
    shapes.forEach { it.accept(perimeterVisitor) }
    println("Total Perimeter: ${String.format("%.2f", perimeterVisitor.getTotalPerimeter())}\n")

    // Example 2: Employee Visitors
    println("--- Employee Reports ---")
    val employees: List<EmployeeElement> = listOf(
        Manager("John Manager", 120000.0),
        Developer("Alice Dev", 90000.0, listOf("Kotlin", "Java", "Python")),
        Designer("Bob Design", 80000.0, "Figma"),
        Developer("Carol Dev", 95000.0, listOf("TypeScript", "React")),
        Manager("David Manager", 125000.0)
    )

    println("💼 Employee Salary Report:")
    val salaryVisitor = SalaryVisitor()
    employees.forEach { it.accept(salaryVisitor) }
    println("Total Company Salary: \$${salaryVisitor.getTotalSalary()}")
}

