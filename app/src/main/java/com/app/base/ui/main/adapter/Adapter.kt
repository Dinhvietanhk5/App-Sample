package com.app.base.ui.main.adapter

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.base.R
import com.app.base.dao.model.base.UserSuitableModel
import com.newsoft.nscustomview.recyclerview.BaseAdapter
import kotlinx.android.synthetic.main.item_text.view.*


class Adapter :
    BaseAdapter<UserSuitableModel.Item?, Adapter.AdapterHolder>() {


//    fun setItems(items: List<UserSuitableModel.Item?>?, index: Int) {
//        super.setItems(items as java.util.ArrayList<UserSuitableModel.Item?>?, index)
//    }

    override fun onCreateHolder(parent: ViewGroup?, viewType: Int): AdapterHolder {
        return AdapterHolder(setView(R.layout.item_text))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindView(
        holder: AdapterHolder?,
        item: UserSuitableModel.Item?,
        position: Int,
        size: Int
    ) {
        item?.let {
            holder!!.tv.text = it.name
        }
    }

    class AdapterHolder(mView: View) : RecyclerView.ViewHolder(mView) {
        val tv = mView.tv
    }
}