package com.nosetrap.alertlib

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import cn.pedant.SweetAlert.SweetAlertDialog
import kotlinx.android.synthetic.main.alert_ok.*
import kotlinx.android.synthetic.main.body_view.*
import kotlinx.android.synthetic.main.confirm_alert.*

class AlertDialog(private val activity: Activity,private val alertType: Int = TYPE_NORMAL) {

    companion object{
        /**
         * the type of alert dialog that is used
         */
        const val TYPE_NORMAL = 0
        const val TYPE_OK = 1
        const val TYPE_CONFIRM = 2

        /**
         * determines what type of custom view is used
         */
        //the custom view is disabled so use the default view
        private const val CUSTOM_VIEW_DISABLED = 0
        // a layout resource id is the custom view
        private const val CUSTOM_VIEW_LAYOUT_RES = 1
        // a view object is the custom view
        private const val CUSTOM_VIEW_OBJECT = 2

    }


    private val dialog = Dialog(activity)

    var onOkClicked: (() -> Unit)? = null
    var onConfirmClicked: (() -> Unit)? = null
    var onCancelClicked: (() -> Unit)? = null

    private var view: Any? = null
    private var customViewType: Int = CUSTOM_VIEW_DISABLED

    fun setCustomView(@LayoutRes view: Int){
        this.view = view
        customViewType = CUSTOM_VIEW_LAYOUT_RES
    }

    fun setCustomView(view: View){
        this.view = view
        customViewType = CUSTOM_VIEW_OBJECT
    }

    fun dismiss(){
        SweetAlertDialog.NORMAL_TYPE
        dialog.dismiss()
    }

    private fun setDialogView() {
        when (customViewType) {
            CUSTOM_VIEW_OBJECT -> dialog.setContentView(view as View)
            CUSTOM_VIEW_LAYOUT_RES -> dialog.setContentView(view as Int)
            else -> dialog.setContentView(getDialogView())
        }
    }
    
    fun show(title: String, message: String) {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        setDialogView()

        val dm = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(dm)
        val lp = WindowManager.LayoutParams()
        val window = dialog.window
        window!!.setGravity(Gravity.CENTER)
        lp.copyFrom(window.attributes)

        //This makes the dialog take up the full width
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        lp.windowAnimations = R.style.DialogAnimation
        window.attributes = lp

        dialog.tvTitle.text = title
        dialog.tvMessage.text = message

        setOnClickListeners()

        dialog.show()
    }

    private fun setOnClickListeners(){
        when(alertType){
            TYPE_OK -> {
                dialog.tvOk.setOnClickListener {
                    dialog.dismiss()
                    onOkClicked?.invoke()
                }
            }
            TYPE_CONFIRM ->{
                dialog.tvConfirm.setOnClickListener {
                    dialog.dismiss()
                    onConfirmClicked?.invoke()
                }
                dialog.tvCancel.setOnClickListener {
                    dialog.dismiss()
                    onCancelClicked?.invoke()
                }
            }
        }
    }

    private fun getDialogView(): Int{
        return when(alertType){
            TYPE_CONFIRM -> R.layout.confirm_alert
            TYPE_OK -> R.layout.alert_ok
            else -> R.layout.alert_normal
        }
    }
}