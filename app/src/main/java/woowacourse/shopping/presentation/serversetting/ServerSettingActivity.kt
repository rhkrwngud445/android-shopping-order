package woowacourse.shopping.presentation.serversetting

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import woowacourse.shopping.data.ApiClient
import woowacourse.shopping.data.common.PreferenceUtil
import woowacourse.shopping.data.local.recentproduct.RecentProductDao
import woowacourse.shopping.data.local.recentproduct.RecentProductDbHelper
import woowacourse.shopping.data.remote.product.ProductRemoteDataSource
import woowacourse.shopping.data.repository.RecentProductRepositoryImpl
import woowacourse.shopping.databinding.ActivityServerSettingBinding
import woowacourse.shopping.presentation.productlist.ProductListActivity

class ServerSettingActivity : AppCompatActivity() {
    private val binding: ActivityServerSettingBinding by lazy {
        ActivityServerSettingBinding.inflate(layoutInflater)
    }
    private val presenter: ServerContract.Presenter by lazy {
        ServerSettingPresenter(
            PreferenceUtil(this),
            RecentProductRepositoryImpl(
                recentProductLocalDataSource = RecentProductDao(RecentProductDbHelper(this)),
                productDataSource = ProductRemoteDataSource,
            ),
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)
        setupView()
    }

    private fun setupView() {
        binding.btnGlanServer.setOnClickListener {
            startMain(SERVER_GLAN)
        }
        binding.btnGrayServer.setOnClickListener {
            startMain(SERVER_GRAY)
        }
    }

    private fun startMain(newUrl: String) {
        ApiClient.initClient(newUrl, PreferenceUtil(this))
        presenter.saveAuthToken()
        presenter.deleteCart()
        startActivity(ProductListActivity.getIntent(this))
    }

    companion object {
        private const val SERVER_GLAN = "http://13.125.163.216:8080/"
        private const val SERVER_GRAY = "http://54.180.83.161:8080/"
    }
}
