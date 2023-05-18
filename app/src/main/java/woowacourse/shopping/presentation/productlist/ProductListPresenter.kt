package woowacourse.shopping.presentation.productlist

import woowacourse.shopping.Product
import woowacourse.shopping.Products
import woowacourse.shopping.presentation.mapper.toPresentation
import woowacourse.shopping.presentation.model.ProductModel
import woowacourse.shopping.repository.CartRepository
import woowacourse.shopping.repository.ProductRepository
import woowacourse.shopping.repository.RecentProductRepository

class ProductListPresenter(
    private val view: ProductListContract.View,
    private val productRepository: ProductRepository,
    private val recentProductRepository: RecentProductRepository,
    private val cartRepository: CartRepository,
) : ProductListContract.Presenter {

    private val products = Products()

    init {
        updateProductItems()
        updateRecentProductItems()
    }

    override fun updateProductItems() {
        val receivedProducts = productRepository.getProductsWithRange(products.size, PRODUCTS_SIZE)
        products.addProducts(receivedProducts)
        view.loadProductModels(products.toPresentation())
    }

    override fun updateRecentProductItems() {
        val recentProductModels = getRecentProductModels()
        view.loadRecentProductModels(recentProductModels)
    }

    override fun saveRecentProduct(productId: Int) {
        recentProductRepository.deleteRecentProductId(productId)
        recentProductRepository.addRecentProductId(productId)
    }

    override fun updateCartCount() {
        val cartProductType = cartRepository.getAllCartProductsInfo()
        val cartCount = cartProductType.count
        view.showCartCount(cartCount)
    }

    private fun getRecentProductModels(): List<ProductModel> {
        val recentProductIds = recentProductRepository.getRecentProductIds(RECENT_PRODUCTS_SIZE)
        return findProductsById(recentProductIds)
    }

    private fun findProductsById(productIds: List<Int>): List<ProductModel> {
        return productIds.map {
            val product = productRepository.findProductById(it) ?: Product.defaultProduct
            product.toPresentation()
        }
    }

    private fun Products.toPresentation(): List<ProductModel> {
        return items.map { it.toPresentation() }
    }

    companion object {
        private const val PRODUCTS_SIZE = 20
        private const val RECENT_PRODUCTS_SIZE = 10
    }
}
