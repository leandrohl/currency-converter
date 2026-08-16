package com.leandrosilva

import com.leandrosilva.model.CurrencyTypesResult
import com.leandrosilva.model.ExchangeRateResult
import com.leandrosilva.model.commonCurrencyTypes
import com.leandrosilva.model.currencyConversionMap
import com.leandrosilva.model.orUnknown
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
            val from = call.parameters["from"] ?: return@get call.respondText(
                status = io.ktor.http.HttpStatusCode.BadRequest,
                text = "Não foi possível obter o acrônimo da moeda atual."
            )

            val to = call.parameters["to"] ?: return@get call.respondText(
                status = io.ktor.http.HttpStatusCode.BadRequest,
                text = "Não foi possível obter o acrônimo da moeda atual."
            )

            call.respond(
                ExchangeRateResult(
                    from = from.orUnknown(),
                    to = to.orUnknown(),
                    exchangeRate = currencyConversionMap[from]?.get(to) ?: 0.0
                )
            )


        }

    }
}