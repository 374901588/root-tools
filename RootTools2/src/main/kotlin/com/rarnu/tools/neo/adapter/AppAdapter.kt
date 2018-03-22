package com.rarnu.tools.neo.adapter

import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.ImageView
import android.widget.Switch
import android.widget.TextView
import com.rarnu.base.app.BaseAdapter
import com.rarnu.tools.neo.R
import com.rarnu.tools.neo.data.AppInfo
import kotlinx.android.synthetic.main.listitem_app.view.*

class AppAdapter(context: Context, list: MutableList<AppInfo>?) : BaseAdapter<AppInfo, AppAdapter.AppHolder>(context, list) {

    override fun fillHolder(baseVew: View, holder: AppHolder, item: AppInfo) {
        holder.setItem(item)
        holder.prefStatus?.visibility = if (showSwitch) View.VISIBLE else View.GONE
    }

    override fun getAdapterLayout(): Int = R.layout.listitem_app

    override fun newHolder(baseView: View): AppHolder = AppHolder(baseView)

    private var showSwitch = false

    override fun getValueText(item: AppInfo): String? = (item.name + item.packageName)

    fun setShowSwitch(on: Boolean) {
        showSwitch = on
        notifyDataSetChanged()
    }

    inner class AppHolder(v: View) {
        private var ivIcon: ImageView? = null
        internal var prefStatus: Switch? = null
        private var tvName: TextView? = null
        private var tvPackageName: TextView? = null

        internal fun setItem(item: AppInfo) {
            ivIcon?.setImageDrawable(item.imageId)
            prefStatus?.isChecked = !item.isDisable
            tvName?.text = item.name
            tvPackageName?.text = item.packageName
            if (item.isSystem) {
                tvName?.setTextColor(context.resources.getColor(android.R.color.holo_green_dark, context.theme))
                tvPackageName?.setTextColor(context.resources.getColor(android.R.color.holo_green_light, context.theme))
            } else {
                tvName?.setTextColor(Color.BLACK)
                tvPackageName?.setTextColor(Color.DKGRAY)
            }
        }

        init {
            ivIcon = v.ivIcon
            prefStatus = v.prefStatus
            tvName = v.tvName
            tvPackageName = v.tvPackageName
        }
    }
}
