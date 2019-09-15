package com.nosetrap.alertdialog

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.nosetrap.alertlib.AlertDialog
import com.nosetrap.alertlib.AlertType
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnNormal.setOnClickListener {
           val builder =  AlertDialog.Builder(this)
            builder.contentText = "message"
            builder.titleText =  "testing title"
               builder.build().show()
        }

        btnOk.setOnClickListener {
            val builder =  AlertDialog.Builder(this,AlertType.TYPE_OK)
            builder.contentText = " Ok message"
            builder.titleText =  "testing Ok title"
            builder.okText = "No"
            builder.onOkClicked = { Log.d("AlertDialog test","On Ok Clicked")}
            builder.build().show()
        }

        btnConfirm.setOnClickListener {
            val builder =  AlertDialog.Builder(this,AlertType.TYPE_CONFIRM)
            builder.contentText = " Ok message"
            builder.titleText =  "testing Ok title"
            builder.onConfirmClicked = { Log.d("AlertDialog test","On Confirm Clicked")}
            builder.onCancelClicked = { Log.d("AlertDialog test","On Cancel Clicked")}
            builder.build().show()
        }
    }
}
