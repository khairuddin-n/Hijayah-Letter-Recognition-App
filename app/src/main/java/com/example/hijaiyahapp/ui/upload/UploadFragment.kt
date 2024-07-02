package com.example.hijaiyahapp.ui.upload

import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.hijaiyahapp.MainViewModel
import com.example.hijaiyahapp.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UploadFragment : Fragment(R.layout.fragment_upload) {

    private val viewModel: MainViewModel by viewModels()

    private lateinit var imageView: ImageView
    private lateinit var uploadButton: Button
    private lateinit var clearButton: Button
    private lateinit var predictionText: TextView
    private lateinit var confidenceText: TextView

    private val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            val bitmap = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, uri)
            imageView.setImageBitmap(bitmap)
            viewModel.predict(bitmap, isUpload = true)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        imageView = view.findViewById(R.id.image_view)
        uploadButton = view.findViewById(R.id.btn_upload)
        clearButton = view.findViewById(R.id.btn_clear)
        predictionText = view.findViewById(R.id.tv_prediction)
        confidenceText = view.findViewById(R.id.tv_confidence)

        uploadButton.setOnClickListener {
            pickImage.launch("image/*")
        }

        clearButton.setOnClickListener {
            imageView.setImageDrawable(null)
            predictionText.text = ""
            confidenceText.text = ""
        }

        viewModel.prediction.observe(viewLifecycleOwner) { prediction ->
            predictionText.text = prediction
        }

        viewModel.confidence.observe(viewLifecycleOwner) { confidence ->
            confidenceText.text = String.format("Confidence: %.2f", confidence)
        }
    }
}