package com.shubham.majam.other

open class Event<out T>(private val data: T) {

    var hasBeenHandled = false //false until the event has been handled
        private set

    fun getContentIfNotHandled(): T? {
        return if(hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            data
        }
    }

    fun peekContent() = data /* if the user needs to get the data even though it is handled already,
    this class comes in handy then */
}