package woowacourse.shopping.data.remote.cart

import woowacourse.shopping.CartProductInfo

interface CartDataSource {
    fun addCartItem(productId: Int, onSuccess: (Int) -> Unit, onFailure: () -> Unit)
    fun deleteCartItem(cartItemId: Int, onSuccess: () -> Unit, onFailure: () -> Unit)
    fun updateCartItemQuantity(
        cartId: Int,
        count: Int,
        onSuccess: () -> Unit,
        onFailure: () -> Unit
    )

    fun getAllCartProductsInfo(
        onSuccess: (List<CartProductInfo>) -> Unit,
        onFailure: () -> Unit
    )
}