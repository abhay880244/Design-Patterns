package designpattern

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