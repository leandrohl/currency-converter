package com.example.currencyconverter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.currencyconverter.network.KtorHttpClient
import com.example.currencyconverter.network.model.CurrencyType
import com.example.currencyconverter.network.model.ExchangeRateResult
import com.example.currencyconverter.utils.CurrencyTypeAcronym
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CurrencyExchangeViewModel: ViewModel() {
    private val _currentTypes = MutableStateFlow<Result<List<CurrencyType>>>(Result.success(emptyList()))
    val currencyTypes: StateFlow<Result<List<CurrencyType>>> = _currentTypes.asStateFlow()

    private val _exchangeRate = MutableStateFlow(Result.success(ExchangeRateResult.empty()))
    val exchangeRate: StateFlow<Result<ExchangeRateResult>> = _exchangeRate.asStateFlow()

    fun requireCurrencyTypes() {
        viewModelScope.launch {
            _currentTypes.emit(KtorHttpClient.getCurrencyTypes().mapCatching { result ->
                result.values
            })
        }
    }

    fun requireExchangeRate(from: CurrencyTypeAcronym, to: CurrencyTypeAcronym) {
        if (from == to) {
            _exchangeRate.value = Result.success(
                ExchangeRateResult(
                    from,
                    to,
                    1.0
                )
            )
            return
        }
        viewModelScope.launch {
            _exchangeRate.emit(KtorHttpClient.getExchangeRate(from, to))
        }
    }
}