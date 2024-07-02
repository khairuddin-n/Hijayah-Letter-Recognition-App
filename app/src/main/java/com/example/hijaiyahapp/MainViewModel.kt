package com.example.hijaiyahapp

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hijaiyahapp.repository.HijaiyahRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: HijaiyahRepository
) : ViewModel() {
    private val _prediction = MutableLiveData<String>()
    val prediction: LiveData<String> = _prediction

    private val _confidence = MutableLiveData<Float>()
    val confidence: LiveData<Float> = _confidence

    fun predict(bitmap: Bitmap, isUpload: Boolean = false) {
        viewModelScope.launch {
            val (label, conf) = repository.predictHijaiyah(bitmap)
            if (isUpload && conf < CONFIDENCE_THRESHOLD) {
                _prediction.value = "Gambar tidak dikenali"
                _confidence.value = conf
            } else {
                _prediction.value = label
                _confidence.value = conf
            }
        }
    }

    companion object {
        private const val CONFIDENCE_THRESHOLD = 0.7f
    }
}