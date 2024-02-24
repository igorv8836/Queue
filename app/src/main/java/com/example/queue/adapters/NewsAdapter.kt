package com.example.queue.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.queue.add_classes.NewsItem
import com.example.queue.databinding.NewsItemBinding
import java.text.SimpleDateFormat
import java.util.Locale

class NewsAdapter(var data: ArrayList<NewsItem>): RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    class NewsViewHolder(view: View): RecyclerView.ViewHolder(view){
        val binding: NewsItemBinding = NewsItemBinding.bind(view)
        val title = binding.title
        val text = binding.description
        val date = binding.date
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        return NewsViewHolder(
            NewsItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            ).root
        )
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.title.text = data[position].title
        holder.text.text = data[position].text
        holder.date.text =
            SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
                .format(data[position].date)
    }
}