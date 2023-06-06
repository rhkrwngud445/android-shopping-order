package woowacourse.shopping.data.remote.cart

import com.google.gson.annotations.SerializedName
import woowacourse.shopping.data.remote.product.ProductDataModel

data class CartDataModel(
    val id: Int,
    @SerializedName("product")
    val product: ProductDataModel,
    @SerializedName("quantity")
    var count: Int,
)
