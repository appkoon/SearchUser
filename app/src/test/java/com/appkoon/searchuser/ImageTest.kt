package com.appkoon.searchuser

import com.appkoon.searchuser.common.getResizedHeight
import org.junit.Test
import kotlin.test.assertEquals

/**
 * Created by seongheonson on 2018. 10. 16..
 */

class ImageTest {

    @Test
    fun testImageSize(){
        val imageWidth = 300
        val imageHeight = 700
        val displayWidth = 1080

        val originRate = imageWidth.toFloat() / imageHeight.toFloat()
        val resizeRate = displayWidth.toFloat() / getResizedHeight(imageWidth, imageHeight, displayWidth)

        assertEquals(originRate ,resizeRate)
    }

}