package viewModel

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel

abstract class ViewModel {

    val viewModelScope = CoroutineScope(Dispatchers.Default)

    fun onClear() {
        viewModelScope.cancel()
        println("view model cleared")
    }
}