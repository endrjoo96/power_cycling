package com.nakolanie.powercycling

enum class EfficiencyClass(val value: Float, val friendlyName: String) {
    F(2.5f, "F"),
    E(2f, "E"),
    D(1.5f, "D"),
    C(1.25f, "C"),
    B(1.1f, "B"),
    A(1f, "A"),
    Ap(0.9f, "A+"),
    App(0.85f, "A++"),
    Appp(0.775f, "A+++"),
}