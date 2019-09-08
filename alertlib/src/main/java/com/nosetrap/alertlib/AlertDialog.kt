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
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.ok_alert_view.*
import kotlinx.android.synthetic.main.alert_view.*
import kotlinx.android.synthetic.main.body_view.*
import kotlinx.android.synthetic.main.confirm_alert_view.*

/**
 *
 *  make sure to call [build] before calling [show]
*
 */
class AlertDialog private constructor(private val activity: Activity) {

    val dialog = Dialog(activity)

    fun dismiss() {
        dialog.dismiss()
    }

    fun show() {
        dialog.show()
    }


    /**
     * use the builder class to create an alert dialog
     */
    class Builder(private val activity: Activity, private val alertType: AlertType = AlertType.TYPE_NORMAL) {

        var onOkClicked: (() -> Unit)? = null
        var onConfirmClicked: (() -> Unit)? = null
        var onCancelClicked: (() -> Unit)? = null

        var titleText: String = ""
        var contentText: String = ""

        @LayoutRes
        private var view: Int = R.layout.body_view

        @ColorRes
        private var lineColor: Int = R.color.sky_blue

        private val layoutInflater = activity.layoutInflater

        private val alertDialog = AlertDialog(activity)

        fun setCustomView(@LayoutRes view: Int) {
            this.view = view
        }

        /**
         *  call [build] before calling [show]
         */
        fun build(): AlertDialog {
            alertDialog.dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            alertDialog.dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            setAlertView()

            val dm = DisplayMetrics()
            activity.windowManager.defaultDisplay.getMetrics(dm)
            val lp = WindowManager.LayoutParams()
            val window = alertDialog.dialog.window
            window!!.setGravity(Gravity.CENTER)
            lp.copyFrom(window.attributes)

            //This makes the dialog take up the full width
            lp.width = WindowManager.LayoutParams.MATCH_PARENT
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT
            lp.windowAnimations = R.style.DialogAnimation
            window.attributes = lp

            alertDialog.dialog.tvTitle.text = titleText
            alertDialog.dialog.tvMessage.text = contentText

            setOnClickListeners()

            return alertDialog
        }

        private fun setAlertView() {
            alertDialog.dialog.setContentView(R.layout.alert_view)

            val addView = layoutInflater.inflate(view, null, false)
            alertDialog.dialog.contentContainer.addView(addView)

            when (alertType) {
                AlertType.TYPE_OK -> {
                    alertDialog.dialog.buttonsContainer.addView(
                        layoutInflater.inflate(
                            R.layout.ok_alert_view,
                            null,
                            false
                        )
                    )
                }
                AlertType.TYPE_CONFIRM -> {
                    alertDialog.dialog.buttonsContainer.addView(
                        layoutInflater.inflate(
                            R.layout.confirm_alert_view,
                            null,
                            false
                        )
                    )
                }
                AlertType.TYPE_NORMAL -> {
                    alertDialog.dialog.horizontalLine.visibility = View.GONE
                }
            }

            alertDialog.dialog.horizontalLine.setBackgroundColor(ContextCompat.getColor(activity!!, lineColor))

        }

        private fun setOnClickListeners() {
            when (alertType) {
                AlertType.TYPE_OK -> {
                    alertDialog.dialog.btnOk.setOnClickListener {
                        alertDialog.dialog.dismiss()
                        onOkClicked?.invoke()
                    }
                }
                AlertType.TYPE_CONFIRM -> {
                    alertDialog.dialog.btnConfirm.setOnClickListener {
                        alertDialog.dialog.dismiss()
                        onConfirmClicked?.invoke()
                    }
                    alertDialog.dialog.btnCancel.setOnClickListener {
                        alertDialog.dialog.dismiss()
                        onCancelClicked?.invoke()
                    }
                }
            }
        }

        fun setLineColor(@ColorRes color: Int) {
            this.lineColor = color
        }
    }
}