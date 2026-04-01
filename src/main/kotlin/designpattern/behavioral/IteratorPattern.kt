package designpattern.behavioral

/**
 * Iterator Pattern Implementation
 *
 * WHAT IS THE ITERATOR PATTERN?
 * Provides a way to access elements of a collection sequentially without
 * exposing its underlying representation.
 *
 * KEY CONCEPTS:
 * - Iterator: Interface for accessing elements
 * - ConcreteIterator: Implements iteration
 * - Collection: Contains elements
 * - Aggregate: Interface for creating iterator
 *
 * REAL-WORLD ANALOGY:
 * Book reading:
 *   - Book is the collection
 *   - Bookmark is the iterator (keeps track of current page)
 *   - You can move forward, backward, check if more pages exist
 *
 * BENEFITS:
 * - Decouples collection from iteration logic
 * - Supports multiple concurrent iterations
 * - Simplifies collection interface
 * - Works with different collection types
 *
 * WHEN TO USE:
 * - Access collection elements sequentially
 * - Hide collection internal structure
 * - Multiple traversals of same collection
 * - Different traversal patterns
 */

// ============================================================================
// ITERATOR INTERFACE
// ============================================================================

interface Iterator<T> {
    fun hasNext(): Boolean
    fun next(): T
    fun hasPrevious(): Boolean
    fun previous(): T
}

// ============================================================================
// COLLECTION INTERFACE
// ============================================================================

interface Collection<T> {
    fun iterator(): Iterator<T>
    fun add(element: T)
    fun remove(element: T)
    fun size(): Int
}

// ============================================================================
// CONCRETE COLLECTION
// ============================================================================

class BookShelf<T> : Collection<T> {
    private val books = mutableListOf<T>()

    override fun iterator(): Iterator<T> {
        return BookShelfIterator(books)
    }

    override fun add(element: T) {
        books.add(element)
    }

    override fun remove(element: T) {
        books.remove(element)
    }

    override fun size(): Int = books.size
}

// ============================================================================
// CONCRETE ITERATOR
// ============================================================================

class BookShelfIterator<T>(private val books: List<T>) : Iterator<T> {
    private var currentIndex = 0

    override fun hasNext(): Boolean = currentIndex < books.size

    override fun next(): T {
        if (!hasNext()) {
            throw IndexOutOfBoundsException("No more elements")
        }
        return books[currentIndex++]
    }

    override fun hasPrevious(): Boolean = currentIndex > 0

    override fun previous(): T {
        if (!hasPrevious()) {
            throw IndexOutOfBoundsException("No previous elements")
        }
        return books[--currentIndex]
    }
}

// ============================================================================
// EXAMPLE DOMAIN CLASS
// ============================================================================

data class Book(
    val title: String,
    val author: String,
    val year: Int
)

// ============================================================================
// ANOTHER EXAMPLE: CUSTOM ITERATORS
// ============================================================================

class NumberCollection(private val numbers: List<Int>) {
    fun forwardIterator(): NumericIterator {
        return ForwardIterator(numbers)
    }

    fun reverseIterator(): NumericIterator {
        return ReverseIterator(numbers)
    }

    fun evenNumbersIterator(): NumericIterator {
        return EvenNumbersIterator(numbers)
    }

    fun oddNumbersIterator(): NumericIterator {
        return OddNumbersIterator(numbers)
    }
}

interface NumericIterator {
    fun hasNext(): Boolean
    fun next(): Int
}

class ForwardIterator(private val numbers: List<Int>) : NumericIterator {
    private var index = 0

    override fun hasNext(): Boolean = index < numbers.size

    override fun next(): Int {
        if (!hasNext()) throw NoSuchElementException()
        return numbers[index++]
    }
}

class ReverseIterator(private val numbers: List<Int>) : NumericIterator {
    private var index = numbers.size - 1

    override fun hasNext(): Boolean = index >= 0

    override fun next(): Int {
        if (!hasNext()) throw NoSuchElementException()
        return numbers[index--]
    }
}

class EvenNumbersIterator(private val numbers: List<Int>) : NumericIterator {
    private var index = 0

    override fun hasNext(): Boolean {
        while (index < numbers.size) {
            if (numbers[index] % 2 == 0) {
                return true
            }
            index++
        }
        return false
    }

    override fun next(): Int {
        if (!hasNext()) throw NoSuchElementException()
        return numbers[index++]
    }
}

class OddNumbersIterator(private val numbers: List<Int>) : NumericIterator {
    private var index = 0

    override fun hasNext(): Boolean {
        while (index < numbers.size) {
            if (numbers[index] % 2 != 0) {
                return true
            }
            index++
        }
        return false
    }

    override fun next(): Int {
        if (!hasNext()) throw NoSuchElementException()
        return numbers[index++]
    }
}

// ============================================================================
// DEMO
// ============================================================================

fun demonstrateIterator() {
    println("=== Iterator Pattern Demo ===\n")

    // Example 1: Book Shelf Iterator
    println("--- Book Shelf Iterator ---")
    val bookShelf = BookShelf<Book>()

    bookShelf.add(Book("The Great Gatsby", "F. Scott Fitzgerald", 1925))
    bookShelf.add(Book("To Kill a Mockingbird", "Harper Lee", 1960))
    bookShelf.add(Book("1984", "George Orwell", 1949))
    bookShelf.add(Book("Pride and Prejudice", "Jane Austen", 1813))

    println("📚 Books in shelf: ${bookShelf.size()}")
    println("\n📖 Iterating forward:")
    val iterator = bookShelf.iterator()
    while (iterator.hasNext()) {
        val book = iterator.next()
        println("   - ${book.title} by ${book.author} (${book.year})")
    }

    println("\n📖 Iterating backward:")
    while (iterator.hasPrevious()) {
        val book = iterator.previous()
        println("   - ${book.title}")
    }

    // Example 2: Custom Numeric Iterators
    println("\n--- Numeric Collection Iterators ---")
    val numbers = NumberCollection(listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10))

    println("\n🔢 Forward iteration:")
    val forward = numbers.forwardIterator()
    while (forward.hasNext()) {
        print("${forward.next()} ")
    }
    println()

    println("\n🔢 Reverse iteration:")
    val reverse = numbers.reverseIterator()
    while (reverse.hasNext()) {
        print("${reverse.next()} ")
    }
    println()

    println("\n🔢 Even numbers only:")
    val evenIterator = numbers.evenNumbersIterator()
    while (evenIterator.hasNext()) {
        print("${evenIterator.next()} ")
    }
    println()

    println("\n🔢 Odd numbers only:")
    val oddIterator = numbers.oddNumbersIterator()
    while (oddIterator.hasNext()) {
        print("${oddIterator.next()} ")
    }
    println()
}

