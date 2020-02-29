package fr.legrand.runalytics.data.component

interface LogComponent {
    fun i(message : String?)
    fun e(message : String?)
    fun startSession(id : Long)
    fun endSession(id : Long)
}