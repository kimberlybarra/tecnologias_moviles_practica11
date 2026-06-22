package com.kimberly.smartreply
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.kimberly.smartreply.databinding.ActivityMainBinding
import com.google.mlkit.nl.smartreply.SmartReply
import com.google.mlkit.nl.smartreply.SmartReplySuggestionResult
import com.google.mlkit.nl.smartreply.TextMessage

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var conversation = ArrayList<TextMessage>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.sendButton.setOnClickListener {
            addMessage(binding.messageText.text.toString())
        }

        binding.hintsButton.setOnClickListener {
            getHints()
        }

        binding.clearButton.setOnClickListener {
            conversation.clear()

            binding.hint0Button.visibility = View.GONE
            binding.hint1Button.visibility = View.GONE
            binding.hint2Button.visibility = View.GONE

            binding.nameText.setText("")
            binding.messageText.setText("")
            binding.errorText.text = ""
        }

        binding.hint0Button.setOnClickListener {
            addMessage(binding.hint0Button.text.toString())
        }

        binding.hint1Button.setOnClickListener {
            addMessage(binding.hint1Button.text.toString())
        }

        binding.hint2Button.setOnClickListener {
            addMessage(binding.hint2Button.text.toString())
        }
    }

    private fun addMessage(text: String) {

        if (text.isNotEmpty()) {

            conversation.add(
                TextMessage.createForRemoteUser(
                    text,
                    System.currentTimeMillis(),
                    binding.nameText.text.toString()
                )
            )

            binding.messageText.setText("")
        }
    }

    private fun getHints() {

        val smartReply = SmartReply.getClient()

        smartReply.suggestReplies(conversation)
            .addOnSuccessListener { result ->

                if (result.status ==
                    SmartReplySuggestionResult.STATUS_NOT_SUPPORTED_LANGUAGE
                ) {

                    Toast.makeText(
                        applicationContext,
                        "Idioma no soportado",
                        Toast.LENGTH_SHORT
                    ).show()

                } else if (result.status ==
                    SmartReplySuggestionResult.STATUS_SUCCESS
                ) {

                    if (result.suggestions.size >= 3) {

                        binding.hint0Button.text =
                            result.suggestions[0].text

                        binding.hint1Button.text =
                            result.suggestions[1].text

                        binding.hint2Button.text =
                            result.suggestions[2].text

                        binding.hint0Button.visibility = View.VISIBLE
                        binding.hint1Button.visibility = View.VISIBLE
                        binding.hint2Button.visibility = View.VISIBLE
                    }
                }
            }
            .addOnFailureListener {

                binding.errorText.text = it.message
            }
    }
}