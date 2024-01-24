package com.androidexpert.qurbanku_apps_skripsi.utils

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import java.lang.ref.WeakReference
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*

class MoneyTextWatcher(editText: EditText) : TextWatcher {
    private val editTextWeakReference: WeakReference<EditText> = WeakReference(editText)
    private val formatter: DecimalFormat = NumberFormat.getCurrencyInstance(Locale("id", "ID")) as DecimalFormat

    init {
        formatter.maximumFractionDigits = 0
        formatter.roundingMode = RoundingMode.FLOOR

        val symbols = formatter.decimalFormatSymbols
        symbols.currencySymbol = "" // Remove the currency symbol
        formatter.decimalFormatSymbols = symbols
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

    override fun afterTextChanged(s: Editable?) {
        val editText = editTextWeakReference.get()
        if (editText == null || editText.text.toString().isEmpty()) {
            return
        }
        editText.removeTextChangedListener(this)

        val parsed = parseCurrencyValue(s.toString(), formatter)
        val formatted = formatter.format(parsed)

        editText.setText(formatted)
        editText.setSelection(formatted.length)
        editText.addTextChangedListener(this)
    }

    companion object {
        fun parseCurrencyValue(value: String, formatter: DecimalFormat): BigDecimal {
            try {
                val replaceRegex = String.format("[%s,.\\s]", formatter.decimalFormatSymbols.groupingSeparator)
                var currencyValue = value.replace(replaceRegex.toRegex(), "")
                currencyValue = if (currencyValue.isEmpty()) "0" else currencyValue
                return BigDecimal(currencyValue)
            } catch (e: Exception) {
                Log.e("App", e.message, e)
            }
            return BigDecimal.ZERO
        }
    }
}

