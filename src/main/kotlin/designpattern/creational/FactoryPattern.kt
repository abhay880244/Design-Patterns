package designpattern.creational

/**
 * Factory Pattern Explanation:
 *
 * The Factory pattern is used to create objects without specifying their
 * exact classes. Instead of using 'new' directly, you call a factory method
 * that returns the appropriate object based on the parameters provided.
 *
 * Benefits:
 * - Decouples object creation from the code that uses them
 * - Centralizes object creation logic in one place
 * - Makes it easy to add new types without changing existing code
 * - Follows the Open/Closed principle (open for extension, closed for modification)
 */

interface Factory<T> {
    fun create(type : Vehicle.TYPE): T
}
var vehicleFactory = VehicleFactory()
open class Vehicle {
    enum class TYPE {
        CAR, BIKE, TRUCK
    }
}

class Car : Vehicle() {

}class Bike : Vehicle() {

}class Truck : Vehicle() {

}

class VehicleFactory : Factory<Vehicle> {
    override fun  create(type: Vehicle.TYPE): Vehicle {
        return when(type){
            Vehicle.TYPE.CAR -> Car()
            Vehicle.TYPE.BIKE -> Bike()
            Vehicle.TYPE.TRUCK -> Truck()
        }
    }


}

