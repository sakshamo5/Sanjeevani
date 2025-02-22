package com.example.sanjeevani

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.graphics.Bitmap
import org.tensorflow.lite.Interpreter
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel
import android.content.res.AssetFileDescriptor
import android.app.Activity
import android.content.Context
import android.graphics.ImageDecoder
import android.net.Uri
import android.provider.MediaStore
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import android.widget.EditText
import android.widget.Toast

val leavesLabels = listOf(
    "Aloevera", "Amla", "Amruthaballi", "Arali", "Astma_weed", "Badipala", "Balloon_Vine", "Bamboo", "Beans", "Betel",
    "Bhrami", "Bringaraja", "Caricature", "Castor", "Catharanthus", "Chakte", "Chilly", "Citron lime (herelikai)", "Coffee",
    "Common rue(naagdalli)", "Coriender", "Curry", "Doddpathre", "Drumstick", "Ekka", "Eucalyptus", "Ganigale", "Ganike",
    "Gasagase", "Ginger", "Globe Amarnath", "Guava", "Henna", "Hibiscus", "Honge", "Insulin", "Jackfruit", "Jasmine",
    "Kambajala", "Kasambruga", "Kohlrabi", "Lantana", "Lemon", "Lemongrass", "Malabar_Nut", "Malabar_Spinach", "Mango",
    "Marigold", "Mint", "Neem", "Nelavembu", "Nerale", "Nooni", "Onion", "Padri", "Palak(Spinach)", "Papaya", "Parijatha",
    "Pea", "Pepper", "Pomoegranate", "Pumpkin", "Raddish", "Rose", "Sampige", "Sapota", "Seethaashoka", "Seethapala",
    "Spinach1", "Tamarind", "Taro", "Tecoma", "Thumbe", "Tomato", "Tulsi", "Turmeric", "ashoka", "camphor", "kamakasturi",
    "kepala"
)
class PredictionActivity : AppCompatActivity() {
    private lateinit var imageView: ImageView
    private lateinit var tfliteHelper: TFLiteHelper
    private lateinit var txtPredictionResult: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_prediction)

        imageView = findViewById(R.id.imageView)
        txtPredictionResult = findViewById(R.id.txtPredictionResult)
        val btnSelectImage = findViewById<Button>(R.id.btnSelectImage)
        tfliteHelper = TFLiteHelper(assets, "plant_leaf_model.tflite")

        btnSelectImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            imagePickerLauncher.launch(intent)
        }
    }

    private val imagePickerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val uri: Uri? = result.data?.data
            uri?.let {
                val bitmap = uriToBitmap(this, it)
                imageView.setImageBitmap(bitmap)
                val predictionIndex = tfliteHelper.predict(bitmap)
                val predictionLabel = if (predictionIndex in leavesLabels.indices) leavesLabels[predictionIndex] else "Unknown"
                txtPredictionResult.text = "Prediction: $predictionLabel"
            }
        }
    }

    private fun uriToBitmap(context: Context, uri: Uri): Bitmap {
        val source = ImageDecoder.createSource(context.contentResolver, uri)
        return ImageDecoder.decodeBitmap(source)
    }
}
