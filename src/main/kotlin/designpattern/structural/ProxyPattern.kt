package designpattern.structural

/**
 * Proxy Pattern Implementation
 *
 * WHAT IS THE PROXY PATTERN?
 * Provides a surrogate or placeholder for another object to control access to it.
 * Acts as an intermediary between client and real object.
 *
 * KEY CONCEPTS:
 * - Subject: Interface for both Proxy and Real Subject
 * - RealSubject: The actual object
 * - Proxy: Controls access to RealSubject
 * - Client: Uses Proxy instead of RealSubject
 *
 * REAL-WORLD ANALOGY:
 * Secretary (Proxy) for an executive (RealSubject):
 *   - Secretary screens calls and emails
 *   - Only important requests reach the executive
 *   - Secretary controls access to the executive
 *
 * BENEFITS:
 * - Controls access to another object
 * - Lazy initialization (Virtual Proxy)
 * - Logging and caching
 * - Access control and permissions
 * - Remote method invocation (Remote Proxy)
 *
 * WHEN TO USE:
 * - Lazy initialization of expensive objects (Virtual Proxy)
 * - Access control (Protection Proxy)
 * - Logging and caching (Logging Proxy)
 * - Remote objects (Remote Proxy)
 * - Smart references with additional behavior
 */

// ============================================================================
// SUBJECT INTERFACE
// ============================================================================

interface DataService {
    fun fetchData(): String
}

// ============================================================================
// REAL SUBJECT (Expensive operation)
// ============================================================================

class RealDataService : DataService {
    init {
        println("⚠️ Expensive initialization of RealDataService")
        Thread.sleep(1000) // Simulate expensive initialization
        println("✅ RealDataService initialized")
    }

    override fun fetchData(): String {
        println("📡 Fetching data from real service...")
        return "Important Data from Real Service"
    }
}

// ============================================================================
// VIRTUAL PROXY (Lazy Initialization)
// ============================================================================

class LazyDataServiceProxy : DataService {
    private var realService: RealDataService? = null

    private fun getRealService(): RealDataService {
        if (realService == null) {
            realService = RealDataService()
        }
        return realService!!
    }

    override fun fetchData(): String {
        println("🔗 Proxy: Lazy loading real service...")
        return getRealService().fetchData()
    }
}

// ============================================================================
// ANOTHER EXAMPLE: PROTECTION PROXY (Access Control)
// ============================================================================

interface Database {
    fun create(id: Int, data: String)
    fun read(id: Int): String
    fun update(id: Int, data: String)
    fun delete(id: Int)
}

class RealDatabase : Database {
    private val store = mutableMapOf<Int, String>()

    override fun create(id: Int, data: String) {
        store[id] = data
        println("✅ Database: Created record $id")
    }

    override fun read(id: Int): String {
        println("✅ Database: Reading record $id")
        return store[id] ?: "No data found"
    }

    override fun update(id: Int, data: String) {
        store[id] = data
        println("✅ Database: Updated record $id")
    }

    override fun delete(id: Int) {
        store.remove(id)
        println("✅ Database: Deleted record $id")
    }
}

enum class UserRole {
    ADMIN, USER, GUEST
}

class ProtectionDatabaseProxy(
    private val realDatabase: RealDatabase,
    private val userRole: UserRole
) : Database {

    override fun create(id: Int, data: String) {
        if (userRole == UserRole.ADMIN) {
            realDatabase.create(id, data)
        } else {
            println("❌ Access Denied: Only ADMIN can create records")
        }
    }

    override fun read(id: Int): String {
        return if (userRole in listOf(UserRole.ADMIN, UserRole.USER)) {
            realDatabase.read(id)
        } else {
            println("❌ Access Denied: GUEST cannot read records")
            "Access Denied"
        }
    }

    override fun update(id: Int, data: String) {
        if (userRole == UserRole.ADMIN) {
            realDatabase.update(id, data)
        } else {
            println("❌ Access Denied: Only ADMIN can update records")
        }
    }

    override fun delete(id: Int) {
        if (userRole == UserRole.ADMIN) {
            realDatabase.delete(id)
        } else {
            println("❌ Access Denied: Only ADMIN can delete records")
        }
    }
}

// ============================================================================
// LOGGING PROXY (Adds logging behavior)
// ============================================================================

class LoggingDatabaseProxy(
    private val database: Database
) : Database {

    override fun create(id: Int, data: String) {
        println("📝 [LOG] CREATE operation on record $id")
        database.create(id, data)
    }

    override fun read(id: Int): String {
        println("📝 [LOG] READ operation on record $id")
        return database.read(id)
    }

    override fun update(id: Int, data: String) {
        println("📝 [LOG] UPDATE operation on record $id")
        database.update(id, data)
    }

    override fun delete(id: Int) {
        println("📝 [LOG] DELETE operation on record $id")
        database.delete(id)
    }
}

// ============================================================================
// CACHING PROXY (Adds caching behavior)
// ============================================================================

class CachingDatabaseProxy(
    private val database: Database
) : Database {
    private val cache = mutableMapOf<Int, String>()

    override fun create(id: Int, data: String) {
        database.create(id, data)
        cache[id] = data
    }

    override fun read(id: Int): String {
        return if (cache.containsKey(id)) {
            println("💾 [CACHE HIT] Returning cached data for record $id")
            cache[id]!!
        } else {
            println("💾 [CACHE MISS] Fetching from database")
            val data = database.read(id)
            cache[id] = data
            data
        }
    }

    override fun update(id: Int, data: String) {
        database.update(id, data)
        cache[id] = data
    }

    override fun delete(id: Int) {
        database.delete(id)
        cache.remove(id)
    }

    fun clearCache() {
        cache.clear()
        println("💾 Cache cleared")
    }
}

fun main() {
    demonstrateProxy()
}
// ============================================================================
// DEMO
// ============================================================================

fun demonstrateProxy() {
    println("=== Proxy Pattern Demo ===\n")

    // Example 1: Virtual Proxy (Lazy Initialization)
    println("--- Virtual Proxy (Lazy Initialization) ---")
    val lazyProxy = LazyDataServiceProxy()
    println("Proxy created (service not initialized yet)\n")

    println("First call to fetchData():")
    println(lazyProxy.fetchData())

    println("\nSecond call to fetchData() (reuses initialized service):")
    println(lazyProxy.fetchData())

    // Example 2: Protection Proxy (Access Control)
    println("\n--- Protection Proxy (Access Control) ---")
    val realDb = RealDatabase()

    println("\nAdmin access:")
    val adminDb = ProtectionDatabaseProxy(realDb, UserRole.ADMIN)
    adminDb.create(1, "Admin Data")
    adminDb.read(1)

    println("\nUser access:")
    val userDb = ProtectionDatabaseProxy(realDb, UserRole.USER)
    userDb.read(1)
    userDb.create(2, "User Data") // This will be denied

    println("\nGuest access:")
    val guestDb = ProtectionDatabaseProxy(realDb, UserRole.GUEST)
    guestDb.read(1) // This will be denied

    // Example 3: Logging Proxy
    println("\n--- Logging Proxy ---")
    val loggingDb = LoggingDatabaseProxy(realDb)
    loggingDb.create(2, "Logged Data")
    loggingDb.read(2)

    // Example 4: Caching Proxy
    println("\n--- Caching Proxy ---")
    val cachingDb = CachingDatabaseProxy(realDb)
    cachingDb.create(3, "Cached Data")

    println("\nFirst read (cache miss):")
    cachingDb.read(3)

    println("\nSecond read (cache hit):")
    cachingDb.read(3)

    println("\nSecond read again (cache hit):")
    cachingDb.read(3)

    // Example 5: Combining proxies
    println("\n--- Combined Proxy (Logging + Caching) ---")
    val combinedDb = LoggingDatabaseProxy(CachingDatabaseProxy(realDb))
    combinedDb.create(4, "Combined Proxy Data")
    println("\nFirst read:")
    combinedDb.read(4)
    println("\nSecond read:")
    combinedDb.read(4)
}

