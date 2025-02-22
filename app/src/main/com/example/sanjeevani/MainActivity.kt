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

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnDirectory = findViewById<Button>(R.id.btnDirectory)
        val btnPrediction = findViewById<Button>(R.id.btnPrediction)

        btnDirectory.setOnClickListener {
            startActivity(Intent(this, DirectoryActivity::class.java))
        }

        btnPrediction.setOnClickListener {
            startActivity(Intent(this, PredictionActivity::class.java))
        }
    }
}