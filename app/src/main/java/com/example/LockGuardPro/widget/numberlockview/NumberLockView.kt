package com.example.LockGuardPro.widget.numberlockview

import android.content.ContentValues.TAG
import android.content.Context
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.GridLayout
import com.thn.LockGuardPro.R


class NumberLockView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : GridLayout(context, attrs, defStyleAttr) {
    private var listener: NumberLockListener? = null
    private var pass: String = ""

    init {
        rowCount = 4
        columnCount = 3
        createButtons()
    }

    private fun createButtons() {
        for (row in 0 until rowCount) {
            for (col in 0 until columnCount) {
                val button = Button(context)
                button.layoutParams = LayoutParams().apply {
                    rowSpec = spec(row, 1f)
                    columnSpec = spec(col, 1f)
                    width = LayoutParams.WRAP_CONTENT
                    height = LayoutParams.WRAP_CONTENT
                    height = ViewGroup.LayoutParams.WRAP_CONTENT
                    setMargins(
                        8.dpToPx(context),
                        8.dpToPx(context),
                        8.dpToPx(context),
                        8.dpToPx(context)
                    )
                }
                configureButton(button, row, col)
                addView(button)
            }
        }
    }

    private fun configureButton(button: Button, row: Int, col: Int) {
        when (val value = row * columnCount + col + 1) {
            10 -> {
                button.visibility = View.GONE
            }

            11 -> {
                button.text = "0"
            }

            12 -> {
                button.text = "X"
                button.setTextColor(context.getColor(R.color.red))
                button.textSize = 6.dpToPx(context).toFloat()
            }

            else -> {
                button.text = value.toString()
            }
        }
        button.setBackgroundResource(R.drawable.bg_button_number)
        button.setOnClickListener {
            handleButtonClick(button)
        }
    }

    private fun handleButtonClick(button: Button) {
        listener?.let {
            if (button.text == "X") {
                pass = removeLastChar(pass)
                it.onXButtonClick()

            } else {
                if (pass.length < 4) {
                    pass += button.text
                    it.onNumberButtonClick()
                    if (pass.length == 4) {
                        it.onComple()
                    }
                }
            }
            Log.d(TAG, "handleButtonClick:$pass ")
        }
    }

    fun removeLastChar(str: String): String {
        if (str.isNotEmpty()) {
            return str.substring(0, str.length - 1)
        }
        return str
    }

    fun setListener(listener: NumberLockListener) {
        this.listener = listener
    }

    fun getPass() = pass
    fun clearPass() {
        pass = ""
        listener?.let {
            it.onXButtonClick()
            invalidate()
        }
    }
}

fun Int.dpToPx(context: Context): Int {
    val displayMetrics: DisplayMetrics = context.resources.displayMetrics
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), displayMetrics)
        .toInt();
}
