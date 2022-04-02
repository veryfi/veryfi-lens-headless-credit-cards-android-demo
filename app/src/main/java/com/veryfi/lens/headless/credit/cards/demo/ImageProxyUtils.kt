package com.veryfi.lens.headless.credit.cards.demo

import androidx.camera.core.ImageProxy
import kotlin.math.min

object ImageProxyUtils {

    fun toByteArray(image: ImageProxy): ByteArray {
        val yPlane = image.planes[0]
        val uPlane = image.planes[1]
        val vPlane = image.planes[2]
        val yBuffer = yPlane.buffer
        val uBuffer = uPlane.buffer
        val vBuffer = vPlane.buffer
        yBuffer.rewind()
        uBuffer.rewind()
        vBuffer.rewind()
        val ySize = yBuffer.remaining()
        var position = 0
        val nv21 = ByteArray(ySize + image.width * image.height / 2)
        for (row in 0 until image.height) {
            yBuffer[nv21, position, image.width]
            position += image.width
            yBuffer.position(
                min(ySize, yBuffer.position() - image.width + yPlane.rowStride)
            )
        }
        val chromaHeight = image.height / 2
        val chromaWidth = image.width / 2
        val vRowStride = vPlane.rowStride
        val uRowStride = uPlane.rowStride
        val vPixelStride = vPlane.pixelStride
        val uPixelStride = uPlane.pixelStride
        val vLineBuffer = ByteArray(vRowStride)
        val uLineBuffer = ByteArray(uRowStride)
        for (row in 0 until chromaHeight) {
            vBuffer[vLineBuffer, 0, min(vRowStride, vBuffer.remaining())]
            uBuffer[uLineBuffer, 0, min(uRowStride, uBuffer.remaining())]
            var vLineBufferPosition = 0
            var uLineBufferPosition = 0
            for (col in 0 until chromaWidth) {
                nv21[position++] = vLineBuffer[vLineBufferPosition]
                nv21[position++] = uLineBuffer[uLineBufferPosition]
                vLineBufferPosition += vPixelStride
                uLineBufferPosition += uPixelStride
            }
        }
        return nv21
    }
}