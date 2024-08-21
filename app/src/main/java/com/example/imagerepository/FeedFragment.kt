package com.example.imagerepository

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.navigation.Navigation
import com.example.imagerepository.databinding.FragmentFeedBinding
import com.example.imagerepository.databinding.FragmentSplashBinding

class FeedFragment : Fragment(),PopupMenu.OnMenuItemClickListener {
    private var _binding: FragmentFeedBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
       if(item?.itemId ==R.id.newpost){
           val action= FeedFragmentDirections.actionFeedFragmentToUploadFragment()
           Navigation.findNavController(requireView()).navigate(action)

       }
       if(item?.itemId ==R.id.exit)
       {
       }
        return  true
    }


}