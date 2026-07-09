package com.example.parcial_final_pdm.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parcial_final_pdm.model.Product
import com.example.parcial_final_pdm.repository.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProductViewModel : ViewModel() {
    private val repository = ProductRepository()

    private val _uiState = MutableStateFlow<UiState<List<Product>>>(UiState.Loading)
    val uiState: StateFlow<UiState<List<Product>>> = _uiState.asStateFlow()

    private val _categories = MutableStateFlow<List<String>>(emptyList())
    val categories: StateFlow<List<String>> = _categories.asStateFlow()

    private val _selectedCategory = MutableStateFlow("Todas las categorías")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _detailState = MutableStateFlow<UiState<Product>>(UiState.Loading)
    val detailState: StateFlow<UiState<Product>> = _detailState.asStateFlow()

    private var allProducts: List<Product> = emptyList()

    init {
        loadInitialData()
    }

    fun loadInitialData() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                // Obtener productos y categorías simultáneamente
                val productsResult = repository.getProducts()
                val categoriesResult = repository.getCategories()

                allProducts = productsResult

                val categoryList = mutableListOf("Todas las categorías")
                categoryList.addAll(categoriesResult)
                _categories.value = categoryList

                applyFilters()
            } catch (e: Exception) {
                _uiState.value = UiState.Error("Error de conexión: ${e.message}")
            }
        }
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
        applyFilters()
    }

    fun onCategorySelected(category: String) {
        _selectedCategory.value = category
        applyFilters()
    }

    private fun applyFilters() {
        val query = _searchQuery.value.trim().lowercase()
        val category = _selectedCategory.value

        var filteredList = allProducts

        if (category != "Todas las categorías") {
            filteredList = filteredList.filter { it.category == category }
        }

        if (query.isNotEmpty()) {
            filteredList = filteredList.filter { it.title.lowercase().contains(query) }
        }

        _uiState.value = UiState.Success(filteredList)
    }

    fun loadProductDetail(id: Int) {
        viewModelScope.launch {
            _detailState.value = UiState.Loading
            try {
                val product = repository.getProductById(id)
                _detailState.value = UiState.Success(product)
            } catch (e: Exception) {
                _detailState.value = UiState.Error("Error al cargar detalles del producto")
            }
        }
    }
}