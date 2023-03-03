package dk.itu.moapd.scootersharing.oska

import android.content.Context
import android.os.Parcelable.ClassLoaderCreator
import java.util.Random

class RidesDB private constructor(context: Context){
    private val rides = ArrayList<Scooter>()
    companion object : RidesDBHolder<RidesDB, Context>(::RidesDB)


    init {
        rides.add(
            Scooter("CPH001", "Alberstlund", randomDate())
        )
        rides.add(
            Scooter("CPH002", "Sverige", randomDate())
        )
        rides.add(
            Scooter("CPH003", "Finland", randomDate())
        )
        rides.add(
            Scooter("CPH003", "Finland", randomDate())
        )
        rides.add(
            Scooter("CPH003", "Finland", randomDate())
        )
        rides.add(
            Scooter("CPH003", "Finland", randomDate())
        )
        rides.add(
            Scooter("CPH003", "Finland", randomDate())
        )
        rides.add(
            Scooter("CPH003", "Finland", randomDate())
        )
        rides.add(
            Scooter("CPH003", "Finland", randomDate())
        )
        rides.add(
            Scooter("CPH003", "Finland", randomDate())
        )
        rides.add(
            Scooter("CPH003", "Finland", randomDate())
        )
    }

    fun getRidesList(): List<Scooter> {
        return rides
    }
    fun addScooter(name: String, location: String){
        rides.add(Scooter(name, location, randomDate()))
    }

    fun updateCurrentScooter(time: Long, number: Int){
       rides[number]._timestamp=time

    }

    fun getCurrentScooter(number: Int): Scooter? {
        return rides[number]
    }
    fun deleteSelectedScooter(number: Int) {
        rides.remove(rides[number])
    }

    fun getCurrentScooterInfo(number: Int): String {
        return getCurrentScooter(number).toString()
    }


    private fun randomDate() : Long {
        val random = Random()
        val now = System.currentTimeMillis()
        val year = random.nextDouble() * 1000 * 60 * 60 * 24 * 365
        return (now - year).toLong()
    }
}

open class  RidesDBHolder<out T: Any, in A>(creator: (A) -> T) {
    private var creator: ((A) -> T)? = creator

    @Volatile private var instance: T? = null

    fun get(arg: A): T {
        val checkInstance = instance
        if (checkInstance != null)
            return checkInstance

        return synchronized(this){
            val checkInstanceAgain = instance
            if(checkInstanceAgain != null)
                checkInstanceAgain

            else {
                val created = creator!!(arg)
                instance = created
                creator = null
                created
            }


        }
    }
}