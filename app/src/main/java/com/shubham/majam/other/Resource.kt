package com.shubham.majam.other

/* T is a Generic Type Parameter whereas the keyword out restrains the input methods
* i.e. when we use 'out', all the input methods in the class won't compile
* We can also use 'out' to pass the parent class of the parameter we pass e.g. if
* we intend to pass int, float & double into our class, then we can pass OUT NUMBER
* which will include all the 3 types */
data class Resource<out T>(val status: Status, val data: T?, val message: String?) {

    companion object {

        fun <T> success(data: T?) = Resource(Status.SUCCESS, data, null)

        fun <T> error(message: String, data: T?) = Resource(Status.ERROR, data, message)

        fun <T> loading(data: T?) = Resource(Status.LOADING, data, null)

    }
}

enum class Status {
    SUCCESS,
    ERROR,
    LOADING
}