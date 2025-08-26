package com.qgdev.calculator

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.qgdev.calculator.databinding.ActivityMainBinding
import androidx.core.content.edit

class MainActivity : AppCompatActivity() {

    lateinit var mainBinding: ActivityMainBinding
    private lateinit var viewFragment: ViewFragment

    private val calculator = CalculatorEngine()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding= ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(mainBinding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        viewFragment = ViewFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.frameLayout, viewFragment) // your FrameLayout ID
            .commit()
    }


    fun onButtonClick(view: View) {
        val tag = (view as Button).tag.toString()
        val output = calculator.input(tag)  // returns String

        if (tag == "=") {
            // Format for TextView: remove .0 if integer
            val formatted = output.toDoubleOrNull()?.let {
                if (it % 1.0 == 0.0) it.toInt().toString() else it.toString()
            } ?: output
            viewFragment.updateTextView(formatted)  // bottom TextView
        } else {
            // For numbers/operators, show exactly what engine gives in EditText
            viewFragment.showResult(output)         // top EditText
        }
    }

    override fun onPause() {
        super.onPause()

        val prefs = getSharedPreferences("calc_prefs", MODE_PRIVATE)
        prefs.edit {
            putString("expression", calculator.toString()) // use engine's toString()
        }
    }


    override fun onResume() {
        super.onResume()
        val prefs = getSharedPreferences("calc_prefs", MODE_PRIVATE)
        val expression = prefs.getString("expression", "")

        if (!expression.isNullOrEmpty()) {
            // Clear engine and load saved expression
            calculator.clear()
            calculator.input(expression)  // you can loop over each char if needed
            viewFragment.showResult(expression) // update EditText
        }
    }



}






