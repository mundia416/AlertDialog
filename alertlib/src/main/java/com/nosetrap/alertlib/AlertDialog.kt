package com.nosetrap.alertlib

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.Window
import android.view.WindowManager
import cn.pedant.SweetAlert.SweetAlertDialog
import kotlinx.android.synthetic.main.alert_ok.*
import kotlinx.android.synthetic.main.body_view.*
import kotlinx.android.synthetic.main.confirm_alert.*

class AlertDialog(private val activity: Activity,private val alertType: Int = TYPE_NORMAL) {

    companion object{
        const val TYPE_NORMAL = 0
        const val TYPE_OK = 1
        const val TYPE_CONFIRM = 2
    }

    private val dialog = Dialog(activity)

    var onOkClicked: (() -> Unit)? = null
    var onConfirmClicked: (() -> Unit)? = null
    var onCancelClicked: (() -> Unit)? = null

    fun dismiss(){
        SweetAlertDialog.NORMAL_TYPE
        dialog.dismiss()
    }

    fun show(title: String, message: String) {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(getDialogView())
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