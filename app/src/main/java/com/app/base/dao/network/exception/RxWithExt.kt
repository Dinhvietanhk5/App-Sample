package com.app.base.dao.network.exception

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.Gravity
import androidx.core.content.ContextCompat
import com.app.base.R
import com.app.base.dao.model.base.ResponeBase
import com.app.base.ui.login.LoginActivity
import com.app.base.utils.onDialogLoadingShadow
import com.app.base.utils.onDismissDialogLoading
import com.newsoft.nscustom.ext.context.startActivityExt
import com.newsoft.nscustomview.cfalertdialog.CFAlertDialog
import com.trello.rxlifecycle.ActivityEvent
import com.trello.rxlifecycle.ActivityLifecycleProvider
import com.trello.rxlifecycle.kotlin.bindUntilEvent
import rx.Observable
import rx.Observer
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 * subscribe api
 */

inline fun <T> Observable<T>.subscribeUntilDestroy(
    context: Context,
    type: LoadingType,
    observer: KObserver<T>.() -> Unit
): Subscription {
    checkDialogLoading(context, type)
    return this.observeOn(AndroidSchedulers.mainThread())
        .subscribeOn(Schedulers.computation())
        .bindUntilEvent(context as ActivityLifecycleProvider, ActivityEvent.DESTROY)
        .subscribe(
            KObserver<T>(context, DialogType.CHECK_ERROR, type != LoadingType.NOT).apply(
                observer
            )
        )
}

/**
 * subscribe api RecyclerView
 */
inline fun <T> Observable<T>.subscribeUntilDestroy(
    context: Context,
    index: Int,
    observer: KObserver<T>.() -> Unit
): Subscription {
    val type = if (index == 0) LoadingType.SHOW_SHADOW else LoadingType.NOT
    checkDialogLoading(context, type)
    return this.observeOn(AndroidSchedulers.mainThread())
        .subscribeOn(Schedulers.computation())
        .bindUntilEvent(context as ActivityLifecycleProvider, ActivityEvent.DESTROY)
        .subscribe(
            KObserver<T>(context, DialogType.CHECK_ERROR, type != LoadingType.NOT).apply(
                observer
            )
        )
}

/**
 * subscribe api not check error (not show notification)
 */

inline fun <T> Observable<T>.subscribeUntilDestroyNotCheckError(
    context: Context,
    type: LoadingType,
    observer: KObserver<T>.() -> Unit
): Subscription {
    checkDialogLoading(context, type)
    return this.observeOn(AndroidSchedulers.mainThread())
        .subscribeOn(Schedulers.computation())
        .bindUntilEvent(context as ActivityLifecycleProvider, ActivityEvent.DESTROY)
        .subscribe(
            KObserver<T>(context, DialogType.NOT_CHECK_ERROR, type != LoadingType.NOT).apply(
                observer
            )
        )
}

/**
 * subscribe api check success show notification
 */

inline fun <T> Observable<T>.subscribeUntilDestroyCheckSuccess(
    context: Context,
    type: LoadingType,
    observer: KObserver<T>.() -> Unit
): Subscription {
    checkDialogLoading(context, type)
    return this.observeOn(AndroidSchedulers.mainThread())
        .subscribeOn(Schedulers.computation())
        .bindUntilEvent(context as ActivityLifecycleProvider, ActivityEvent.DESTROY)
        .subscribe(
            KObserver<T>(context, DialogType.CHECK_SUCCESS, type != LoadingType.NOT).apply(
                observer
            )
        )
}

/**
 * show, hide notification by LoadingType
 */

fun checkDialogLoading(
    context: Context,
    type: LoadingType,
) {
    if (type == LoadingType.SHOW_SHADOW)
        context.onDialogLoadingShadow()
    else if (type == LoadingType.SHOW_TRANSPARENT)
        context.onDialogLoadingShadow()
}

enum class LoadingType {
    NOT,
    SHOW_SHADOW,
    SHOW_TRANSPARENT
}

enum class DialogType {
    CHECK_ERROR,         //TODO: hiển thị dialog thông báo khi api lỗi
    NOT_CHECK_ERROR,     //TODO: ko hiển thị dialog thông báo khi api lỗi
    CHECK_SUCCESS        //TODO: hiển thị dialog thông báo khi api trả về thành công
}

class KObserver<T>(context: Context, checkDialog: DialogType, isLoading: Boolean) : Observer<T> {

    private var onNext: ((T) -> Unit)? = null
    private var onError: (() -> Unit)? = null
    private var onCompleted: (() -> Unit)? = null
    private var mContext = context
    private var mIsLoading = isLoading
    private var mDialogType = checkDialog
    private var responeBase = ResponeBase()

    override fun onNext(t: T) {
        val responeBase = t as ResponeBase
        this.responeBase = responeBase

        //TODO: ẩn dialog loading
        if (mIsLoading) mContext.onDismissDialogLoading()

        //TODO: check error
        if (responeBase.error) {
            //TODO: token failure về trang login
            if ((responeBase.code == 1001 || responeBase.msg.contains("Token sai")) && mDialogType != DialogType.NOT_CHECK_ERROR) {
                (mContext as Activity).startActivityExt<LoginActivity>()
                return
            }
            //TODO: check type dialog -> showdialog thông báo lỗi
            if (mDialogType != DialogType.NOT_CHECK_ERROR)
                mContext.run {
                    val builder = CFAlertDialog.Builder(mContext)
                        .setDialogStyle(CFAlertDialog.CFAlertStyle.ALERT)
                        .setTextGravity(Gravity.CENTER)
                        .setTitle("Thông báo")
                        .setMessage(responeBase.msg)
                    builder.show()
                }

            //TODO: callback lỗi
            this.onError?.invoke()
            return
        }

        //TODO: check type dialog -> showdialog thông báo thành công
        if (mDialogType == DialogType.CHECK_SUCCESS) {
            mContext.run {
                val builder = CFAlertDialog.Builder(mContext)
                    .setDialogStyle(CFAlertDialog.CFAlertStyle.ALERT)
                    .setTitle("Thông báo")
                    .setMessage(responeBase.msg)
                    .addButton(
                        "Đóng",
                        ContextCompat.getColor(mContext, R.color.blue2),
                        ContextCompat.getColor(mContext, R.color.gray6),
                        CFAlertDialog.CFAlertActionStyle.NEGATIVE,
                        CFAlertDialog.CFAlertActionAlignment.JUSTIFIED
                    ) { dialog, which ->
                        onNext?.invoke(t)
                    }
                builder.show()
            }
            return
        }
        //TODO: callback thành công
        onNext?.invoke(t)
    }

    override fun onError(e: Throwable) {
        Log.e("onError", "${e.message}")
        this.onError?.invoke()
        if (mIsLoading) mContext.onDismissDialogLoading()

        //TODO: kiểm tra nếu api responeBase ko lỗi thì ko báo lỗi ở đây
        // trường hợp lỗi và do hiển thị view lỗi ở respone trả về

        if (!responeBase.error && NetworkUtil.isOnline())
            return

        val msg = if (!NetworkUtil.isOnline()) e.message!!
        else "Có lỗi trong quá trình kết nối"

        if (mDialogType == DialogType.NOT_CHECK_ERROR) return
        val builder = CFAlertDialog.Builder(mContext)
            .setDialogStyle(CFAlertDialog.CFAlertStyle.ALERT)
            .setTitle("Thông báo")
            .setMessage(msg)
        builder.show()
    }

    override fun onCompleted() {
        this.onCompleted?.invoke()
    }

    fun onNext(function: (T) -> Unit) {
        this.onNext = function
    }

    fun onError(function: () -> Unit) {
        this.onError = function
    }

    fun onCompleted(function: () -> Unit) {
        this.onCompleted = function
    }

}
