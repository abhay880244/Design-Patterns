package designpattern.behavioral

/**
 * Mediator Pattern Implementation
 *
 * WHAT IS THE MEDIATOR PATTERN?
 * Defines an object that encapsulates how a set of objects interact.
 * Promotes loose coupling by keeping objects from referring to each other.
 *
 * KEY CONCEPTS:
 * - Mediator: Central hub for communication
 * - Colleague: Objects that communicate through mediator
 * - Direct interactions replaced by mediator interactions
 * - Mediator knows about all colleagues
 *
 * REAL-WORLD ANALOGY:
 * Air traffic control tower:
 *   - Airplanes (Colleagues) don't communicate directly
 *   - Control tower (Mediator) coordinates all flights
 *   - All communication goes through the tower
 *
 * BENEFITS:
 * - Decouples colleagues from each other
 * - Centralizes control logic
 * - Easier to maintain and modify communication
 * - Reusable colleague objects
 *
 * WHEN TO USE:
 * - Complex communication between objects
 * - Many objects with interdependencies
 * - Want to decouple objects
 * - Reducing subclass count
 */

// ============================================================================
// MEDIATOR INTERFACE
// ============================================================================

interface ChatRoomMediator {
    fun sendMessage(message: String, sender: User)
    fun addUser(user: User)
    fun removeUser(user: User)
}

// ============================================================================
// CONCRETE MEDIATOR
// ============================================================================

class ChatRoom : ChatRoomMediator {
    private val users = mutableListOf<User>()
    private val messageHistory = mutableListOf<String>()

    override fun sendMessage(message: String, sender: User) {
        val timestamp = java.time.LocalDateTime.now().format(
            java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss")
        )
        val formattedMessage = "[$timestamp] ${sender.getName()}: $message"

        println("💬 $formattedMessage")
        messageHistory.add(formattedMessage)

        // Notify all users except sender
        users.filter { it != sender }.forEach { user ->
            user.receiveMessage(message, sender.getName())
        }
    }

    override fun addUser(user: User) {
        users.add(user)
        println("✅ ${user.getName()} joined the chat room")
        broadcastSystemMessage("${user.getName()} has joined")
    }

    override fun removeUser(user: User) {
        users.remove(user)
        println("❌ ${user.getName()} left the chat room")
        broadcastSystemMessage("${user.getName()} has left")
    }

    private fun broadcastSystemMessage(message: String) {
        users.forEach { user ->
            user.receiveSystemMessage(message)
        }
    }

    fun getMessageHistory(): List<String> = messageHistory.toList()

    fun getUserCount(): Int = users.size
}

// ============================================================================
// COLLEAGUE
// ============================================================================

abstract class User(
    private val name: String,
    protected val chatRoom: ChatRoomMediator
) {
    fun getName(): String = name

    fun sendMessage(message: String) {
        chatRoom.sendMessage(message, this)
    }

    open fun receiveMessage(message: String, sender: String) {
        println("   📩 ${getName()} received from $sender: $message")
    }

    open fun receiveSystemMessage(message: String) {
        println("   📢 ${getName()} notified: $message")
    }
}

// ============================================================================
// CONCRETE COLLEAGUES
// ============================================================================

class RegularUser(name: String, chatRoom: ChatRoomMediator) : User(name, chatRoom)

class AdminUser(name: String, chatRoom: ChatRoomMediator) : User(name, chatRoom) {
    override fun receiveSystemMessage(message: String) {
        println("   🔔 [ADMIN] ${getName()} notified: $message")
    }

    fun shutDownChat() {
        println("🛑 Admin ${getName()} is shutting down the chat room")
    }
}

// ============================================================================
// ANOTHER EXAMPLE: FLIGHT CONTROL SYSTEM
// ============================================================================

interface AirTrafficControl {
    fun registerAircraft(aircraft: Aircraft)
    fun requestLanding(aircraft: Aircraft): Boolean
    fun requestTakeoff(aircraft: Aircraft): Boolean
    fun notifyAircraft(message: String, exclude: Aircraft? = null)
}

class ControlTower : AirTrafficControl {
    private val aircraftList = mutableListOf<Aircraft>()
    private var landingQueue = mutableListOf<Aircraft>()
    private var activeFlights = mutableListOf<Aircraft>()

    override fun registerAircraft(aircraft: Aircraft) {
        aircraftList.add(aircraft)
        println("✈️ ${aircraft.getCallsign()} registered with control tower")
    }

    override fun requestLanding(aircraft: Aircraft): Boolean {
        return if (landingQueue.isEmpty() && activeFlights.size < 3) {
            println("✅ ${aircraft.getCallsign()} cleared to land")
            landingQueue.add(aircraft)
            activeFlights.remove(aircraft)
            true
        } else {
            println("⏳ ${aircraft.getCallsign()} must wait for landing clearance")
            false
        }
    }

    override fun requestTakeoff(aircraft: Aircraft): Boolean {
        return if (activeFlights.size < 3) {
            println("✅ ${aircraft.getCallsign()} cleared for takeoff")
            activeFlights.add(aircraft)
            landingQueue.remove(aircraft)
            true
        } else {
            println("⏳ ${aircraft.getCallsign()} must wait for takeoff clearance")
            false
        }
    }

    override fun notifyAircraft(message: String, exclude: Aircraft?) {
        aircraftList.filter { it != exclude }.forEach { aircraft ->
            aircraft.receiveInstruction(message)
        }
    }

    fun getFlightStatus(): String {
        return "Active flights: ${activeFlights.size}, Waiting to land: ${landingQueue.size}"
    }
}

abstract class Aircraft(
    private val callsign: String,
    protected val control: AirTrafficControl
) {
    fun getCallsign(): String = callsign

    fun requestLanding() {
        control.requestLanding(this)
    }

    fun requestTakeoff() {
        control.requestTakeoff(this)
    }

    fun receiveInstruction(instruction: String) {
        println("   📡 $callsign received: $instruction")
    }
}

class PassengerFlight(callsign: String, control: AirTrafficControl) : Aircraft(callsign, control)

class CargoPlan(callsign: String, control: AirTrafficControl) : Aircraft(callsign, control)

// ============================================================================
// DEMO
// ============================================================================

fun demonstrateMediator() {
    println("=== Mediator Pattern Demo ===\n")

    // Example 1: Chat Room
    println("--- Chat Room System ---")
    val chatRoom = ChatRoom()

    val user1 = RegularUser("Alice", chatRoom)
    val user2 = RegularUser("Bob", chatRoom)
    val user3 = AdminUser("Charlie", chatRoom)

    chatRoom.addUser(user1)
    chatRoom.addUser(user2)
    chatRoom.addUser(user3)

    println("\n💬 Chat Messages:")
    user1.sendMessage("Hello everyone!")
    user2.sendMessage("Hi Alice, how are you?")
    user1.sendMessage("I'm doing great, thanks!")
    user3.sendMessage("Great discussion everyone!")

    println("\n📜 Message History:")
    chatRoom.getMessageHistory().forEach { println("  - $it") }

    println("\n👥 Chat Room Users: ${chatRoom.getUserCount()}")

    chatRoom.removeUser(user2)

    // Example 2: Air Traffic Control
    println("\n\n--- Air Traffic Control System ---")
    val controlTower = ControlTower()

    val flight1 = PassengerFlight("AA100", controlTower)
    val flight2 = PassengerFlight("UA200", controlTower)
    val flight3 = CargoPlan("FX300", controlTower)

    controlTower.registerAircraft(flight1)
    controlTower.registerAircraft(flight2)
    controlTower.registerAircraft(flight3)

    println("\n✈️ Flight Operations:")
    println("${flight1.getCallsign()} requesting takeoff:")
    flight1.requestTakeoff()

    println("\n${flight2.getCallsign()} requesting takeoff:")
    flight2.requestTakeoff()

    println("\n${flight3.getCallsign()} requesting takeoff:")
    flight3.requestTakeoff()

    println("\n${flight1.getCallsign()} requesting landing:")
    flight1.requestLanding()

    println("\nCurrent Status: ${controlTower.getFlightStatus()}")

    controlTower.notifyAircraft("All flights reduce altitude to 10,000 feet", flight1)
}

