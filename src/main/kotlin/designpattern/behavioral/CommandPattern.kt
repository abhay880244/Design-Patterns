package designpattern.behavioral

import java.util.*

/**
 * Command Pattern Implementation
 *
 * WHAT IS THE COMMAND PATTERN?
 * Encapsulates a request as an object, thereby letting you parameterize clients with 
 * different requests, queue or log requests, and support undoable operations.
 *
 * KEY CONCEPTS:
 * - Command: Interface with an execute() method
 * - ConcreteCommand: Binds a Receiver with an action
 * - Receiver: Knows how to perform the work (e.g., Light, Stereo)
 * - Invoker: Asks the command to carry out the request (e.g., RemoteControl)
 * - Client: Creates ConcreteCommand and sets its receiver
 *
 * REAL-WORLD ANALOGY:
 * A Restaurant:
 *   - Customer (Client) gives an Order (Command) to the Waiter (Invoker)
 *   - Waiter takes the Order to the Kitchen (Receiver)
 *   - Chef (Receiver) prepares the meal
 *
 * BENEFITS:
 * - Decouples the object that invokes the operation from the one that knows how to perform it
 * - Easy to add new commands without changing existing code
 * - Supports undo/redo operations
 * - Allows building complex commands from simple ones (Macro commands)
 */

// ============================================================================
// RECEIVER CLASSES
// ============================================================================

class Light(private val location: String) {
    fun on() {
        println("💡 $location Light is ON")
    }

    fun off() {
        println("🌑 $location Light is OFF")
    }
}

class Stereo(private val location: String) {
    fun on() {
        println("📻 $location Stereo is ON")
    }

    fun off() {
        println("📻 $location Stereo is OFF")
    }

    fun setCD() {
        println("📻 $location Stereo is set for CD input")
    }

    fun setVolume(volume: Int) {
        println("🔊 $location Stereo volume set to $volume")
    }
}

// ============================================================================
// COMMAND INTERFACE
// ============================================================================

interface Command {
    fun execute()
    fun undo()
}

// ============================================================================
// CONCRETE COMMANDS
// ============================================================================

class LightOnCommand(private val light: Light) : Command {
    override fun execute() {
        light.on()
    }

    override fun undo() {
        light.off()
    }
}

class LightOffCommand(private val light: Light) : Command {
    override fun execute() {
        light.off()
    }

    override fun undo() {
        light.on()
    }
}

class StereoOnWithCDCommand(private val stereo: Stereo) : Command {
    override fun execute() {
        stereo.on()
        stereo.setCD()
        stereo.setVolume(11)
    }

    override fun undo() {
        stereo.off()
    }
}

class StereoOffCommand(private val stereo: Stereo) : Command {
    override fun execute() {
        stereo.off()
    }

    override fun undo() {
        stereo.on()
        stereo.setCD()
        stereo.setVolume(11)
    }
}

class NoCommand : Command {
    override fun execute() {}
    override fun undo() {}
}

// ============================================================================
// INVOKER
// ============================================================================

class RemoteControl {
    private val onCommands = Array<Command>(7) { NoCommand() }
    private val offCommands = Array<Command>(7) { NoCommand() }
    private var undoCommand: Command = NoCommand()

    fun setCommand(slot: Int, onCommand: Command, offCommand: Command) {
        onCommands[slot] = onCommand
        offCommands[slot] = offCommand
    }

    fun onButtonWasPushed(slot: Int) {
        onCommands[slot].execute()
        undoCommand = onCommands[slot]
    }

    fun offButtonWasPushed(slot: Int) {
        offCommands[slot].execute()
        undoCommand = offCommands[slot]
    }

    fun undoButtonWasPushed() {
        print("⏪ Undoing: ")
        undoCommand.undo()
    }

    override fun toString(): String {
        val sb = StringBuilder()
        sb.append("\n------ Remote Control -------\n")
        for (i in onCommands.indices) {
            sb.append("[slot $i] ${onCommands[i].javaClass.simpleName}    ${offCommands[i].javaClass.simpleName}\n")
        }
        sb.append("[undo] ${undoCommand.javaClass.simpleName}\n")
        return sb.toString()
    }
}

// ============================================================================
// MACRO COMMAND (Example of composite pattern with command)
// ============================================================================

class MacroCommand(private val commands: List<Command>) : Command {
    override fun execute() {
        commands.forEach { it.execute() }
    }

    override fun undo() {
        commands.asReversed().forEach { it.undo() }
    }
}

// ============================================================================
// DEMO
// ============================================================================

fun demonstrateCommand() {
    println("=== Command Pattern Demo ===\n")

    val remoteControl = RemoteControl()

    // Create Receivers
    val livingRoomLight = Light("Living Room")
    val kitchenLight = Light("Kitchen")
    val stereo = Stereo("Living Room")

    // Create Commands
    val livingRoomLightOn = LightOnCommand(livingRoomLight)
    val livingRoomLightOff = LightOffCommand(livingRoomLight)
    val kitchenLightOn = LightOnCommand(kitchenLight)
    val kitchenLightOff = LightOffCommand(kitchenLight)
    val stereoOn = StereoOnWithCDCommand(stereo)
    val stereoOff = StereoOffCommand(stereo)

    // Load commands into remote slots
    remoteControl.setCommand(0, livingRoomLightOn, livingRoomLightOff)
    remoteControl.setCommand(1, kitchenLightOn, kitchenLightOff)
    remoteControl.setCommand(2, stereoOn, stereoOff)

    println(remoteControl)

    // Test slots
    println("--- Testing Slots ---")
    remoteControl.onButtonWasPushed(0)
    remoteControl.offButtonWasPushed(0)
    remoteControl.undoButtonWasPushed()

    remoteControl.onButtonWasPushed(1)
    remoteControl.offButtonWasPushed(1)

    remoteControl.onButtonWasPushed(2)
    remoteControl.undoButtonWasPushed()

    // Macro Command Test (Party Mode)
    println("\n--- Testing Macro Command (Party Mode) ---")
    val partyOn = listOf(livingRoomLightOn, kitchenLightOn, stereoOn)
    val partyOff = listOf(livingRoomLightOff, kitchenLightOff, stereoOff)

    val partyOnMacro = MacroCommand(partyOn)
    val partyOffMacro = MacroCommand(partyOff)

    remoteControl.setCommand(3, partyOnMacro, partyOffMacro)

    println("Pushing Party ON:")
    remoteControl.onButtonWasPushed(3)
    
    println("\nPushing Party OFF:")
    remoteControl.offButtonWasPushed(3)
    
    println("\nPushing Undo (Party ON again):")
    remoteControl.undoButtonWasPushed()
}
