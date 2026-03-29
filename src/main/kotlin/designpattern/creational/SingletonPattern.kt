package designpattern.creational

}
    }
        return "config_value_for_$key"
    fun getConfig(key: String): String {

    }
        println("ConfigManager instance created (Thread-safe Lazy Initialization)")
    init {

    }
        }
            return instance!!
            }
                }
                    }
                        instance = ConfigManager()
                    if (instance == null) {
                synchronized(this) {
            if (instance == null) {
        fun getInstance(): ConfigManager {

        private var instance: ConfigManager? = null
        @Volatile
    companion object {
class ConfigManager private constructor() {
// Approach 3: Thread-safe Lazy Initialization with Double-Checked Locking

}
    }
        println("[LOG] $message")
    fun log(message: String) {

    }
        println("Logger instance created (Lazy Initialization)")
    init {

    }
        }
            return instance!!
            }
                instance = Logger()
            if (instance == null) {
        fun getInstance(): Logger {

        private var instance: Logger? = null
    companion object {
class Logger() {
// Approach 2: Lazy Initialization with companion object

}
    }
        println("Disconnected from database")
    fun disconnect() {

    }
        println("Connected to database")
    fun connect() {

    }
        println("DatabaseConnection instance created (Eager Initialization)")
    init {
object DatabaseConnection {
// Approach 1: Eager Initialization (Thread-safe by default in Kotlin)

 */
 * Ensures a class has only one instance and provides a global point of access to it.
 * Singleton Pattern Implementation
/**

