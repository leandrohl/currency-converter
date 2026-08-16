package com.example.currencyconverter

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.currencyconverter.databinding.ActivityMainBinding
import com.example.currencyconverter.network.model.CurrencyType
import com.example.currencyconverter.ui.CurrencyTypesAdapter
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel by viewModels<CurrencyExchangeViewModel>()

    private var exchangeRate: Double? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        viewModel.requireCurrencyTypes()
        binding.etFromExchangeValue.addCurrencyMask()

        lifecycleScope.apply {
            launch {
                viewModel.currencyTypes.collect { result ->
                    result.onSuccess { currencyTypes ->
                        binding.configureCurrencyTypeSpinners(currencyTypes)
                    }.onFailure { exception ->
                        Toast.makeText(this@MainActivity, exception.message, Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }

            launch {
                viewModel.exchangeRate.collect { result ->
                    result.onSuccess { exchangeRateResult ->
                        exchangeRateResult?.let {
                            exchangeRate = it.exchangeRate
                            binding.generateConvertedValue()
                        }
                    }.onFailure { exception ->
                        Toast.makeText(this@MainActivity, exception.message, Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }
    }

    private fun ActivityMainBinding.configureCurrencyTypeSpinners(currencyTypes: List<CurrencyType>) {
        spnFromCurrency.apply {
            adapter = CurrencyTypesAdapter(currencyTypes)
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    // funcao de atualizacao de conversão monetaria entre as selecoes de FROM e TO
                    val from = currencyTypes[position]
                    val to = currencyTypes[spnToCurrency.selectedItemPosition]

                    tvFromCurrencySymbol.text = from.symbol

                    viewModel.requireExchangeRate(
                        from = from.acronym,
                        to = to.acronym,
                    )
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
        }

        spnToCurrency.apply {
            adapter = CurrencyTypesAdapter(currencyTypes)
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val from = currencyTypes[spnFromCurrency.selectedItemPosition]
                    val to = currencyTypes[position]

                    tvToExchangeValue.text = to.symbol

                    viewModel.requireExchangeRate(
                        from = from.acronym,
                        to = to.acronym,
                    )
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    currencyTypes.firstOrNull()?.let { firstCurrencyType ->
                        tvFromCurrencySymbol.text = firstCurrencyType.symbol
                        tvToCurrencySymbol.text = firstCurrencyType.symbol

                        viewModel.requireExchangeRate(
                            from = firstCurrencyType.acronym,
                            to = firstCurrencyType.acronym,
                        )
                    }
                }
            }
        }

    }

    private fun EditText.addCurrencyMask() {
        addTextChangedListener(object : TextWatcher {
            private var currentText = ""

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (s.toString() != currentText) {
                    removeTextChangedListener(this)

                    val cleanedString = s.toString().replace("[,.]".toRegex(), "")
                    val currencyValue = cleanedString.toDoubleOrNull() ?: 0.0
                    val formatted = DecimalFormat("#,##0.00", DecimalFormatSymbols(Locale.getDefault())).format(currencyValue / 100)

                    currentText = formatted
                    setText(formatted)
                    setSelection(formatted.length)

                    binding.generateConvertedValue()

                    addTextChangedListener(this)
                }
            }
        })
    }

    private fun ActivityMainBinding.generateConvertedValue() {
        exchangeRate?.let {
            val fromValue = etFromExchangeValue.text.toString().replace("[,.]".toRegex(), "").toDoubleOrNull() ?: 0.0
            val convertedValue = fromValue * it / 100


            val formattedConvertedValue = DecimalFormat(
                "#,##0.00",
                DecimalFormatSymbols(
                    Locale.getDefault())
            ).format(convertedValue)
            tvToExchangeValue.text = formattedConvertedValue
        }
    }
}