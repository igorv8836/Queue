package com.example.queue.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.queue.R
import com.example.queue.adapters.NewsAdapter
import com.example.queue.databinding.FragmentAuthBinding
import com.example.queue.databinding.FragmentNewsBinding
import com.example.queue.viewmodels.NewsViewModel
import java.util.logging.LogManager

class NewsFragment : Fragment() {
    lateinit var binding: FragmentNewsBinding
    lateinit var viewModel: NewsViewModel
    lateinit var adapter: NewsAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[NewsViewModel::class.java]

        viewModel.loadNews()

        viewModel.newsData.observe(viewLifecycleOwner){
            adapter = NewsAdapter(it)
            binding.newsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            binding.newsRecyclerView.adapter = adapter
        }
    }
}