package com.example.queue.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.queue.add_classes.NewsItem
import com.example.queue.databinding.NewsItemBinding
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.math.min

class NewsAdapter(private var data: ArrayList<NewsItem>):
    RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    class NewsViewHolder(view: View): RecyclerView.ViewHolder(view){
        private val binding: NewsItemBinding = NewsItemBinding.bind(view)
        var detailedShow = false
        val main = binding.main
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

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.title.text = data[position].title
        if (data[position].text.length > 100)
            holder.text.text = data[position].text.substring(0, 100) + "..."
        else
            holder.text.text = data[position].text
        holder.date.text = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
            .format(data[position].date)

        holder.main.setOnClickListener {
            holder.detailedShow = !holder.detailedShow
            if (holder.detailedShow){
                holder.text.text = data[position].text
            } else {
                if (data[position].text.length > 100)
                    holder.text.text = data[position].text.substring(0, 100) + "..."
                else
                    holder.text.text = data[position].text
            }
        }
    }
}