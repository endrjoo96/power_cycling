package com.nakolanie.powercycling

enum class Quality(priceMultiplicator: Float, friendlyDescription: String) {
    GARBAGE(0.10f, "Śmieć"),
    SOMEHOW_WORKING(0.35f, "Jakoś działa"),
    DENTED(0.6f, "Powgniatane"),
    USED_LOWEND(0.8f, "Używane - niskiej jakości"),
    USED_GOOD(1f, "Używane - dobrej jakości"),
    NEW_LOWEND(2f, "Nowe - niskiej jakości"),
    NEW_MEDIOCRE(3.5f, "Nowe - średniej jakości"),
    NEW_GOOD(7f, "Nowe - dobrej jakości"),
    NEW_TOP(15f, "Nowe - najwyższa jakość")
}