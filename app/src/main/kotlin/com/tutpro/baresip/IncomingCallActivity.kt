package com.tutpro.baresip

import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.WindowManager
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView

class IncomingCallActivity : Activity() {
    private var callp: Long = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        callp = intent.getLongExtra("callp", 0L)
        val caller = intent.getStringExtra("caller").orEmpty().ifBlank { "Unknown caller" }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true)
            setTurnScreenOn(true)
        }
        window.addFlags(
            WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON or
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
        )

        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setPadding(48, 48, 48, 48)
        }
        root.addView(TextView(this).apply {
            text = "BD PBX"
            textSize = 30f
            gravity = Gravity.CENTER
        }, LinearLayout.LayoutParams(-1, -2))
        root.addView(TextView(this).apply {
            text = caller
            textSize = 24f
            gravity = Gravity.CENTER
            setPadding(0, 24, 0, 48)
        }, LinearLayout.LayoutParams(-1, -2))
        root.addView(Button(this).apply {
            text = "Answer"
            textSize = 18f
            setOnClickListener { answerCall() }
        }, LinearLayout.LayoutParams(-1, -2))
        root.addView(Button(this).apply {
            text = "Decline"
            textSize = 18f
            setOnClickListener { rejectCall() }
        }, LinearLayout.LayoutParams(-1, -2).apply { topMargin = 12 })
        setContentView(root)
    }

    private fun answerCall() {
        IncomingCallNotifier.cancel(this)
        ConnectionService.connections[callp]?.onAnswer()
        finish()
    }

    private fun rejectCall() {
        IncomingCallNotifier.cancel(this)
        ConnectionService.connections[callp]?.onReject()
        finish()
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        rejectCall()
    }
}
