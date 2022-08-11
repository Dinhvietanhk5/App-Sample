package com.app.base.ui.base

import android.graphics.Bitmap
import com.app.base.dao.model.base.ProfileUserModel
import kotlin.collections.ArrayList


class AppLayerManager {

    //    var data = CampainModel.Data()
    var profileUserModel = ProfileUserModel()
    var tokenFirebase = ""
    var imageUrlLoginBitmap: Bitmap? = null
    var imageUrlLogin = ""
    var imageIntroduce = ArrayList<Bitmap>()
    var totalPending = 0


    companion object {
        var sInstance: AppLayerManager? = null

        @Synchronized
        fun getInstance(): AppLayerManager {
            if (sInstance == null)
                sInstance = AppLayerManager()

            return sInstance as AppLayerManager
        }
    }

    constructor() {
    }

}