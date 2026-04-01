package designpattern.creational

/**
 * Singleton Pattern Implementation
 * Ensures a class has only one instance and provides a global point of access to it.
 */

// Approach 1: Eager Initialization (Thread-safe by default in Kotlin)
object DatabaseConnection {
    init {
        println("DatabaseConnection instance created (Eager Initialization)")
    }

    fun connect() {
        println("Connected to database")
    }

    fun disconnect() {
        println("Disconnected from database")
    }
}

// Approach 2: Lazy Initialization with companion object
class Logger() {
    companion object {
        private var instance: Logger? = null

        fun getInstance(): Logger {
            if (instance == null) {
                instance = Logger()
            }
            return instance!!
        }
    }

    init {
        println("Logger instance created (Lazy Initialization)")
    }

    fun log(message: String) {
        println("[LOG] $message")
    }
}

// Approach 3: Thread-safe Lazy Initialization with Double-Checked Locking
class ConfigManager private constructor() {
    companion object {
        @Volatile
        private var instance: ConfigManager? = null

        fun getInstance(): ConfigManager {
            if (instance == null) {
                synchronized(this) {
                    if (instance == null) {
                        instance = ConfigManager()
                    }
                }
            }
            return instance!!
        }
    }

    init {
        println("ConfigManager instance created (Thread-safe Lazy Initialization)")
    }

    fun getConfig(key: String): String {
        return "config_value_for_$key"
    }
}

