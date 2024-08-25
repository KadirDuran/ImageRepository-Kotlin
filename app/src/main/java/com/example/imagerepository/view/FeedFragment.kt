package com.example.imagerepository.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.imagerepository.model.Post
import com.example.imagerepository.R
import com.example.imagerepository.adapter.PostAdapter
import com.example.imagerepository.databinding.FragmentFeedBinding
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore

class FeedFragment : Fragment(),PopupMenu.OnMenuItemClickListener {
    private var _binding: FragmentFeedBinding? = null
    private val binding get() = _binding!!
    private lateinit var auths: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    val posts : ArrayList<Post> = arrayListOf()
    private  var adapter : PostAdapter?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auths = Firebase.auth
        db = Firebase.firestore

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFeedBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.floatingActionButton.setOnClickListener{
            ShowMenu(it)
        }

        GetData()

        adapter = PostAdapter(posts)
        binding.rcFeed.layoutManager = LinearLayoutManager(requireContext())
        binding.rcFeed.adapter =adapter


    }

    override fun onDestroy() {
        super.onDestroy()
    }

    fun ShowMenu(view: View)
    {
        val popupMenu = PopupMenu(requireContext(),binding.floatingActionButton)
        val inflater = popupMenu.menuInflater
        inflater.inflate(R.menu.popup_menu,popupMenu.menu)
        popupMenu.setOnMenuItemClickListener(this)
        popupMenu.show()

    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {

        //newpost ve exit degerleri popup_menu'deki item Id'leri
       if(item?.itemId == R.id.newpost){
           val action= FeedFragmentDirections.actionFeedFragmentToUploadFragment()
           Navigation.findNavController(requireView()).navigate(action)

       }
       if(item?.itemId == R.id.exit)
       {
            LogOut(requireView())
       }
        return  true
    }
    fun LogOut(view: View)
    {
        auths.signOut()
        val action = FeedFragmentDirections.actionFeedFragmentToSplashFragment()
        Navigation.findNavController(view).navigate(action)
    }

    fun GetData(){
        val documents = db.collection("Post").orderBy("date",Query.Direction.DESCENDING)
        documents.addSnapshotListener { value, error ->
            if(error!=null){
                Toast.makeText(requireContext(),"Veriler çekilirken hata oluştu.",Toast.LENGTH_LONG).show()
            }else {
                if (value != null && !value.isEmpty) {
                    posts.clear()
                    val documents = value.documents
                    for (document in documents)
                    {
                        val post = Post(document.get("email").toString(),document.get("imageUrl").toString(),document.get("comment").toString(),document.get("date").toString())
                        posts.add(post)
                    }
                    adapter?.notifyDataSetChanged()
                }
            }
        }
    }
}