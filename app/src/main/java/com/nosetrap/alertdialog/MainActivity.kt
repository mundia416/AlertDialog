package com.nosetrap.alertdialog

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.nosetrap.alertlib.AlertDialog
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnNormal.setOnClickListener {
            AlertDialog(this).show("testing title","message")
        }

        btnOk.setOnClickListener {
            val dialog = AlertDialog(this,AlertDialog.TYPE_OK)
            dialog.onOkClicked = { Log.d("AlertDialog test","On Ok Clicked")}
            dialog.show("testing Ok title"," Ok message")
        }

        btnConfirm.setOnClickListener {
            val dialog = AlertDialog(this,AlertDialog.TYPE_CONFIRM)
            dialog.onConfirmClicked = { Log.d("AlertDialog test","On Confirm Clicked")}
            dialog.onCancelClicked = { Log.d("AlertDialog test","On Cancel Clicked")}
            dialog.show("testing confirm title","confirm message")
        }
    }
}
