package com.example.threadhomework

import android.os.*
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var downLoadWorkerHandlerThread: HandlerThread
    private lateinit var downLoadWorkerThreadHandler: Handler

    private var handlerOfMain = object : Handler(Looper.getMainLooper()){
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            download_result.text = msg.obj.toString()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        startDownLoadWorker()
        bindClick()
    }



    private fun bindClick(){
        button_restart.setOnClickListener {
            if(!downLoadWorkerHandlerThread.isAlive){
                startDownLoadWorker();
            }
        }

        button_stop.setOnClickListener {
            downLoadWorkerHandlerThread.quitSafely()
        }

        button_download.setOnClickListener {
            if(downLoadWorkerHandlerThread.isAlive){
                val msg = Message.obtain()
                msg.obj = arrayOf("url_of_Image0", "url_of_Image1", "url_of_Image2", "url_of_Image3", "url_of_Image4")
                downLoadWorkerThreadHandler.sendMessage(msg)
            }
        }
    }

    private fun startDownLoadWorker(){
        downLoadWorkerHandlerThread = HandlerThread("downLoadWorker")
        downLoadWorkerHandlerThread.start()

        downLoadWorkerThreadHandler = object : Handler(downLoadWorkerHandlerThread.looper){
            override fun handleMessage(msg: Message) {
                val urlList = msg.obj as Array<String>
                urlList.forEach {
                    val resultMsg = Message.obtain()
                    resultMsg.obj = "$it finished"
                    handlerOfMain.sendMessage(resultMsg)

                    Thread.sleep(1000)
                }
            }
        }
    }
}
