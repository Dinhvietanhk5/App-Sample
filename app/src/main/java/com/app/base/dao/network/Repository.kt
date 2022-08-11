package com.app.base.dao.network

import android.content.Context
import android.net.Uri
import com.app.base.dao.model.base.UploadFileModel
import com.app.base.dao.network.exception.LoadingType
import com.app.base.dao.network.exception.subscribeUntilDestroy
import com.app.base.utils.custom.InputStreamRequestBody
import com.app.base.utils.onDialogLoadingShadow
import com.app.base.utils.onDismissDialogLoading
import com.app.base.utils.sharePreferences.SPKeyEnum
import com.app.base.utils.sharePreferences.SharePreferencesManager
import com.newsoft.nscustom.ext.context.getDeviceName
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers


open class Repository {

    private var requestAPI = RequestAPI.getRestAPI()
    private var token = ""
    private var contentType = "application/x-www-form-urlencoded"
    private var authorization = "Bearer 6234564589ba427566777ac5db7b56"

    companion object {
        private var sInstance: Repository? = null

        @Synchronized
        fun getInstance(): Repository {
            if (sInstance == null)
                sInstance = Repository()
            return sInstance as Repository
        }
    }

    constructor() {
        token = SharePreferencesManager.getInstance().getValue(SPKeyEnum.TOKEN)!!
        val typeUrl = SharePreferencesManager.getInstance().getValue(SPKeyEnum.BASE_URL, 0)
        if (typeUrl == 1)
            authorization = "Bearer f1290186a5d0b1ceab27f4e77c0c5d68"
    }

    fun setToken(token: String) {
        this.token = token
    }

    //TODO: Login
    fun onCheckToken() =
        requestAPI.checktoken(authorization, token)

    fun onSigin(phone: String, password: String) =
        requestAPI.signIn(authorization, phone, password, "0", getDeviceName(), "android")

    //TODO: Registration

    fun onSendOTP(phone: String) =                           // nhận otp đăng ký
        requestAPI.sendOTP(authorization, contentType, phone)

    fun onOTPVertify(otpSession: String, otpCode: String) =    // xác nhận otp đăng ký
        requestAPI.OTPVerify(authorization, contentType, otpSession, otpCode)

    fun onRegistry(
        authToken: String,
        password: String,
        name: String,
        address: String,
        email: String,
        refCode: String
    ) =          // đăng ký thông tin
        requestAPI.registry(
            authorization,
            authToken,
            password,
            name,
            address,
            email,
            refCode,
            "0",// fribase
            getDeviceName()
        )

    fun onVerifyForgotPass(auth_token: String, password: String, phone: String) =
        requestAPI.verifyForgotPass(authorization, auth_token, password, phone)

    //TODO: Change profile

    fun onUpdateInforuser(
        name: String,
        phone: String,
        email: String,
        address: String,
        image: String
    ) =
        requestAPI.updateInforuser(authorization, token, name, phone, email, address, image)

    fun onChangePassword(oldpassword: String, newpassword: String) =
        requestAPI.changePassword(authorization, oldpassword, newpassword, newpassword, token)


    //TODO:-------------------------------- upload file ---------------------------------------
    fun onUploadFile(
        context: Context,
        file_name: String,
        file_size: String,
        uri: Uri,
        listener: UploadFileListener
    ) {
        context.onDialogLoadingShadow()
        //TODO: upload file name giữ chỗ
        requestAPI.uploadFileName(authorization, token, file_name, file_size)
            .subscribeUntilDestroy(context, LoadingType.NOT) {
                onNext {
                    onUploadToServer(context, it, uri, listener)
                }
            }
    }

    //TODO: gửi file lên server
    private fun onUploadToServer(
        context: Context,
        uploadModel: UploadFileModel,
        uri: Uri,
        listener: UploadFileListener
    ) {
        val part = uploadModel.upload.replace(RequestAPI.URLS3, "")
        context.contentResolver.getType(uri)?.let { mimeType ->
            val requestBody = InputStreamRequestBody(context.contentResolver, uri)
            RequestAPI.getRestAPIUpFile().uploadToServer(mimeType, part, requestBody)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    listener.onResponse(uploadModel)
                    context.onDismissDialogLoading()
                }, {
                    listener.onResponse(uploadModel)
                    context.onDismissDialogLoading()
                })
        }
    }

    fun onCheckVersion() =
        requestAPI.checkVersion(authorization, "android")

    //TODO:-------------------------------------------------------------------------------------

    interface UploadFileListener {
        fun onResponse(uploadFileModel: UploadFileModel)
    }

    fun onAddAddress(
        title: String,
        phone: String,
        address: String,
        province_id: String,
        district_id: String,
        sub_district_id: String,
        selected: Int
    ) =
        requestAPI.addAddress(
            authorization,
            token,
            title,
            phone,
            address,
            province_id,
            district_id,
            sub_district_id,
            selected
        )

}