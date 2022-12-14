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
    CHECK_ERROR,         //TODO: hi???n th??? dialog th??ng b??o khi api l???i
    NOT_CHECK_ERROR,     //TODO: ko hi???n th??? dialog th??ng b??o khi api l???i
    CHECK_SUCCESS        //TODO: hi???n th??? dialog th??ng b??o khi api tr??? v??? th??nh c??ng
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

        //TODO: ???n dialog loading
        if (mIsLoading) mContext.onDismissDialogLoading()

        //TODO: check error
        if (responeBase.error) {
            //TODO: token failure v??? trang login
            if ((responeBase.code == 1001 || responeBase.msg.contains("Token sai")) && mDialogType != DialogType.NOT_CHECK_ERROR) {
                (mContext as Activity).startActivityExt<LoginActivity>()
                return
            }
            //TODO: check type dialog -> showdialog th??ng b??o l???i
            if (mDialogType != DialogType.NOT_CHECK_ERROR)
                mContext.run {
                    val builder = CFAlertDialog.Builder(mContext)
                        .setDialogStyle(CFAlertDialog.CFAlertStyle.ALERT)
                        .setTextGravity(Gravity.CENTER)
                        .setTitle("Th??ng b??o")
                        .setMessage(responeBase.msg)
                    builder.show()
                }

            //TODO: callback l???i
            this.onError?.invoke()
            return
        }

        //TODO: check type dialog -> showdialog th??ng b??o th??nh c??ng
        if (mDialogType == DialogType.CHECK_SUCCESS) {
            mContext.run {
                val builder = CFAlertDialog.Builder(mContext)
                    .setDialogStyle(CFAlertDialog.CFAlertStyle.ALERT)
                    .setTitle("Th??ng b??o")
                    .setMessage(responeBase.msg)
                    .addButton(
                        "????ng",
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
        //TODO: callback th??nh c??ng
        onNext?.invoke(t)
    }

    override fun onError(e: Throwable) {
        Log.e("onError", "${e.message}")
        this.onError?.invoke()
        if (mIsLoading) mContext.onDismissDialogLoading()

        //TODO: ki???m tra n???u api responeBase ko l???i th?? ko b??o l???i ??? ????y
        // tr?????ng h???p l???i v?? do hi???n th??? view l???i ??? respone tr??? v???

        if (!responeBase.error && NetworkUtil.isOnline())
            return

        val msg = if (!NetworkUtil.isOnline()) e.message!!
        else "C?? l???i trong qu?? tr??nh k???t n???i"

        if (mDialogType == DialogType.NOT_CHECK_ERROR) return
        val builder = CFAlertDialog.Builder(mContext)
            .setDialogStyle(CFAlertDialog.CFAlertStyle.ALERT)
            .setTitle("Th??ng b??o")
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
