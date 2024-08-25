package com.example.imagerepository.view

import android.os.Bundle

import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.example.imagerepository.databinding.FragmentSplashBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth

class SplashFragment : Fragment() {
    private var _binding: FragmentSplashBinding? = null
    private val binding get() = _binding!!
    private lateinit var auths: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auths = Firebase.auth

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
        UserLoginCheck(view)
    }

    override fun onDestroy() {
        super.onDestroy()
    }
    fun Register(view: View)
    {
        if(binding.txtEmail.text.isNotEmpty() && binding.txtPassword.text.isNotEmpty() )
        {
            UserAdd(binding.txtEmail.text.toString(), binding.txtPassword.text.toString())
        }
    }
        fun Login(view: View)
    {
        if(binding.txtEmail.text.isNotEmpty() && binding.txtPassword.text.isNotEmpty() )
        {
            auths.signInWithEmailAndPassword(binding.txtEmail.text.toString(),binding.txtPassword.text.toString())
                .addOnCompleteListener(){
                        task ->
                    if (task.isSuccessful)
                    {
                        val action = SplashFragmentDirections.actionSplashFragmentToFeedFragment()
                        Navigation.findNavController(view).navigate(action)
                    }
                }.addOnFailureListener { exp ->
                    Toast.makeText(requireContext(),exp.localizedMessage,Toast.LENGTH_LONG).show()
                }

        }
    }


    fun UserLoginCheck(view: View)
    {
        val currentUser = auths.currentUser
        if (currentUser != null) {
            Login(view)
        }
    }
    private fun UserAdd(email : String, password : String){
        auths.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(requireContext(),"Kayıt işleminiz başarılı",Toast.LENGTH_LONG).show()
                    val user = auths.currentUser
                    UpdateUI(user)
                }
            }.addOnFailureListener { exeption ->
                Toast.makeText(requireContext(),exeption.localizedMessage,Toast.LENGTH_LONG).show()
            }
    }

    fun UpdateUI(user : FirebaseUser?)
    {
        Toast.makeText(requireContext(),"Hoş Geldiniz "+ user?.displayName,Toast.LENGTH_LONG).show()
    }

}