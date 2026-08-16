package com.leandrosilva.model

fun String.orUnknown(): String {
    return if (this in currencyConversionMap.keys) this else "Desconhecido"
}