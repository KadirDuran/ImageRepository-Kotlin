package com.example.imagerepository.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.imagerepository.databinding.DatarowBinding
import com.example.imagerepository.databinding.FragmentSplashBinding
import com.example.imagerepository.model.Post
import com.google.firebase.Firebase
import com.squareup.picasso.Picasso


class PostAdapter(val postList : List<Post>) : RecyclerView.Adapter<PostAdapter.PostHolder>() {

    class PostHolder(val binding : DatarowBinding) : RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostHolder {
        val rcRowBinding  = DatarowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return  PostHolder(rcRowBinding)
    }

    override fun getItemCount(): Int {
       return  postList.size
    }

    override fun onBindViewHolder(holder: PostHolder, position: Int) {
        Picasso.get().load(postList[position].imageUrl).into(holder.binding.img)
        holder.binding.txtComment.text =   postList[position].comment
        holder.binding.lblEmail.text =postList[position].email
        holder.binding.date.text = postList[position].date
    }
}