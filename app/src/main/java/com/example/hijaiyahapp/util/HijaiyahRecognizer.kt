package com.example.hijaiyahapp.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.common.FileUtil
import java.nio.ByteBuffer
import java.nio.ByteOrder

class HijaiyahRecognizer(context: Context) {
    private val interpreter: Interpreter
    private val labels: List<String>

    init {
        val model = FileUtil.loadMappedFile(context, "model.tflite")
        interpreter = Interpreter(model)
        labels = loadLabels(context, "labels.txt")
    }

    fun predict(bitmap: Bitmap): Pair<String, Float> {
        val scaledBitmap = Bitmap.createScaledBitmap(bitmap, 32, 32, true)
        val byteBuffer = convertBitmapToByteBuffer(scaledBitmap)

        val result = Array(1) { FloatArray(labels.size) }
        interpreter.run(byteBuffer, result)

        val maxIndex = result[0].indices.maxByOrNull { result[0][it] } ?: -1
        val maxConfidence = if (maxIndex != -1) result[0][maxIndex] else 0f

        return if (maxIndex != -1) Pair(labels[maxIndex], maxConfidence) else Pair("Unknown", 0f)
    }

    private fun convertBitmapToByteBuffer(bitmap: Bitmap): ByteBuffer {
        val byteBuffer = ByteBuffer.allocateDirect(4 * 32 * 32)
        byteBuffer.order(ByteOrder.nativeOrder())
        for (y in 0 until 32) {
            for (x in 0 until 32) {
                val pixel = bitmap.getPixel(x, y)
                val value = Color.red(pixel).toFloat()
                byteBuffer.putFloat(value)
            }
        }
        return byteBuffer
    }

    companion object {
        fun loadLabels(context: Context, fileName: String): List<String> {
            return FileUtil.loadLabels(context, fileName)
        }
    }
}