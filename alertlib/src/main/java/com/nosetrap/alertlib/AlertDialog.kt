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
import androidx.annotation.ColorRes
import androidx.annotation.LayoutRes
import kotlinx.android.synthetic.main.ok_alert_view.*
import kotlinx.android.synthetic.main.alert_view.*
import kotlinx.android.synthetic.main.body_view.*
import kotlinx.android.synthetic.main.confirm_alert_view.*

class AlertDialog(private val activity: Activity,private val alertType: Int = TYPE_NORMAL) {

    companion object{

        /**
         * the type of alert dialog that is used
         */
        const val TYPE_NORMAL = 0
        const val TYPE_OK = 1
        const val TYPE_CONFIRM = 2
    }


    private val dialog = Dialog(activity)

    var onOkClicked: (() -> Unit)? = null
    var onConfirmClicked: (() -> Unit)? = null
    var onCancelClicked: (() -> Unit)? = null

    var titleText : String = ""
    var contentText: String = ""

    @LayoutRes private var view: Int = R.layout.body_view

    @ColorRes private var lineColor: Int = R.color.sky_blue

    private val layoutInflater = activity.layoutInflater

    fun setCustomView(@LayoutRes view: Int){
        this.view = view
    }



    fun dismiss(){
        dialog.dismiss()
    }

    fun show() {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        setAlertView()

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

        dialog.tvTitle.text = titleText
        dialog.tvMessage.text = contentText

        setOnClickListeners()

        dialog.show()
    }

    private fun setAlertView(){
        dialog.setContentView(R.layout.alert_view)

        val addView = layoutInflater.inflate(view,null,false)
        dialog.contentContainer.addView(addView)

        when(alertType){
            TYPE_OK -> {
                dialog.buttonsContainer.addView(layoutInflater.inflate(R.layout.ok_alert_view,null,false))
            }
            TYPE_CONFIRM -> {
                dialog.buttonsContainer.addView(layoutInflater.inflate(R.layout.confirm_alert_view,null,false))
            }
            TYPE_NORMAL -> {
                dialog.horizontalLine.visibility = View.GONE
            }
        }

        dialog.horizontalLine.setBackgroundColor(lineColor)

    }

    private fun setOnClickListeners(){
        when(alertType){
            TYPE_OK -> {
                dialog.btnOk.setOnClickListener {
                    dialog.dismiss()
                    onOkClicked?.invoke()
                }
            }
            TYPE_CONFIRM ->{
                dialog.btnConfirm.setOnClickListener {
                    dialog.dismiss()
                    onConfirmClicked?.invoke()
                }
                dialog.btnCancel.setOnClickListener {
                    dialog.dismiss()
                    onCancelClicked?.invoke()
                }
            }
        }
    }

    fun setLineColor(@ColorRes color: Int){
        this.lineColor = color
    }
}