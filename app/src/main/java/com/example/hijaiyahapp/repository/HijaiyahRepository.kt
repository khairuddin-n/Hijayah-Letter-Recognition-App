package com.example.hijaiyahapp.repository

import android.graphics.Bitmap
import com.example.hijaiyahapp.util.HijaiyahRecognizer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class HijaiyahRepository @Inject constructor(
    private val hijaiyahRecognizer: HijaiyahRecognizer
) {
    suspend fun predictHijaiyah(bitmap: Bitmap): Pair<String, Float> {
        return withContext(Dispatchers.IO) {
            hijaiyahRecognizer.predict(bitmap)
        }
    }
}