package com.polidea.androidthings.driver.uln2003.driver

enum class ULN2003Resolution(val id: Int) {
    FULL(Metadata.ID_FULL),
    HALF(Metadata.ID_HALF);

    companion object {
        fun getFromId(id: Int): ULN2003Resolution {
            val resolution: ULN2003Resolution
            when (id) {
                Metadata.ID_FULL -> resolution = FULL
                Metadata.ID_HALF -> resolution = HALF
                else -> throw IllegalArgumentException("Invalid resolution id: $id")
            }

            return resolution
        }
    }

    internal class Metadata {
        companion object {
            val ID_FULL = 1
            val ID_HALF = 2
        }
    }
}