package com.kimberly.barcodescanner
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import com.kimberly.barcodescanner.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val seleccionarImagen =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->

            uri?.let {

                binding.imageView.setImageURI(it)

                val image = InputImage.fromFilePath(this, it)

                val scanner = BarcodeScanning.getClient()

                scanner.process(image)
                    .addOnSuccessListener { barcodes ->

                        if (barcodes.isEmpty()) {
                            binding.txtResultado.text =
                                "No se detectó ningún código."
                        } else {

                            val resultado = StringBuilder()

                            for (barcode in barcodes) {
                                resultado.append(
                                    barcode.rawValue
                                ).append("\n")
                            }

                            binding.txtResultado.text =
                                resultado.toString()
                        }
                    }
                    .addOnFailureListener {
                        binding.txtResultado.text =
                            "Error: ${it.message}"
                    }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.btnSeleccionar.setOnClickListener {
            seleccionarImagen.launch("image/*")
        }
    }
}