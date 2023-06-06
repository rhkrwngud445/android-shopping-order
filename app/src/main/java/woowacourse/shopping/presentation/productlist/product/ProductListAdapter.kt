package woowacourse.shopping.presentation.productlist.product

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import woowacourse.shopping.databinding.ItemMoreBinding
import woowacourse.shopping.databinding.ItemProductBinding
import woowacourse.shopping.databinding.ItemRecentProductContainerBinding
import woowacourse.shopping.presentation.common.CartProductDiffItemCallback
import woowacourse.shopping.presentation.model.CartProductModel
import woowacourse.shopping.presentation.productlist.ProductListContract
import woowacourse.shopping.presentation.productlist.product.ProductListViewType.MORE_ITEM
import woowacourse.shopping.presentation.productlist.product.ProductListViewType.PRODUCT
import woowacourse.shopping.presentation.productlist.product.ProductListViewType.RECENT_PRODUCT
import woowacourse.shopping.presentation.productlist.recentproduct.RecentProductAdapter
import woowacourse.shopping.presentation.productlist.recentproduct.RecentProductContainerViewHolder

class ProductListAdapter(
    private val recentProductAdapter: RecentProductAdapter,
    private val presenter: ProductListContract.Presenter,
) : ListAdapter<CartProductModel, RecyclerView.ViewHolder>(CartProductDiffItemCallback()) {

    private lateinit var itemProductBinding: ItemProductBinding
    private lateinit var inflater: LayoutInflater

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (!::inflater.isInitialized) {
            inflater = LayoutInflater.from(parent.context)
        }

        return when (ProductListViewType.find(viewType)) {
            RECENT_PRODUCT -> {
                val containerBinding = ItemRecentProductContainerBinding.inflate(
                    inflater,
                    parent,
                    false,
                )

                RecentProductContainerViewHolder(
                    containerBinding,
                    recentProductAdapter,
                )
            }
            PRODUCT -> {
                itemProductBinding = ItemProductBinding.inflate(
                    inflater,
                    parent,
                    false,
                )

                ProductItemViewHolder(
                    itemProductBinding,
                    presenter,
                )
            }
            MORE_ITEM -> {
                val itemMoreBinding = ItemMoreBinding.inflate(
                    inflater,
                    parent,
                    false,
                )
                MoreItemViewHolder(itemMoreBinding, presenter)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ProductItemViewHolder) {
            holder.bind(getItem(position))
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            RECENT_PRODUCT_VIEW_POSITION -> RECENT_PRODUCT.number
            itemCount - 1 -> MORE_ITEM.number
            else -> PRODUCT.number
        }
    }

    companion object {
        const val RECENT_PRODUCT_VIEW_POSITION = 0
    }
}
