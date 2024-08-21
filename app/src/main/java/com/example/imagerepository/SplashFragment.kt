package com.example.imagerepository

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.example.imagerepository.databinding.FragmentSplashBinding

class SplashFragment : Fragment() {
    private var _binding: FragmentSplashBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSplashBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnRegister.setOnClickListener { Register(it) }
        binding.btnLogin.setOnClickListener { Login(it) }
    }

    override fun onDestroy() {
        super.onDestroy()
    }
    fun Register(view: View)
    {
        val action = SplashFragmentDirections.actionSplashFragmentToFeedFragment()
        Navigation.findNavController(view).navigate(action)
    }
    fun Login(view: View)
    {

    }

}