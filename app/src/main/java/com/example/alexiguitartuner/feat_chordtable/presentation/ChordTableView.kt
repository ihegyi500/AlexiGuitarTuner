package com.example.alexiguitartuner.feat_chordtable.presentation

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ChordTableView (context: Context?, attrs: AttributeSet?)
    : View(context, attrs) {

    companion object {
        private const val MARGIN = 30
        private const val STRINGNUM = 6
    }

    private var paintBackground: Paint = Paint()
    private var paintPitch: Paint = Paint()
    private var paintPitchOpenString: Paint = Paint()
    private var paintText: Paint = Paint()

    private val chordTableViewModel by lazy {
        ViewModelProvider(
            findViewTreeViewModelStoreOwner()!!)[ChordTableViewModel::class.java]
    }

    init {
        paintBackground.color = Color.BLACK
        paintBackground.style = Paint.Style.STROKE
        paintBackground.strokeWidth = 10f

        paintPitch.color = Color.argb(100,55,0,179)//Color.GRAY
        paintPitch.style = Paint.Style.FILL_AND_STROKE
        paintPitch.strokeWidth = 5f

        paintPitchOpenString.color = Color.argb(100,55,0,179)//Color.GRAY
        paintPitchOpenString.style = Paint.Style.STROKE
        paintPitchOpenString.strokeWidth = 5f
        
        paintText.color = Color.BLACK
        paintText.style = Paint.Style.FILL_AND_STROKE
        paintText.textSize = 50f
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawRect(0F,0F, width.toFloat(), height.toFloat(),paintBackground)
        drawGuitarNeck(canvas)
        drawPitches(canvas)
    }

    private fun drawGuitarNeck(canvas: Canvas?) {
        //horizontal lines
        for(i in MARGIN until canvas?.height!! - MARGIN step (canvas.height/ STRINGNUM)) {
            canvas.drawText(
                "E0",
                0f,
                i.toFloat(),
                paintText
            )
            canvas.drawLine(
                canvas.width / 5f,
                i.toFloat(),
                canvas.width.toFloat(),
                i.toFloat(),
                paintBackground
            )
        }

        //vertical lines
        for(i in (canvas.width / 5) until canvas.width step (canvas.width / 5)) {
            canvas.drawLine(
                i.toFloat(),
                MARGIN.toFloat(),
                i.toFloat(),
                (MARGIN + ((STRINGNUM - 1) * canvas.height / STRINGNUM)).toFloat(),
                paintBackground
            )
        }
    }

    private fun drawPitches(canvas:Canvas?) {
        val placeholderList = listOf(1, 1, 2, 3, 3, 1)
        var pitchX: Float
        var pitchY: Float

        for(i in placeholderList.indices) {
            when (placeholderList[i]) {
                -1 -> {
                    pitchX = (canvas?.width!! / (STRINGNUM - 1)).toFloat()
                    pitchY = i * (canvas.height / STRINGNUM).toFloat() + MARGIN
                    canvas.drawLine(
                        pitchX - MARGIN.toFloat(),
                        pitchY - MARGIN.toFloat(),
                        pitchX + MARGIN.toFloat(),
                        pitchY + MARGIN.toFloat(),
                        paintPitch
                    )
                    canvas.drawLine(
                        pitchX + MARGIN.toFloat(),
                        pitchY - MARGIN.toFloat(),
                        pitchX - MARGIN.toFloat(),
                        pitchY + MARGIN.toFloat(),
                        paintPitch
                    )
                }
                0 -> {
                    pitchX = (canvas?.width!! / (STRINGNUM - 1)).toFloat()
                    pitchY = i * (canvas.height / STRINGNUM).toFloat() + MARGIN
                    canvas.drawCircle(
                        pitchX,
                        pitchY,
                        MARGIN.toFloat(),
                        paintPitchOpenString
                    )
                }
                else -> {
                    pitchX = placeholderList[i] *
                            (canvas?.width!! / (STRINGNUM - 1)).toFloat() +
                            ((canvas.width / (STRINGNUM - 1)).toFloat() / 2)
                    pitchY = i *
                            (canvas.height / STRINGNUM).toFloat() +
                            MARGIN
                    canvas.drawCircle(
                        pitchX,
                        pitchY,
                        MARGIN.toFloat(),
                        paintPitch
                    )
                }
            }
        }
    }
}
