package com.techfortyone.beerappcompose.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.cachedIn
import androidx.paging.map
import com.techfortyone.beerappcompose.data.local.BeerEntity
import com.techfortyone.beerappcompose.data.mappers.toBeerModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import javax.inject.Inject


@HiltViewModel
class BeerViewModel @Inject constructor(pager: Pager<Int, BeerEntity>) : ViewModel() {
    val beerPagingFlow = pager.flow.map {pagingData ->
        pagingData.map { it.toBeerModel() }
    }.cachedIn(viewModelScope)
}