package com.dv.comfortly.data.errors

class SensorNotFoundError(val name: String) : NoSuchFieldException("Your device does not support sensor $name")
