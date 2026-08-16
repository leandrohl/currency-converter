package com.leandrosilva

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        get("/currency_types") {
            call.respond(
                CurrencyTypesResult(
                    values = commonCurrencyTypes
                )
            )
        }

        // conversão  de valores, moeda atual => moeda alvo, taxa de conversão entre essas moesdas
        get("/exchange_rate/{from}/{to}") {
            val fromCurrency = call.request.queryParameters["from"]
            val toCurrency = call.request.queryParameters["to"]

            if (fromCurrency == null || toCurrency == null) {
                call.respondText("Missing 'from' or 'to' query parameters", status = io.ktor.http.HttpStatusCode.BadRequest)
                return@get
            }

            val exchangeRate = 1.23 // Exemplo de taxa de câmbio

            call.respondText("Exchange rate from $fromCurrency to $toCurrency is $exchangeRate")
        }

    }
}