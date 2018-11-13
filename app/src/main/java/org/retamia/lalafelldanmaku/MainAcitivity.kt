package org.retamia.lalafelldanmaku

import android.os.Bundle
import android.support.v7.app.AppCompatActivity;
import android.view.View


import kotlinx.android.synthetic.main.activity_main.*
import org.retamia.lalafell.danmaku.parser.TextParser


class MainAcitivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        send_btn.setOnClickListener(this::btnClickListener)
        danmaku_view.start()
    }


    private fun btnClickListener(view: View) {
        danmaku_view.shootDanmaku(danmaku_et.text.toString(), TextParser())
    }

}
