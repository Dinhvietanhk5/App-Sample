//package com.ntr.ecom.data.realm_db
//
//import android.content.Context
//import io.realm.*
//
//class RealmDB {
//
//    companion object {
//        var sInstance: RealmDB? = null
//
//        @Synchronized
//        fun initializeInstance(context: Context?) {
//            if (sInstance == null) {
//                sInstance =
//                    RealmDB(
//                        context!!
//                    )
//            }
//        }
//
//        @Synchronized
//        fun getInstance(): RealmDB {
//            checkNotNull(sInstance) {
//                RealmDB::class.java.simpleName +
//                        " is not initialized, call initializeInstance(..) method first."
//            }
//            return sInstance as RealmDB
//        }
//    }
//
//    lateinit var realmOrder: Realm
//    lateinit var context: Context
//
//    constructor(context: Context) {
//        this.context = context
//        Realm.init(context)
//
//        val configRepost = RealmConfiguration.Builder()
//            .name("DBUserOrderProduct.realm")
//            .schemaVersion(0)
//            .modules(UserOrderProduct())
//            .allowWritesOnUiThread(true)
////            .migration { realm: DynamicRealm, oldVersion: Long, newVersion: Long ->
////                Log.e("migration", " $oldVersion $newVersion")
////                val schema = realm.schema
////                if (newVersion == 1L) {
////                    schema["UserOrderProduct"]!!.addField("img3", String::class.java)
////                }
////            }
//            .build()
//        realmOrder = Realm.getInstance(configRepost)
//    }
//
//    //TODO: thêm sản phẩm vào đơn hàng
//    fun insertUserOrderProduct(userOrderProduct: UserOrderProduct) {
//        if (checkProduct(userOrderProduct.id)) {
//            realmOrder.beginTransaction()
//            realmOrder.insertOrUpdate(userOrderProduct)
//            realmOrder.commitTransaction()
//        } else {
//            //Todo: đã có thì update
//            realmOrder.executeTransaction { realm ->
//                val result = realm.where(UserOrderProduct::class.java)
//                    .equalTo("id", userOrderProduct.id)
//                    .findFirst()
//                result!!.quantity = userOrderProduct.quantity
//            }
//        }
//    }
//
//    //TODO: lấy sản phẩm trong đơn hàng theo id sản phẩm
//    fun getUserOrderProduct(id: String): UserOrderProduct? {
//        return realmOrder.where(UserOrderProduct::class.java)
//            .equalTo("id", id)
//            .findFirst()
//    }
//
//    //TODO: lấy tất cả các sản phẩm trong đơn hàng
//    fun getAllUserOrderProduct(): ArrayList<ItemOrderModel.Products> {
//        val realmResults = realmOrder.where(UserOrderProduct::class.java).findAll()
//        val items = ArrayList<ItemOrderModel.Products>()
//        for (item in realmResults) {
//            val orderProduct = ItemOrderModel.Products()
//            orderProduct.id = item.id
//            orderProduct.title = item.title
//            orderProduct.buy_price = item.buy_price
//            orderProduct.sell_price = item.sell_price
//            orderProduct.quantity = item.quantity
//            orderProduct.sku = item.sku
//            orderProduct.photo = item.photo
//            orderProduct.discount_percent = item.discount_percent
//            items.add(orderProduct)
//        }
//        return items
//    }
//
//    //TODO: lấy số lượng tất cả các sản phẩm trong đơn hàng
//    fun getCountAllUserOrderProduct(): Int {
//        var count = 0
//        val realmResults = realmOrder.where(UserOrderProduct::class.java).findAll()
//        for (item in realmResults)
//            count += item.quantity
//        return count
//    }
//
//    //TODO: xóa tất cả các sản phẩm trong đơn hàng
//    fun deleteAllUserOrderProduct() {
//        realmOrder.beginTransaction()
//        val realmResults = realmOrder.where(UserOrderProduct::class.java).findAll()
//        realmResults.deleteAllFromRealm()
//        realmOrder.commitTransaction()
//    }
//
//    //Todo: kiểm tra mặt hàng đã có hay chưa
//    // true chưa có, false đã có
//    private fun checkProduct(
//        id: String
//    ): Boolean {
//        val userOrder = realmOrder.where(UserOrderProduct::class.java)
//            .equalTo("id", id)
//            .findAll()
//        return userOrder.size == 0
//    }
//
//    //TODO: xóa sản phẩm
//    fun deleteProduct(id: String) {
//        realmOrder.beginTransaction()
//        val userOrder = realmOrder.where(UserOrderProduct::class.java)
//            .equalTo("id", id)
//            .findFirst()
//        userOrder!!.deleteFromRealm()
//        realmOrder.commitTransaction()
//    }
//
//}