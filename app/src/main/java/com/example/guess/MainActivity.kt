package com.example.guess

import android.content.Context

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    lateinit var showPhrase: TextView
    lateinit var submit: Button
    lateinit var textEnter: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPreferences = this.getSharedPreferences(
            getString(R.string.preference_file_key), Context.MODE_PRIVATE

        )
        var myMessage = sharedPreferences.getString("myMessage", "").toString()
        // --> retrieves data from Shared Preferences
        // We can save data with the following code
        with(sharedPreferences.edit()) {
            putString("myMessage", myMessage)
            apply()
        }

        //UI elements
        val showLetter = findViewById<TextView>(R.id.letter)
        showPhrase = findViewById(R.id.phrase)
        submit = findViewById(R.id.submit) // submit button
        textEnter = findViewById(R.id.textEnter) // user input
        val myRV = findViewById<RecyclerView>(R.id.rvMain)
        val myLayout = findViewById<ConstraintLayout>(R.id.screen_layout)


        // pre-game
        var remaining = 10
        val results = mutableListOf<String>()
        val phrases = listOf<String>(
            "Hello coding dojo",
            "Bye coding dojo"

        )

        val selectedPhrase = phrases[Random.nextInt(phrases.size)]
        var starPhrase = Regex("[A-Za-z]").replace(selectedPhrase, "*")
        showPhrase.text = starPhrase
        var enterPhrase = true

        myRV.adapter = RecyclerViewAdapter(results)
        myRV.layoutManager = LinearLayoutManager(this)

        submit.setOnClickListener {
            var input = textEnter.text.toString().trim().uppercase()
            if (showPhrase.text.contains("*")) {
                if (input.isEmpty()) {
                    Snackbar.make(
                        myLayout,
                        "You must enter at least one letter",
                        Snackbar.LENGTH_SHORT
                    ).show()
                } else {
                    if (enterPhrase) {
                        if (input == selectedPhrase) {
                            results.add("Greet job")
                            showPhrase.text = selectedPhrase
                            showPhrase.textSize = 18f
                            submit.isClickable = false
                        } else {
                            results.add("Wrong guess: $input")
                        }
                        textEnter.setText("")
                        textEnter.hint = "Guess a letter"
                        enterPhrase = false
                    } else {
                        showLetter.text = input[0].toString()
                        if (selectedPhrase.contains(input[0])) {
                            var counter = 0
                            var phraseChar = showPhrase.text.toString().toCharArray()
                            for (i in 0..selectedPhrase.length - 1) {
                                if (selectedPhrase[i] == input[0]) {
                                    phraseChar[i] = input[0]
                                    counter++
                                }
                            }
                            showPhrase.text = String(phraseChar)
                            results.add("Found $counter $input(s)")
                        } else {
                            results.add("Wrong guess: $input")
                            results.add("${--remaining} guesses remaining")
                        }
                        textEnter.setText("")
                        textEnter.hint = "Guess the full phrase"
                        enterPhrase = true
                    }
                }
            } else {
                results.add("Greet job")
                showPhrase.text = selectedPhrase
                showPhrase.textSize = 18f
                submit.isClickable = false
            }

            myRV.adapter = RecyclerViewAdapter(results)
            myRV.layoutManager = LinearLayoutManager(this)
            myRV.scrollToPosition(results.size - 1)

            if (remaining == 0) {
                results.add("Game over")
                submit.isClickable = false
            }
        }


    }

}