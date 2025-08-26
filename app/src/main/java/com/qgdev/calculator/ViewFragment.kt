package com.qgdev.calculator

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.qgdev.calculator.databinding.FragmentViewBinding


class ViewFragment : Fragment() {

    private var _binding: FragmentViewBinding? = null
    private val binding get() = _binding!!   // safe accessor


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentViewBinding.inflate(inflater, container, false)
        return binding.root
    }


    fun showResult(result: String) {
        binding.inputField.setText(result)
    }

    fun updateTextView(text: String){

        binding.textView.setText(text)
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null  // avoid memory leaks
    }

}