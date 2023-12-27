package com.nakolanie.powercycling.enums

enum class Quality(val priceMultiplicator: Float, val efficiencyMultiplicator: Float, val friendlyDescription: String) {
    GARBAGE(         0.10f,   1.4f,  "Śmieć"),
    SOMEHOW_WORKING( 0.35f,   1.3f,  "Jakoś działa"),
    DENTED(          0.6f,    1.2f,  "Powgniatane"),
    USED_LOWEND(     0.8f,    1.1f,  "Używane - niskiej jakości"),
    USED_GOOD(       1f,      1.05f, "Używane - dobrej jakości"),
    NEW_LOWEND(      2f,      1f,    "Nowe - niskiej jakości"),
    NEW_MEDIOCRE(    3.5f,    0.95f, "Nowe - średniej jakości"),
    NEW_GOOD(        7f,      0.85f, "Nowe - dobrej jakości"),
    NEW_TOP(         15f,     0.7f,  "Nowe - najwyższa jakość")
}