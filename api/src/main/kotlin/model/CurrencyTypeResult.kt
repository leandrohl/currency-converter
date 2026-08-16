package com.leandrosilva.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CurrencyType(
    val acronym: String,
    val name: String,
    val symbol: String,
    @SerialName("country_flag_image_url")
    val countryFlagImageUrl: String
)


@Serializable
data class CurrencyTypesResult(
    val values: List<CurrencyType>
)

val commonCurrencyTypes = listOf(
    CurrencyType(
        acronym = "USD",
        name = "US Dollar",
        symbol = "$",
        countryFlagImageUrl = "https://flagcdn.com/w320/us.png"
    ),
    CurrencyType(
        acronym = "EUR",
        name = "Euro",
        symbol = "€",
        countryFlagImageUrl = "https://flagcdn.com/w320/eu.png"
    ),
    CurrencyType(
        acronym = "JPY",
        name = "Japanese Yen",
        symbol = "¥",
        countryFlagImageUrl = "https://flagcdn.com/w320/jp.png"
    ),
    CurrencyType(
        acronym = "GBP",
        name = "British Pound Sterling",
        symbol = "£",
        countryFlagImageUrl = "https://flagcdn.com/w320/gb.png"
    ),
    CurrencyType(
        acronym = "CNY",
        name = "Chinese Yuan Renminbi",
        symbol = "¥",
        countryFlagImageUrl = "https://flagcdn.com/w320/cn.png"
    ),
    CurrencyType(
        acronym = "BRL",
        name = "Brazilian Real",
        symbol = "R$",
        countryFlagImageUrl = "https://flagcdn.com/w320/br.png"
    ),
    CurrencyType(
        acronym = "CHF",
        name = "Swiss Franc",
        symbol = "CHF",
        countryFlagImageUrl = "https://flagcdn.com/w320/ch.png"
    ),
    CurrencyType(
        acronym = "CAD",
        name = "Canadian Dollar",
        symbol = "C$",
        countryFlagImageUrl = "https://flagcdn.com/w320/ca.png"
    ),
    CurrencyType(
        acronym = "AUD",
        name = "Australian Dollar",
        symbol = "A$",
        countryFlagImageUrl = "https://flagcdn.com/w320/au.png"
    ),
    CurrencyType(
        acronym = "INR",
        name = "Indian Rupee",
        symbol = "₹",
        countryFlagImageUrl = "https://flagcdn.com/w320/in.png"
    )
)