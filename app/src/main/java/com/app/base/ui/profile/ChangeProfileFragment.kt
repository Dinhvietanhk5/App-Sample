package com.app.base.ui.profile

import android.net.Uri
import android.view.Gravity
import android.view.View
import android.widget.Toast
import com.app.base.R
import com.app.base.dao.model.base.UploadFileModel
import com.app.base.dao.network.Repository
import com.app.base.dao.network.exception.LoadingType
import com.app.base.dao.network.exception.subscribeUntilDestroy
import com.app.base.ui.base.AppLayerManager
import com.app.base.ui.base.BaseFragment
import com.app.base.ui.main.MainActivity
import com.app.base.utils.presenter
import com.pawegio.kandroid.e
import kotlinx.android.synthetic.main.fragment_change_profile.*
import kotlinx.android.synthetic.main.fragment_change_profile.ivAvata
import kotlinx.android.synthetic.main.view_top_home.*
import java.io.File
import java.lang.Long


class ChangeProfileFragment : BaseFragment(R.layout.fragment_change_profile) {

    lateinit var mainActivity: MainActivity
    val profileUserModel = AppLayerManager.getInstance().profileUserModel
    var image = ""
    override fun onCreate() {
        mainActivity = activity as MainActivity
    }

    override fun onViewCreated(view: View) {
        tvCategory.text = "Thay đổi thông tin"
        tvCategory.gravity = Gravity.LEFT

        btnBack.setOnClickListener { mainActivity.supportFragmentManager.popBackStack() }
        try {
            profileUserModel.info.avatarURL?.let { ivAvata.setImageURI(it) }
            profileUserModel.info.fileStoreURL?.let { image = it }

            edtTitle.text = profileUserModel.info.name
            edtPhone.text = profileUserModel.info.phone
            profileUserModel.info.email?.let {
                edtEmail.text = it
            }
            profileUserModel.info.address?.let {
                edtAddress.text = it
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        //https://acloudvn.s3.ap-southeast-1.amazonaws.com/enterprise/1/disk/2021/09/15/f0ea07670f3d6ac5f4fa3bea6ee7a88a.jpg?X-Amz-Content-Sha256=UNSIGNED-PAYLOAD&X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=AKIAJWV2UWBGOU7M53TA%2F20210915%2Fap-southeast-1%2Fs3%2Faws4_request&X-Amz-Date=20210915T084502Z&X-Amz-SignedHeaders=host&X-Amz-Expires=5400&X-Amz-Signature=833ab7af2640ea4517f3d5df9df6851b41b6a5eb600d9e52ecc2cad53021f6ab

        btnSave.setOnClickListener { onUpdateInfo() }
//        btnChangeAvatar.setOnClickListener {
//            requireContext().onGetFile(
//                mainActivity,
//                MimeType.ofImage()
//            )
//        }
    }

    private fun onUpdateInfo() {
        edtTitle.reset()
        edtPhone.reset()
        if (edtTitle.validate() || edtPhone.validate())
            return

        presenter().onUpdateInforuser(
            edtTitle.text.toString(),
            edtPhone.text.toString(),
            edtEmail.text.toString(),
            edtAddress.text.toString(),
            image
        )
            .subscribeUntilDestroy(requireContext(), LoadingType.SHOW_SHADOW) {
                onNext {

                    AppLayerManager.getInstance().profileUserModel.info.apply {
                        id = it.info.id
                        name = it.info.name
                        phone = it.info.phone
                        email = it.info.email
                        uniqid = it.info.uniqid
                        address = it.info.address
                        avatarURL = it.info.avatarURL
                        fileStoreURL = it.info.fileStoreURL
                    }
                    Toast.makeText(
                        requireContext(),
                        it.msg,
                        Toast.LENGTH_LONG
                    ).show()
                    mainActivity.supportFragmentManager.popBackStack()
                }
            }

    }
//
//    fun onActivityResultFr(requestCode: Int, resultCode: Int, data: Intent?) {
//        var uriString = ""
//        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == Activity.RESULT_OK) {
//            uriString = Matisse.obtainPathResult(data)[0]
//            var uri = Matisse.obtainResult(data)[0]
//
//            ivAvata.setImageURI(uri)
////            uri.s
////            //TODO: nén ảnh
////            Luban.with(requireContext())
////                .load(uri)
////                .ignoreBy(100)
////                .setTargetDir("storage/emulated/0/")
////                .filter { path ->
////                    e(path)
////                    uriString = path
////                    !(TextUtils.isEmpty(path) || path.toLowerCase().endsWith(".gif"))
////                }
////                .setCompressListener(object : OnCompressListener {
////                    override fun onStart() {
////                    }
////
////                    override fun onSuccess(file: File) {
//            onUploadFile(uriString, "0", uri)
////                    }
////
////                    override fun onError(e: Throwable) {
////                        onUploadFile(uriString, uri)
////                    }
////                }).launch()
//        }
//    }

    fun calculateFileSize(filepath: Uri): String? {
        //String filepathstr=filepath.toString();
        val file = File(filepath.path)

        // Get length of file in bytes
        val fileSizeInBytes = file.length()
        // Convert the bytes to Kilobytes (1 KB = 1024 Bytes)
        val fileSizeInKB = fileSizeInBytes / 1024
        // Convert the KB to MegaBytes (1 MB = 1024 KBytes)
        val fileSizeInMB = fileSizeInKB / 1024
        return Long.toString(fileSizeInMB)
    }

    private fun onUploadFile(uriString: String, file_size: String, uri: Uri) {
        e(uriString)
        presenter().onUploadFile(
            requireContext(),
            file_name = uriString,
            file_size,
            uri,
            object : Repository.UploadFileListener {
                override fun onResponse(uploadFileModel: UploadFileModel) {
                    image = uploadFileModel.fileStoreURL
//                   presenter().onUpdateAvatar(uploadFileModel.fileStoreURL)
//                        .subscribeUntilDestroy(
//                            requireContext(),
//                            LoadingType.SHOW_TRANSPARENT
//                        ) {
//                            onNext {
//                                AppLayerManager.getInstance().profileUserModel.info = it.info
//
//                                Toast.makeText(
//                                    requireContext(),
//                                    "Tải lên ảnh đại diện thành công !",
//                                    Toast.LENGTH_LONG
//                                ).show()
//                            }
//                        }
                }
            })
    }
}