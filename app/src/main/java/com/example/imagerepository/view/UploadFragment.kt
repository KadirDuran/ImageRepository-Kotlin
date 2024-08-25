package com.example.imagerepository.view

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.imagerepository.databinding.FragmentUploadBinding
import com.google.android.material.snackbar.Snackbar
import android.Manifest
import androidx.navigation.Navigation
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.storage
import java.util.UUID

class UploadFragment : Fragment() {
    private var _binding: FragmentUploadBinding? = null
    private val binding get() = _binding!!

    private lateinit var auths: FirebaseAuth
    private lateinit var storage: FirebaseStorage
    private lateinit var db: FirebaseFirestore


    private  lateinit var permissionLauncher : ActivityResultLauncher<String>
    private  lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private var  selectImg: Uri?=null //URL
    private var  selectBitmap: Bitmap?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerLauncher()
        auths = Firebase.auth
        storage = Firebase.storage
        db = Firebase.firestore
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
            SelectImage(view)
        }

        binding.btnCommentSave.setOnClickListener{
            SaveInfo()
        }
    }
    override fun onDestroy() {
        super.onDestroy()
    }

    fun SelectImage(view: View)
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
        {
            if(ContextCompat.checkSelfPermission(requireContext(),Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED)
            {
                if(ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(),Manifest.permission.READ_MEDIA_IMAGES))
                {
                    Snackbar.make(view,"Görsel yüklemek için izin vermeniz gerekmektedir.",Snackbar.LENGTH_INDEFINITE).setAction(
                        "İzin Ver",View.OnClickListener {
                            permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
                        }
                    ).show()
                }else
                {
                    permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
                }
            }else
            {
                val intent = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityResultLauncher.launch(intent)
            }

        }else
        {
            if(ContextCompat.checkSelfPermission(requireContext(),Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            {
                if(ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(),Manifest.permission.READ_EXTERNAL_STORAGE))
                {
                    Snackbar.make(view,"Görsel yüklemek için izin vermeniz gerekmektedir.",Snackbar.LENGTH_INDEFINITE).setAction(
                        "İzin Ver",View.OnClickListener {
                            permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                        }
                    ).show()
                }else
                {
                    permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                }
            }else
            {
                val intent = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityResultLauncher.launch(intent)
            }
        }
    }

    fun SaveInfo() {
        if (auths.currentUser != null) {
            var storageRef = storage.reference

            var imgRef = storageRef.child("images").child("${UUID.randomUUID()}.jpg")
            if (selectImg != null) {
                imgRef.putFile(selectImg!!).addOnSuccessListener {
                    imgRef.downloadUrl.addOnCompleteListener() { uri ->
                        val info = hashMapOf(
                            "email" to auths.currentUser!!.email.toString(),
                            "imageUrl" to uri.result.toString(),
                            "date" to Timestamp.now().toDate(),
                            "comment" to binding.txtComment.text.toString()
                        )
                        db.collection("Post").add(info).addOnCompleteListener(){
                            task->
                            if(task.isSuccessful)
                            {
                                ShowMessage("Yükleme işlemi başarılı!")
                                val action = UploadFragmentDirections.actionUploadFragmentToFeedFragment()
                                Navigation.findNavController(requireView()).navigate(action)
                            }
                        }.addOnFailureListener {
                            exp->ShowMessage("Yükleme işlemi başarız oldu.")
                        }
                    }.addOnFailureListener { exp ->
                        ShowMessage(exp.localizedMessage.toString())
                    }
                }.addOnFailureListener { exeption ->
                    Toast.makeText(requireContext(),exeption.localizedMessage.toString(),Toast.LENGTH_LONG).show()
                    ShowMessage(exeption.localizedMessage.toString())
                }
            }
        }else{
            ShowMessage("Kullanıcı Girişi Yapınız!")
        }
    }
    fun registerLauncher()
    {
        activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult())
        {
            if(it.resultCode == AppCompatActivity.RESULT_OK)
            {
                val intentData =  it.data
                if(intentData != null)
                {
                    selectImg = intentData.data

                    try {
                        if(Build.VERSION.SDK_INT>=28)
                        {
                            val source = ImageDecoder.createSource(requireActivity().contentResolver,selectImg!!)
                            selectBitmap = ImageDecoder.decodeBitmap(source)
                            binding.img.setImageBitmap(selectBitmap)

                        }else
                        {
                            selectBitmap = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, selectImg)
                            binding.img.setImageBitmap(selectBitmap)
                        }
                    }catch (e : Exception)
                    {
                        println(e.localizedMessage)
                    }

                }
            }
        }
        permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission())
        {
            if(it)
            {
                val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityResultLauncher.launch(intent)
            }else
            {
                ShowMessage("İzin verilmedi.")
            }
        }
    }



    fun ShowMessage(message : String){
        Toast.makeText(requireContext(),message,Toast.LENGTH_LONG).show()
    }
}