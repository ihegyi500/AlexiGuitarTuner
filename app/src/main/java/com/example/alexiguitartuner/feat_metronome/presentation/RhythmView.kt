package com.example.alexiguitartuner.feat_metronome.presentation

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.text.TextPaint
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.*
import com.example.alexiguitartuner.R
import com.example.alexiguitartuner.feat_metronome.data.MetronomeRepository
import com.example.alexiguitartuner.feat_metronome.domain.Tone
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@AndroidEntryPoint
class RhythmView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    @Inject
    lateinit var metronomeRepository : MetronomeRepository

    companion object {
        private const val MARGIN = 15f
    }

    private var paintBackground:Paint = Paint()
    private var paintDefaultNote:Paint = Paint()
    private var paintCurrentNote:Paint = Paint()

    init {
        paintBackground.color = Color.DKGRAY
        paintBackground.style = Paint.Style.STROKE
        paintBackground.strokeWidth = 10f

        paintDefaultNote.color = Color.argb(100,55,0,179)//Color.GRAY
        paintDefaultNote.style = Paint.Style.STROKE
        paintDefaultNote.strokeWidth = 5f

        paintCurrentNote.color = Color.argb(100,55,0,179)//Color.GRAY
        paintCurrentNote.style = Paint.Style.FILL
        paintCurrentNote.strokeWidth = 5f

        CoroutineScope(Dispatchers.Main).launch {
            metronomeRepository.rhythmListIterator.collect {
                invalidate()
            }
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawRect(0f,0f,width.toFloat(),height.toFloat(),paintBackground)
        drawNotes(canvas)
        Log.d("try","${metronomeRepository.rhythmListIterator.value}")
    }

    private fun drawNotes(canvas: Canvas) {
        Log.d("custom_view","drawNotes called")
        val numberOfNotes = metronomeRepository.rhythmList.value.size
        val currentNote = if(metronomeRepository.rhythmListIterator.value == 0) numberOfNotes - 1 else metronomeRepository.rhythmListIterator.value - 1
        val noteHeight = (height / 2).toFloat()
        val noteWidth = (width - (numberOfNotes + 1 ) * MARGIN) / numberOfNotes

        var usedPaint : Paint

        for (i in 0 until numberOfNotes){
            usedPaint = if(i == currentNote && metronomeRepository.getIsPlaying()) paintCurrentNote else paintDefaultNote
            if (metronomeRepository.rhythmList.value[i] != Tone.SILENT){
                if (metronomeRepository.rhythmList.value[i] == Tone.LOUDER){
                    canvas.drawRect(
                        (i + 1) * MARGIN + (if (i > 0) i * noteWidth else 0f),
                        MARGIN,
                        (i + 1) * noteWidth + (i + 1) * MARGIN,
                        noteHeight,
                        usedPaint
                    )
                }
                canvas.drawRect(
                    (i + 1) * MARGIN + (if (i > 0) i * noteWidth else 0f),
                    noteHeight,
                    (i + 1) * noteWidth + (i + 1) * MARGIN,
                    height - MARGIN,
                    usedPaint
                )
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if(event.action == MotionEvent.ACTION_DOWN){
            val tX = event.x.toInt() / (width / metronomeRepository.rhythmList.value.size)
            //val tY = event.y.toInt() / (height / 2)
            metronomeRepository.setToneByIndex(tX)
            invalidate()
        }
        return true
    }

}