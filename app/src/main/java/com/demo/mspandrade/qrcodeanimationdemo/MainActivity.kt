package com.demo.mspandrade.qrcodeanimationdemo

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.graphics.PointF
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.view.ViewTreeObserver
import android.view.animation.AccelerateDecelerateInterpolator
import com.dlazaro66.qrcodereaderview.QRCodeReaderView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), QRCodeReaderView.OnQRCodeReadListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ActivityCompat.requestPermissions(this,  arrayOf(android.Manifest.permission.CAMERA), 200)

        initScannerBarAnimation()

        qrDecoderView.setOnQRCodeReadListener(this)
        qrDecoderView.setQRDecodingEnabled(true)
        qrDecoderView.setAutofocusInterval(2000L)
        qrDecoderView.setTorchEnabled(true)
        qrDecoderView.setBackCamera()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onQRCodeRead(text: String?, points: Array<out PointF>?) {
        finish()
    }

    override fun onResume() {
        super.onResume()
        qrDecoderView.startCamera()
    }

    override fun onPause() {
        super.onPause()
        qrDecoderView.stopCamera()
    }

    override fun onDestroy() {
        super.onDestroy()
        qrDecoderView.stopCamera()
    }

    private fun initScannerBarAnimation() {

        val vto = scannerLayout.viewTreeObserver
        vto.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                scannerLayout.viewTreeObserver.removeGlobalOnLayoutListener(this)
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    scannerLayout.viewTreeObserver.removeGlobalOnLayoutListener(this)
                } else {
                    scannerLayout.viewTreeObserver.removeOnGlobalLayoutListener(this)
                }

                val destination = scannerBar.y + scannerLayout.height - scannerBar.height

                val animator = ObjectAnimator.ofFloat(scannerBar, "translationY", scannerBar.y, destination)

                animator.repeatMode = ValueAnimator.REVERSE
                animator.repeatCount = ValueAnimator.INFINITE
                animator.interpolator = AccelerateDecelerateInterpolator()
                animator.duration = 1000
                animator.start()
            }
        })
    }
}
