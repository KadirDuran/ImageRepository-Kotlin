package com.example.imagerepository

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.imagerepository.databinding.FragmentFeedBinding
import com.example.imagerepository.databinding.FragmentUploadBinding


class UploadFragment : Fragment() {
    private var _binding: FragmentUploadBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUploadBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.img.setOnClickListener {
            SelectImage()
        }

        binding.btnCommentSave.setOnClickListener{
            SaveInfo()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    fun SelectImage()
    {

    }

    fun SaveInfo()
    {

    }
}