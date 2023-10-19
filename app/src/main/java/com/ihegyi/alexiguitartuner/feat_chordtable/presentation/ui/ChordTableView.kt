package com.ihegyi.alexiguitartuner.feat_chordtable.presentation.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.ihegyi.alexiguitartuner.R
import com.ihegyi.alexiguitartuner.commons.domain.entities.Pitch
import dagger.hilt.android.AndroidEntryPoint
import java.util.TreeSet

@AndroidEntryPoint
class ChordTableView (context: Context?, attrs: AttributeSet?)
    : View(context, attrs) {
    companion object {
        private const val PITCH_R = 30
        private const val MARGIN = 50
        private const val FRET_NUM = 5
    }

    private var pitchPositions = emptyList<Int>().toMutableList()
    private var pitchNames = emptyList<String>().toMutableList()

    private var numberOfStrings = 0

    private var paintBackground: Paint = Paint()
    private var paintPitch: Paint = Paint()
    private var paintPitchOpenString: Paint = Paint()
    private var paintText: Paint = Paint()

    private var fretHeight : Float = 0f
    private var neckWidth : Float = 0f

    init {
        paintBackground.color = context!!.getColor(R.color.colorPrimary)
        paintBackground.style = Paint.Style.STROKE
        paintBackground.strokeWidth = 10f

        paintPitch.color = context.getColor(R.color.colorOnPrimary)
        paintPitch.style = Paint.Style.FILL_AND_STROKE
        paintPitch.strokeWidth = 5f

        paintPitchOpenString.color = context.getColor(R.color.colorOnPrimary)
        paintPitchOpenString.style = Paint.Style.STROKE
        paintPitchOpenString.strokeWidth = 5f
        
        paintText.color = context.getColor(R.color.colorOnPrimary)
        paintText.style = Paint.Style.FILL_AND_STROKE
        paintText.textSize = 70f
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawRect(0F,0F, width.toFloat(), height.toFloat(),paintBackground)
        if (pitchPositions.isNotEmpty()) {
            fretHeight = (4 * height / 5).toFloat()
            neckWidth = (4 * width / 5 - MARGIN).toFloat()
            drawGuitarNeck(canvas)
            drawPitches(canvas)
        }
    }
    private fun drawGuitarNeck(canvas: Canvas?) {

        val iterator = getFretNumberTreeSet().iterator()

        //horizontal lines
        for (i in 0 until numberOfStrings) {
            canvas?.drawLine(
                width.toFloat() / 5,
                MARGIN + i * fretHeight / numberOfStrings,
                width.toFloat() - MARGIN,
                MARGIN + i * fretHeight / numberOfStrings,
                paintBackground
            )
            canvas?.drawText(
                if(pitchNames.isNotEmpty()) pitchNames[i] else "",
                width.toFloat() / 20,
                MARGIN + i * fretHeight / numberOfStrings + paintText.textSize / 3,
                paintText
            )
        }

        //vertical lines
        for (i in 0 until FRET_NUM + 1) {
            canvas?.drawLine(
                (width.toFloat() / 5) + i * neckWidth / FRET_NUM,
                MARGIN.toFloat(),
                (width.toFloat() / 5) + i * neckWidth / FRET_NUM,
                MARGIN + (numberOfStrings - 1) * fretHeight / numberOfStrings,
                paintBackground
            )
            if(i < FRET_NUM) {
                val fret = if (iterator.hasNext()) iterator.next() else -1
                canvas?.drawText(
                    "$fret",
                    ((neckWidth / FRET_NUM / 2) - paintText.textSize / 3) +
                            (width / 5) +
                            i * (neckWidth / FRET_NUM),
                    9 * height.toFloat() / 10,
                    paintText
                )
            }
        }
    }

    private fun drawPitches(canvas:Canvas?) {
        var pitchX: Float
        var pitchY: Float

        for(i in pitchPositions.indices) {
            when (pitchPositions[i]) {
                -1 -> {
                    pitchX = width.toFloat() / 5
                    pitchY = MARGIN + i * fretHeight / numberOfStrings
                    canvas?.drawLine(
                        pitchX - PITCH_R.toFloat(),
                        pitchY - PITCH_R.toFloat(),
                        pitchX + PITCH_R.toFloat(),
                        pitchY + PITCH_R.toFloat(),
                        paintPitch
                    )
                    canvas?.drawLine(
                        pitchX + PITCH_R.toFloat(),
                        pitchY - PITCH_R.toFloat(),
                        pitchX - PITCH_R.toFloat(),
                        pitchY + PITCH_R.toFloat(),
                        paintPitch
                    )
                }
                0 -> {
                    pitchX = width.toFloat() / 5
                    pitchY = MARGIN + i * fretHeight / numberOfStrings
                    canvas?.drawCircle(
                        pitchX,
                        pitchY,
                        PITCH_R.toFloat(),
                        paintPitchOpenString
                    )
                }
                else -> {
                    pitchX = (width / 5) +
                             (neckWidth / FRET_NUM / 2) +
                             (pitchCorrector(i) - 1) * (neckWidth / FRET_NUM)
                    pitchY = MARGIN + i * fretHeight / numberOfStrings
                    canvas?.drawCircle(
                        pitchX,
                        pitchY,
                        PITCH_R.toFloat(),
                        paintPitch
                    )
                }
            }
        }
    }
    private fun getFretNumberTreeSet() : TreeSet<Int> {
        var treeSet : TreeSet<Int> = TreeSet(pitchPositions.toList())
        treeSet.remove(-1)
        treeSet.remove(0)

        if(treeSet.isEmpty() || treeSet.last() <= FRET_NUM)
            treeSet = TreeSet(listOf(1,2,3,4,5))
        else {
            val first = treeSet.first()
            treeSet.clear()
            for (i in 0 until FRET_NUM)
                treeSet.add(first + i)
        }
        return treeSet
    }

    private fun pitchCorrector(i : Int) = getFretNumberTreeSet().indexOf(pitchPositions[i]) + 1

    fun setPitchList(pitchNames: List<Pitch>, pitchPositions: List<Int>) {
        this.pitchNames.clear()
        pitchNames.sortedByDescending { it.frequency }.forEach { pitch ->
            this.pitchNames.add(pitch.name)
        }
        this.pitchPositions.clear()
        this.pitchPositions.addAll(pitchPositions)
        numberOfStrings = pitchNames.size

    }
}
