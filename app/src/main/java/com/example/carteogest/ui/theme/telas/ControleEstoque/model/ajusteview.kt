import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.carteogest.datadb.data_db.products.ProductViewModel
import com.example.carteogest.datadb.data_db.products.Products
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class StockAdjustment(
    val productName: String,
    val quantity: Int,
    val isPositive: Boolean,
)

class StockAdjustmentViewModel(private val productViewModel: ProductViewModel) : ViewModel() {

    private val _lastAdjustment = MutableStateFlow<StockAdjustment?>(null)
    val lastAdjustment: StateFlow<StockAdjustment?> = _lastAdjustment

    fun adjustStock(productName: String, quantity: Int, isPositive: Boolean) {
        viewModelScope.launch {
            // Busca o produto pelo nome (ou ID, se preferir)
            val product = productViewModel.products.value.find { it.name == productName }
            product?.let {
                val newQuantity = if (isPositive) it.quantity + quantity else it.quantity - quantity
                val updatedProduct = it.copy(quantity = newQuantity)
                productViewModel.updateProduct(updatedProduct)

                _lastAdjustment.value = StockAdjustment(
                    productName = productName,
                    quantity = quantity,
                    isPositive = isPositive
//
                )
            }
        }
    }
}
