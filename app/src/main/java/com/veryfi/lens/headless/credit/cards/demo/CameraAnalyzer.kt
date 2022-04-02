package com.veryfi.lens.headless.credit.cards.demo

import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy

typealias AnalyzerListener = (byteArray: ByteArray, width: Int, height: Int) -> Unit

class CameraAnalyzer(private val listener: AnalyzerListener) : ImageAnalysis.Analyzer {

    override fun analyze(image: ImageProxy) {
        listener(ImageProxyUtils.toByteArray(image), image.width, image.height)
        image.close()
    }

}
