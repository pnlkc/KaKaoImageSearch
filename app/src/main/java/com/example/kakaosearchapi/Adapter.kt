package com.example.kakaosearchapi

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import com.example.kakaosearchapi.databinding.RecyclerviewItemBinding
import kotlinx.coroutines.*

interface OnItemClickListener {
    fun onItemClicked(position: Int, imageView: ImageView, drawable: Drawable)
}


class MyViewHolder(val binding: RecyclerviewItemBinding) :
    RecyclerView.ViewHolder(binding.root)


class MyAdapter(
    private val context: Context,
    private val resultList: List<Items>,
    myInterface: OnItemClickListener,
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var myClickInterface = myInterface

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(RecyclerviewItemBinding.inflate(LayoutInflater.from(parent.context),
            parent,
            false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding = (holder as MyViewHolder).binding
        val items = resultList[position]
        Glide.with(context)
//            .load(items.thumbnailUrl)
            .load(items.imageUrl)
            // 오리지널 사이즈 가져오기
            .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
            .dontTransform()
            .into(binding.imageView)
        binding.imageView.transitionName = items.imageUrl

        // Glide로 URL에서 Drawable 객체 가져오기
//        var drawable: Drawable? = null
//        Glide.with(context)
//            .asDrawable()
//            .load(items.imageUrl)
//            .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
//            .into(object : CustomTarget<Drawable>() {
//                override fun onResourceReady(
//                    resource: Drawable,
//                    transition: Transition<in Drawable>?,
//                ) {
//                    drawable = resource
//                }
//
//                override fun onLoadCleared(placeholder: Drawable?) {
//                    Log.d("로그", "MyAdapter - onLoadCleared() 호출됨")
//                }
//
//            })

            binding.imageView.setOnClickListener {
                val drawable = binding.imageView.drawable
                val imageView = binding.imageView
                myClickInterface.onItemClicked(position, imageView, drawable!!)
            }
    }

    override fun getItemCount(): Int {
        return resultList.size
    }

}

