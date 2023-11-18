package com.ihegyi.alexiguitartuner.feat_metronome.presentation.ui

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.lifecycle.*
import com.ihegyi.alexiguitartuner.R
import com.ihegyi.alexiguitartuner.feat_metronome.domain.Beat
import com.ihegyi.alexiguitartuner.feat_metronome.domain.MetronomeState
import com.ihegyi.alexiguitartuner.feat_metronome.presentation.viewmodel.MetronomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

@SuppressLint("ResourceType")
@AndroidEntryPoint
class RhythmView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    private val viewModel by lazy {
        ViewModelProvider(findViewTreeViewModelStoreOwner()!!).get<MetronomeViewModel>()
    }

    private var rhythmViewState : MetronomeState = MetronomeState.initial_state

    companion object {
        private const val MARGIN = 15f
    }

    private var paintBackground:Paint = Paint()
    private var paintDefaultNote:Paint = Paint()
    private var paintCurrentNote:Paint = Paint()

    init {
        paintBackground.color = context!!.getColor(R.color.colorPrimary)
        paintBackground.style = Paint.Style.STROKE
        paintBackground.strokeWidth = 10f

        paintDefaultNote.color = context.getColor(R.color.colorPrimary)
        paintDefaultNote.style = Paint.Style.STROKE
        paintDefaultNote.strokeWidth = 5f

        paintCurrentNote.color = context.getColor(R.color.colorOnPrimary)
        paintCurrentNote.style = Paint.Style.FILL
        paintCurrentNote.strokeWidth = 5f
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawRect(0f,0f,width.toFloat(),height.toFloat(),paintBackground)
        drawNotes(canvas)
    }

    private fun drawNotes(canvas: Canvas) {
        val numberOfNotes = rhythmViewState.beatList.size
        val currentNote = if(rhythmViewState.beatListIterator == 0) numberOfNotes - 1 else rhythmViewState.beatListIterator - 1
        val noteHeight = (height / 2).toFloat()
        val noteWidth = (width - (numberOfNotes + 1 ) * MARGIN) / numberOfNotes

        var usedPaint : Paint

        for (i in 0 until numberOfNotes){
            usedPaint = if(i == currentNote && rhythmViewState.isPlaying) paintCurrentNote else paintDefaultNote
            if (rhythmViewState.beatList[i] != Beat.SILENT){
                if (rhythmViewState.beatList[i] == Beat.LOUDER){
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
            val tX = event.x.toInt() / (width / rhythmViewState.beatList.size)
            //val tY = event.y.toInt() / (height / 2)
            viewModel.setToneByIndex(tX)
            invalidate()
        }
        return true
    }

    fun setRhythmViewState(state: MetronomeState){
        rhythmViewState = state
    }
}