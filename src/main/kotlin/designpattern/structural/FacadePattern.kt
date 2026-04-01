package designpattern.structural

/**
 * Facade Pattern Implementation
 *
 * WHAT IS THE FACADE PATTERN?
 * Provides a unified, simplified interface to a set of interfaces in a subsystem.
 * Hides the complexity of multiple subsystems behind a single facade.
 *
 * KEY CONCEPTS:
 * - Facade: Simple interface to complex subsystems
 * - Subsystems: Multiple complex components
 * - Client: Uses only the facade, not the subsystems directly
 *
 * REAL-WORLD ANALOGY:
 * Bank teller (Facade):
 *   - Customer deals with teller
 *   - Teller handles multiple departments (Subsystems)
 *   - Customer doesn't need to know about internal processes
 *
 * BENEFITS:
 * - Simplifies client code
 * - Decouples client from subsystems
 * - Reduces dependencies
 * - Easier to maintain and modify subsystems
 *
 * WHEN TO USE:
 * - Complex subsystems with multiple interdependent classes
 * - Provide simple interface to complex library
 * - Organize subsystems into layers
 */

// ============================================================================
// COMPLEX SUBSYSTEMS
// ============================================================================

class DVDPlayer {
    fun on() {
        println("🎬 DVD Player is on")
    }

    fun off() {
        println("🎬 DVD Player is off")
    }

    fun play(movie: String) {
        println("🎬 Playing movie: $movie")
    }
}

class Projector {
    fun on() {
        println("📽️ Projector is on")
    }

    fun off() {
        println("📽️ Projector is off")
    }

    fun setInput(input: String) {
        println("📽️ Projector input set to: $input")
    }

    fun setBrightness(level: Int) {
        println("📽️ Projector brightness set to: $level%")
    }
}

class SoundSystem {
    fun on() {
        println("🔊 Sound system is on")
    }

    fun off() {
        println("🔊 Sound system is off")
    }

    fun setVolume(level: Int) {
        println("🔊 Sound volume set to: $level")
    }

    fun setSurroundSound(enabled: Boolean) {
        if (enabled) {
            println("🔊 Surround sound enabled")
        } else {
            println("🔊 Surround sound disabled")
        }
    }
}

class Lights {
    fun on() {
        println("💡 Lights are on")
    }

    fun off() {
        println("💡 Lights are off")
    }

    fun dim(level: Int) {
        println("💡 Lights dimmed to: $level%")
    }
}

class PopcornMachine {
    fun on() {
        println("🍿 Popcorn machine is on")
    }

    fun off() {
        println("🍿 Popcorn machine is off")
    }

    fun makePopcorn() {
        println("🍿 Making fresh popcorn")
    }
}

// ============================================================================
// FACADE
// ============================================================================

class HomeTheaterFacade(
    private val dvdPlayer: DVDPlayer = DVDPlayer(),
    private val projector: Projector = Projector(),
    private val soundSystem: SoundSystem = SoundSystem(),
    private val lights: Lights = Lights(),
    private val popcornMachine: PopcornMachine = PopcornMachine()
) {

    fun watchMovie(movieName: String) {
        println("\n🎭 Starting movie experience: $movieName\n")

        lights.dim(10)
        popcornMachine.on()
        popcornMachine.makePopcorn()

        soundSystem.on()
        soundSystem.setVolume(20)
        soundSystem.setSurroundSound(true)

        projector.on()
        projector.setInput("DVD")
        projector.setBrightness(85)

        dvdPlayer.on()
        dvdPlayer.play(movieName)

        println("\n🍿 Enjoy your movie!\n")
    }

    fun endMovie() {
        println("\n🎭 Shutting down home theater\n")

        dvdPlayer.off()
        dvdPlayer.off()
        projector.off()
        soundSystem.off()
        popcornMachine.off()
        lights.on()

        println("\n👋 Theater shut down\n")
    }
}

// ============================================================================
// ANOTHER EXAMPLE: HOTEL RESERVATION FACADE
// ============================================================================

class Hotel {
    fun book(hotelName: String) {
        println("🏨 Hotel $hotelName booked")
    }
}

class FlightService {
    fun book(flightNumber: String) {
        println("✈️ Flight $flightNumber booked")
    }
}

class CarRental {
    fun book(carType: String) {
        println("🚗 $carType rented")
    }
}

class TravelAgencyFacade(
    private val hotel: Hotel = Hotel(),
    private val flight: FlightService = FlightService(),
    private val carRental: CarRental = CarRental()
) {

    fun bookVacationPackage(destination: String, days: Int) {
        println("\n✈️ Booking vacation package to $destination for $days days\n")

        flight.book("AA100 to $destination")
        hotel.book("Grand Hotel in $destination")
        carRental.book("SUV")

        println("\n✅ Your vacation package is ready!\n")
    }
}

// ============================================================================
// DEMO
// ============================================================================

fun demonstrateFacade() {
    println("=== Facade Pattern Demo ===\n")

    // Example 1: Home Theater Facade
    println("--- Home Theater System ---")
    val homeTheater = HomeTheaterFacade()
    homeTheater.watchMovie("The Matrix")
    homeTheater.endMovie()

    // Example 2: Travel Agency Facade
    println("--- Travel Agency System ---")
    val travelAgency = TravelAgencyFacade()
    travelAgency.bookVacationPackage("Hawaii", 7)
}

