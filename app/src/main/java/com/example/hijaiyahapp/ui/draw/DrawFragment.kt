package com.example.hijaiyahapp.ui.draw

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.hijaiyahapp.MainViewModel
import com.example.hijaiyahapp.R
import com.example.hijaiyahapp.util.HijaiyahRecognizer
import dagger.hilt.android.AndroidEntryPoint
import java.util.Random

@AndroidEntryPoint
class DrawFragment : Fragment(R.layout.fragment_draw) {

    private val viewModel: MainViewModel by viewModels()

    private lateinit var drawView: DrawView
    private lateinit var predictButton: Button
    private lateinit var clearButton: Button
    private lateinit var generateButton: Button
    private lateinit var questionText: TextView
    private lateinit var predictionText: TextView
    private val random = Random()

    private lateinit var labels: List<String>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        drawView = view.findViewById(R.id.draw_view)
        predictButton = view.findViewById(R.id.btn_predict)
        clearButton = view.findViewById(R.id.btn_clear)
        generateButton = view.findViewById(R.id.btn_generate)
        questionText = view.findViewById(R.id.tv_question)
        predictionText = view.findViewById(R.id.tv_prediction)

        labels = HijaiyahRecognizer.loadLabels(requireContext(), "generate.txt")

        generateButton.setOnClickListener {
            val randomLabel = labels[random.nextInt(labels.size)]
            questionText.text = randomLabel
            drawView.clear()
            drawView.setBorderColor(Color.BLACK)
            Log.d("DrawFragment", "Generated question: $randomLabel")
        }

        predictButton.setOnClickListener {
            viewModel.predict(drawView.getBitmap())
        }

        clearButton.setOnClickListener {
            drawView.clear()
            predictionText.text = ""
            drawView.setBorderColor(Color.BLACK)
        }

        viewModel.prediction.observe(viewLifecycleOwner) { prediction ->
            val question = questionText.text.toString()
            Log.d("DrawFragment", "Prediction: $prediction, Question: $question")

            if (prediction == question) {
                predictionText.text = getString(R.string.correct)
                drawView.setBorderColor(Color.GREEN)
            } else {
                predictionText.text = getString(R.string.incorrect)
                drawView.setBorderColor(Color.RED)
            }
        }
    }
}
