package com.polidea.androidthings.driver.uln2003.driver

enum class ULN2003Resolution(val id: Int) {
    FULL(Metadata.ID_FULL),
    HALF(Metadata.ID_HALF);

    companion object {
        fun getFromId(id: Int): ULN2003Resolution =
                values().firstOrNull { it.id == id } ?:
                        throw IllegalArgumentException("Invalid resolution id: $id")
    }

    private class Metadata {
        companion object {
            val ID_FULL = 1
            val ID_HALF = 2
        }
    }
}