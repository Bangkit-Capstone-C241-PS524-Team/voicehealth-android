package com.aloysius.dicoding.view.customview

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import com.aloysius.voicehealthguide.R
import com.google.android.material.textfield.TextInputLayout

class PasswordEditText : AppCompatEditText {
    private lateinit var context: Context

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context)
    }

    private fun init(context: Context) {
        this.context = context
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val parent = parent.parent
                if (parent is TextInputLayout) {
                    validatePassword(s.toString(), parent)
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun validatePassword(password: String, parent: TextInputLayout) {
        if (password.isEmpty()) {
            parent.error = context.getString(R.string.password_required)
            parent.isErrorEnabled = true
        } else if (password.length < 8) {
            parent.error = context.getString(R.string.invalid_password_error)
            parent.isErrorEnabled = true
        } else {
            parent.error = null
            parent.isErrorEnabled = false
        }
    }
}