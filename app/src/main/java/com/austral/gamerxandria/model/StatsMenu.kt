package com.austral.gamerxandria.model

data class StatsMenu<T: StatsItem>(
    val title: String,
    val isExpanded: Boolean = false,
    val items: List<T> = emptyList<T>()
)

interface StatsItem {
    fun displayInformation(): String
}

data class StringItem(val statTitle: String, val statDescription: String): StatsItem {

    override fun displayInformation(): String {
        TODO("Not yet implemented")
    }
}
