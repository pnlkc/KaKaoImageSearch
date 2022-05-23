package com.example.kakaosearchapi

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.kakaosearchapi.databinding.FragmentSearchResultBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchResultFragment : Fragment(), OnItemClickListener {

    private var _binding: FragmentSearchResultBinding? = null
    private val binding get() = _binding!!

    private val sharedViewModel: SearchViewModel by activityViewModels()

    private lateinit var items: List<Items>

    private lateinit var myAdapter: MyAdapter

    // OnBackPressedCallback(뒤로가기 기능) 객체 선언
    private lateinit var callback: OnBackPressedCallback

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentSearchResultBinding.inflate(inflater, container, false)
        postponeEnterTransition()
        callback = object : OnBackPressedCallback(true) {
            // 뒤로가기 했을 때 실행되는 기능
            override fun handleOnBackPressed() {
                if (sharedViewModel.searchTerm.isNotEmpty()) {
                    binding.bgImageView.visibility = View.VISIBLE
                    binding.recyclerView.visibility = View.INVISIBLE
                    binding.editText.text = null
                    sharedViewModel.searchTerm = ""
                    binding.linearLayout.setBackgroundResource(R.color.kakao_yellow)
                } else {
                    activity?.finish()
                }
            }
        }
        // 액티비티의 BackPressedDispatcher에 여기서 만든 callback 객체를 등록
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 검색창 엔터키 입력시
        binding.editText.setOnKeyListener { _, keyCode, _ ->
            when (keyCode) {
                KeyEvent.KEYCODE_ENTER -> {
                    sharedViewModel.searchTerm = binding.editText.text.toString()
                    sharedViewModel.page.value = 1
                    searchImage(sharedViewModel.searchTerm, sharedViewModel.page.value!!)
                    searchAction()
                    true
                }
                else -> false
            }
        }

        if (sharedViewModel.searchTerm.isNotEmpty()) {
            Log.d("로그", "sharedViewModel.searchTerm : ${sharedViewModel.searchTerm}")
            binding.editText.doOnPreDraw {
                binding.editText.setText(sharedViewModel.searchTerm)
            }
            searchImage(sharedViewModel.searchTerm, sharedViewModel.page.value!!)
            binding.linearLayout.setBackgroundResource(R.color.white)
            binding.bgImageView.visibility = View.INVISIBLE
        } else {
            binding.bgImageView.visibility = View.VISIBLE
        }

        binding.searchBtn.setOnClickListener {
            sharedViewModel.page.value = 1
            sharedViewModel.searchTerm = binding.editText.text.toString()
            searchImage(sharedViewModel.searchTerm, sharedViewModel.page.value!!)

            searchAction()
        }


        binding.nextBtn.setOnClickListener {
            val inputText = binding.editText.text.toString()
            sharedViewModel.page.value = sharedViewModel.page.value!! + 1

            if (inputText == sharedViewModel.searchTerm) {
                searchImage(inputText, sharedViewModel.page.value!!)
            } else {
                sharedViewModel.searchTerm = inputText
                searchImage(inputText)
            }
        }
    }

    // 키보드 내리고 EditText에서 포커스 클리어
    private fun searchAction() {
        val imm =
            context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.editText.windowToken, 0)
        binding.editText.clearFocus()
    }


    // 카카오API에서 이미지 검색해주는 기능
    private fun searchImage(query: String, page: Int = 1) {
        val CLIENT_ID = "KakaoAK 29e3929b4619a8fd66f6af46f0f793af"

        val retrofit = Retrofit.Builder()
            .baseUrl("https://dapi.kakao.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val api = retrofit.create(KaKaoSearchAPI::class.java)
        val callGetSearchImage = api.getSearchImage(CLIENT_ID, query, page)

        callGetSearchImage.enqueue(object : Callback<SearchResult> {
            override fun onResponse(call: Call<SearchResult>, response: Response<SearchResult>) {
                if (response.isSuccessful) {
                    binding.recyclerView.layoutManager =
                        GridLayoutManager(context, 3)
                    myAdapter = MyAdapter(requireContext(),
                        response.body()!!.documents,
                        this@SearchResultFragment
                    )
                    binding.recyclerView.adapter = myAdapter

                    binding.recyclerView.doOnPreDraw {
                        startPostponedEnterTransition()
                        binding.bgImageView.visibility = View.INVISIBLE
                        binding.recyclerView.visibility = View.VISIBLE
                        binding.linearLayout.setBackgroundResource(R.color.white)
                    }

                    items = response.body()!!.documents
                }
            }

            override fun onFailure(call: Call<SearchResult>, t: Throwable) {
                Log.d("로그", "MainActivity - onFailure() 호출됨")
            }

        })
    }

    // 리사이클러뷰 아이템 클릭했을 때
    override fun onItemClicked(position: Int, imageView: ImageView, drawable: Drawable) {
        sharedViewModel.imgUrl.value = items[position].imageUrl
        sharedViewModel.items = items[position]
        sharedViewModel.drawable = drawable

        val extras = FragmentNavigatorExtras(
            imageView to items[position].imageUrl
        )

        findNavController().navigate(
            R.id.action_searchResultFragment_to_resultExtendFragment,
            null,
            null,
            extras
        )
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        callback.remove()
    }
}