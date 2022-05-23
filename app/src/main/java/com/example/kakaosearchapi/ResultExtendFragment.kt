package com.example.kakaosearchapi

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.kakaosearchapi.databinding.FragmentResultExtendBinding

class ResultExtendFragment : Fragment() {

    private var _binding: FragmentResultExtendBinding? = null
    private val binding get() = _binding!!

    private val sharedViewModel: SearchViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val transitionInflater = TransitionInflater.from(requireContext())
            .inflateTransition(android.R.transition.move)
        sharedElementEnterTransition = transitionInflater
        sharedElementReturnTransition = transitionInflater
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentResultExtendBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.imageView.apply {
            setImageDrawable(sharedViewModel.drawable)
        }

        binding.imageView.transitionName = sharedViewModel.items.imageUrl
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}