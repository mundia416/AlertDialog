package com.nosetrap.alertlib

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.DisplayMetrics
import android.view.*
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
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
class AlertDialog private constructor(context: Context) {

    val dialog = Dialog(context, R.style.AlertDialog)

    fun dismiss() {
        dialog.dismiss()
    }

    fun show() {
        dialog.show()
    }


    /**
     * use the builder class to create an alert dialog
     */
    class Builder(private val context: Context, private val alertType: AlertType = AlertType.TYPE_NORMAL) {

        var onOkClicked: (() -> Unit)? = null
        var onConfirmClicked: (() -> Unit)? = null
        var onCancelClicked: (() -> Unit)? = null

        var titleText: String = ""
        var contentText: String = ""

         var okText: String =  context.getString(R.string.ok)
        var confirmText: String = context.getString(R.string.confirm)
         var cancelText: String = context.getString(R.string.cancel)


        @LayoutRes
        private var view: Int = R.layout.body_view

        @ColorRes
        private var lineColor: Int = R.color.sky_blue

        private val layoutInflater = LayoutInflater.from(context)
        private val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager

        private val alertDialog = AlertDialog(context)

        private var hasSetCustomView = false

        fun setCustomView(@LayoutRes view: Int) {
            hasSetCustomView = true
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
            windowManager.defaultDisplay.getMetrics(dm)
            val lp = WindowManager.LayoutParams()
            val window = alertDialog.dialog.window
            window!!.setGravity(Gravity.CENTER)
            lp.copyFrom(window.attributes)

            //This makes the dialog take up the full width
            lp.width = WindowManager.LayoutParams.MATCH_PARENT
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT
            lp.windowAnimations = R.style.DialogAnimation
            window.attributes = lp

            if(!hasSetCustomView) {
                alertDialog.dialog.findViewById<TextView>(R.id.tvTitle).text = titleText
                alertDialog.dialog.findViewById<TextView>(R.id.tvMessage).text = contentText
            }

            setOnClickListeners()

            return alertDialog
        }

        private fun setAlertView() {
            alertDialog.dialog.setContentView(R.layout.alert_view)

            val addView = layoutInflater.inflate(view, null, false)
            alertDialog.dialog.findViewById<FrameLayout>(R.id.contentContainer).addView(addView)

            when (alertType) {
                AlertType.TYPE_OK -> {
                    alertDialog.dialog.findViewById<FrameLayout>(R.id.buttonsContainer).addView(
                        layoutInflater.inflate(R.layout.ok_alert_view, null, false)
                    )
                }
                AlertType.TYPE_CONFIRM -> {
                    alertDialog.dialog.findViewById<FrameLayout>(R.id.buttonsContainer).addView(
                        layoutInflater.inflate(
                            R.layout.confirm_alert_view,
                            null,
                            false
                        )
                    )
                }
                AlertType.TYPE_NORMAL -> {
                    alertDialog.dialog.findViewById<View>(R.id.horizontalLine).visibility = View.GONE
                }
            }

            alertDialog.dialog.findViewById<View>(R.id.horizontalLine).setBackgroundColor(ContextCompat.getColor(context, lineColor))

        }

        private fun setOnClickListeners() {
            when (alertType) {
                AlertType.TYPE_OK -> {
                    alertDialog.dialog.findViewById<Button>(R.id.btnOk).setText(okText)
                    alertDialog.dialog.findViewById<Button>(R.id.btnOk).setOnClickListener {
                        alertDialog.dialog.dismiss()
                        onOkClicked?.invoke()
                    }
                }
                AlertType.TYPE_CONFIRM -> {
                    alertDialog.dialog.findViewById<Button>(R.id.btnConfirm).setText(confirmText)
                    alertDialog.dialog.findViewById<Button>(R.id.btnCancel).setText(cancelText)
                    alertDialog.dialog.findViewById<Button>(R.id.btnConfirm).setOnClickListener {
                        alertDialog.dialog.dismiss()
                        onConfirmClicked?.invoke()
                    }
                    alertDialog.dialog.findViewById<Button>(R.id.btnCancel).setOnClickListener {
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