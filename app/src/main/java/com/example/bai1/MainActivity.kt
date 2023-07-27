package com.example.bai1

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import com.example.bai1.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity()
{
    //binding
    private lateinit var binding : ActivityMainBinding

    //other
    private var firstnumber = ""
    private var currentNumber = ""
    private var currentOperator = ""
    private var result = ""
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState : Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //NoLimitScreen
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        //initViews

        binding.apply {
            // get all buttons
            binding.layoutMain.children.filterIsInstance<Button>().forEach { button ->
                //buttons click listener
                button.setOnClickListener {
                    //get clicked button text
                    val buttonText = button.text.toString()
                    when{
                        buttonText.matches(Regex("[0-9]"))->{
                            if(currentOperator.isEmpty() && firstnumber.length < 9)
                            {
                                firstnumber+=buttonText
                                tvResult.text = firstnumber
                            }else if (currentOperator.isNotEmpty() && currentNumber.length < 9)
                            {
                                currentNumber+=buttonText
                                tvResult.text = currentNumber
                            }
                        }
                        buttonText.matches(Regex("[+\\-x/]"))->{
                            currentNumber = ""
                            if (tvResult.text.toString().isNotEmpty())
                            {
                                currentOperator = buttonText
                                tvResult.text = "0"
                            }
                        }
                        buttonText == "="->{
                            if (currentNumber.isNotEmpty()&& currentOperator.isNotEmpty())
                            {
                                tvFormula.text = "$firstnumber$currentOperator$currentNumber"
                                result = evaluateExpression(firstnumber,currentNumber,currentOperator)
                                firstnumber = result
                                tvResult.text = result
                            }
                        }
                        buttonText == "."->{
                            if(currentOperator.isEmpty())
                            {
                                if (! firstnumber.contains("."))
                                {
                                    if(firstnumber.isEmpty())firstnumber+="0$buttonText"
                                    else firstnumber +=buttonText
                                    tvResult.text = firstnumber
                                }
                            }else
                            {
                                if (! currentNumber.contains("."))
                                {
                                    if(currentNumber.isEmpty()) currentNumber+="0$buttonText"
                                    else currentNumber +=buttonText
                                    tvResult.text = currentNumber
                                }
                            }
                        }
                        buttonText == "C"->{
                            currentNumber = ""
                            firstnumber = ""
                            currentOperator = ""
                            tvResult.text = "0"
                            tvFormula.text = ""
                        }
                    }
                }
            }


        }
    }

    private fun evaluateExpression(firstNumber: String, secondNumber: String, operator: String): String {
        val num1 = firstNumber.toDouble()
        val num2 = secondNumber.toDouble()

        // Check if the second number is zero when performing division
        if (operator == "/" && num2 == 0.0) {
            return "Null"
        }

        // Perform the arithmetic operation
        val result = when (operator) {
            "+" -> num1 + num2
            "-" -> num1 - num2
            "x" -> num1 * num2
            "/" -> num1 / num2
            else -> return "Null"
        }

        // Check if rounding is necessary
        val roundedResult = if (result % 1 == 0.0) {
            result.toInt().toString()
        } else {
            String.format("%.2f", result)
        }

        // Check if the result exceeds 9 digits
        if (roundedResult.length > 9) {
            return "Null"
        }

        return roundedResult
    }

}


