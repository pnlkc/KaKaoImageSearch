package com.example.kakaosearchapi

import android.graphics.drawable.Drawable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SearchViewModel : ViewModel() {

    private var _searchTerm = ""
    var searchTerm: String = _searchTerm

    private var _imgUrl = MutableLiveData<String>("")
    val imgUrl: MutableLiveData<String> get() = _imgUrl

    var items = Items()

    private var _page = MutableLiveData<Int>(0)
    val page: MutableLiveData<Int> get() = _page

    var drawable: Drawable? = null
}