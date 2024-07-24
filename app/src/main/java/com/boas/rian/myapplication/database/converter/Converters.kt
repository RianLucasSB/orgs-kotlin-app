package com.boas.rian.myapplication.database.converter

import androidx.room.TypeConverter
import java.math.BigDecimal

class Converters {
    @TypeConverter
    fun fromDouble(valor: Double?): BigDecimal{
        return valor?.let { BigDecimal(valor.toString()) } ?: BigDecimal.ZERO
    }

    @TypeConverter
    fun bigDecimalToDouble(valor: BigDecimal?): Double? {
        return valor?.toDouble()
    }
}