package woowacourse.shopping.data.cart.local

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import woowacourse.shopping.data.cart.CartLocalDataModel

class CartDao(db: CartDbHelper) : CartLocalDataSource {

    private val writableDb: SQLiteDatabase = db.writableDatabase
    override fun addProduct(productId: Int) {
        val values = ContentValues().apply {
            put(CartDbContract.PRODUCT_ID, productId)
            put(CartDbContract.COUNT, 1)
            put(CartDbContract.TIMESTAMP, System.currentTimeMillis())
        }

        writableDb.insert(CartDbContract.TABLE_NAME, null, values)
    }

    override fun deleteCartProduct(productId: Int) {
        writableDb.delete(
            CartDbContract.TABLE_NAME,
            "${CartDbContract.PRODUCT_ID}=?",
            arrayOf(productId.toString()),
        )
    }

    override fun updateProductCount(cartProductInfo: CartLocalDataModel) {
        val values = ContentValues()
        values.put(CartDbContract.COUNT, cartProductInfo.count)
        writableDb.update(
            CartDbContract.TABLE_NAME,
            values,
            "${CartDbContract.PRODUCT_ID}= ?",
            arrayOf(cartProductInfo.productId.toString()),
        )
    }

    override fun getProductsInfo(limit: Int, offset: Int): List<CartLocalDataModel> {
        val cartLocalDataModel = mutableListOf<CartLocalDataModel>()

        val cursor = makePageCursor(limit, offset)
        while (cursor.moveToNext()) {
            val productInfo = CartLocalDataModel(
                cursor.getCartProductId(),
                cursor.getCartProductCount(),
            )
            cartLocalDataModel.add(productInfo)
        }
        cursor.close()
        return cartLocalDataModel
    }

    private fun makePageCursor(limit: Int, offset: Int): Cursor = writableDb.query(
        CartDbContract.TABLE_NAME,
        arrayOf(
            CartDbContract.PRODUCT_ID,
            CartDbContract.COUNT,
            CartDbContract.TIMESTAMP,
        ),
        null,
        null,
        null,
        null,
        "${CartDbContract.TIMESTAMP} DESC",
        "$offset,$limit",
    )

    override fun getAllProductsInfo(): List<CartLocalDataModel> {
        val cartLocalDataModel = mutableListOf<CartLocalDataModel>()

        val cursor = makeAllCursor()
        while (cursor.moveToNext()) {
            val productInfo = CartLocalDataModel(
                cursor.getCartProductId(),
                cursor.getCartProductCount(),
            )
            cartLocalDataModel.add(productInfo)
        }
        cursor.close()
        return cartLocalDataModel
    }

    override fun getProductInfoById(id: Int): CartLocalDataModel? {
        val query =
            "SELECT * FROM ${CartDbContract.TABLE_NAME} WHERE ${CartDbContract.PRODUCT_ID} = $id"
        val cursor: Cursor = writableDb.rawQuery(query, null)
        var cartLocalDataModel: CartLocalDataModel? = null
        if (cursor != null && cursor.moveToFirst()) {
            val productId = cursor.getInt(cursor.getColumnIndexOrThrow(CartDbContract.PRODUCT_ID))
            val count = cursor.getInt(cursor.getColumnIndexOrThrow(CartDbContract.COUNT))
            cartLocalDataModel = CartLocalDataModel(productId, count)
            cursor.close()
        }
        return cartLocalDataModel
    }

    private fun makeAllCursor(): Cursor = writableDb.query(
        CartDbContract.TABLE_NAME,
        arrayOf(
            CartDbContract.PRODUCT_ID,
            CartDbContract.COUNT,
            CartDbContract.TIMESTAMP,
        ),
        null,
        null,
        null,
        null,
        "${CartDbContract.TIMESTAMP} DESC",
        null,
    )

    private fun Cursor.getCartProductId(): Int =
        getInt(getColumnIndexOrThrow(CartDbContract.PRODUCT_ID))

    private fun Cursor.getCartProductCount(): Int =
        getInt(getColumnIndexOrThrow(CartDbContract.COUNT))
}
