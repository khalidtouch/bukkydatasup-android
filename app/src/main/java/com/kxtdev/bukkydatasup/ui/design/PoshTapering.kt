package com.kxtdev.bukkydatasup.ui.design

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection

class PoshLateralTapering(private val depth: Float = 40f): Shape {

    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {

        val path = Path().apply {
            moveTo(0f, 0f)
            lineTo(size.width, 0f)

            quadraticBezierTo(
                size.width - depth,
                size.height / 2,
                size.width,
                size.height
            )

            lineTo(0f, size.height)

            quadraticBezierTo(
                depth,
                size.height / 2,
                0f,
                0f
            )

            close()
        }

        return Outline.Generic(path)

    }
}


class PoshSlopedRightCurveBottomTapering(private val depth: Float = 40f): Shape {

    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {

        val path = Path().apply {
            moveTo(0f, 0f)
            lineTo(size.width, 0f)

            cubicTo(
                size.width + 180f,
                size.height - depth,
                size.width - depth,
                size.height,
                0f,
                size.height
            )

            close()
        }

        return Outline.Generic(path)

    }
}