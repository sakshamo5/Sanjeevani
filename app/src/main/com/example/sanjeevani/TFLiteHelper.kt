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


class TFLiteHelper(private val assetManager: android.content.res.AssetManager, modelPath: String) {
    private var interpreter: Interpreter

    init {
        interpreter = Interpreter(loadModelFile(modelPath))
    }

    private fun loadModelFile(modelPath: String): MappedByteBuffer {
        val fileDescriptor: AssetFileDescriptor = assetManager.openFd(modelPath)
        val inputStream = fileDescriptor.createInputStream()
        val fileChannel = inputStream.channel
        val startOffset = fileDescriptor.startOffset
        val declaredLength = fileDescriptor.declaredLength
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
    }

    fun predict(bitmap: Bitmap): Int {
        val inputBuffer = convertBitmapToByteBuffer(bitmap)
        val output = Array(1) { FloatArray(leavesLabels.size) }
        interpreter.run(inputBuffer, output)
        return output[0].indices.maxByOrNull { output[0][it] } ?: -1
    }

    private fun convertBitmapToByteBuffer(bitmap: Bitmap): ByteBuffer {
        val inputSize = 224
        val byteBuffer = ByteBuffer.allocateDirect(4 * inputSize * inputSize * 3)
        byteBuffer.order(ByteOrder.nativeOrder())
        val scaledBitmap = Bitmap.createScaledBitmap(bitmap, inputSize, inputSize, true)
        val intValues = IntArray(inputSize * inputSize)
        scaledBitmap.getPixels(intValues, 0, inputSize, 0, 0, inputSize, inputSize)
        for (pixelValue in intValues) {
            byteBuffer.putFloat(((pixelValue shr 16 and 0xFF) / 255.0f))
            byteBuffer.putFloat(((pixelValue shr 8 and 0xFF) / 255.0f))
            byteBuffer.putFloat(((pixelValue and 0xFF) / 255.0f))
        }
        return byteBuffer
    }
}