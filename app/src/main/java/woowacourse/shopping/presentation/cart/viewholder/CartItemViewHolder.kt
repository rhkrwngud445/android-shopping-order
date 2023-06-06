package woowacourse.shopping.presentation.cart.viewholder

import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import woowacourse.shopping.databinding.ItemCartBinding
import woowacourse.shopping.presentation.cart.CartContract
import woowacourse.shopping.presentation.common.CounterListener
import woowacourse.shopping.presentation.model.CartProductModel

class CartItemViewHolder(
    private val binding: ItemCartBinding,
    private val presenter: CartContract.Presenter,
    private val updateProductPrice: (TextView, CartProductModel) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {
    private lateinit var cartProductModel: CartProductModel
    private val counterListener = object : CounterListener {
        override fun onPlus(count: Int) {
            updateCart(count)
        }

        override fun onMinus(count: Int) {
            updateCart(count)
        }
    }

    init {
        deleteButtonClick()
    }

    private fun updateCart(count: Int) {
        presenter.updateProductCount(cartProductModel, count)
    }

    fun bind(item: CartProductModel) {
        cartProductModel = item
        binding.cartProduct = cartProductModel
        updateProductPrice(binding.textCartProductPrice, cartProductModel)
        setUpCounterView()
        checkBoxChange()
    }

    private fun setUpCounterView() {
        binding.counterCartProduct.setUpView(
            counterListener,
            initCount = cartProductModel.count,
            minimumCount = 1,
        )
    }

    private fun deleteButtonClick() {
        binding.imageCartDelete.setOnClickListener {
            presenter.deleteProductItem(cartProductModel)
        }
    }

    private fun checkBoxChange() {
        binding.checkboxProductCart.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                presenter.addProductInOrder(cartProductModel)
                updateOrderView()
            } else {
                presenter.deleteProductInOrder(cartProductModel)
                updateOrderView()
            }
        }
    }

    private fun updateOrderView() {
        presenter.updateOrderCount()
        presenter.updateOrderPrice()
        presenter.checkCurrentPageProductsOrderState()
    }
}
