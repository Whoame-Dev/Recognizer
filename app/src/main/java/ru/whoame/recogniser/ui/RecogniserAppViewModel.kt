package ru.whoame.recogniser.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.whoame.recogniser.data.repository.RecognisedObjectRepository
import ru.whoame.recogniser.utils.mapResource

class RecogniserAppViewModel(
    private val repository: RecognisedObjectRepository,
) : ViewModel() {

    private val mutableStateFlow = MutableStateFlow<String>("Init")
    val stateFlow: StateFlow<String> get() = mutableStateFlow.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.Default) {
            repository.getAll()
                .mapResource { list -> list.firstOrNull()?.title.orEmpty() }
                .collect { value ->
                    mutableStateFlow.value = value.toString()
                }
        }
    }

}
