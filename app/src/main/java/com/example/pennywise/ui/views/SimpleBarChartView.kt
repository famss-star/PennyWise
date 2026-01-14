package com.example.pennywise.ui.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import com.example.pennywise.R

class SimpleBarChartView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val barPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = context.getColor(R.color.primary)
        style = Paint.Style.FILL
    }

    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = context.getColor(android.R.color.darker_gray)
        textSize = 24f
        textAlign = Paint.Align.CENTER
    }

    private val labelPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = context.getColor(android.R.color.darker_gray)
        textSize = 28f
        textAlign = Paint.Align.CENTER
    }

    private var chartData: List<Pair<String, Double>> = emptyList()
    private var maxValue: Double = 0.0

    fun setData(data: List<Pair<String, Double>>) {
        chartData = data
        maxValue = data.maxOfOrNull { it.second } ?: 0.0
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (chartData.isEmpty()) {
            // Draw empty state
            textPaint.textSize = 40f
            canvas.drawText(
                "Belum ada data",
                width / 2f,
                height / 2f,
                textPaint
            )
            return
        }

        val barCount = chartData.size
        val spacing = 40f
        val totalSpacing = spacing * (barCount + 1)
        val barWidth = (width - totalSpacing) / barCount
        val chartHeight = height - 120f // Space for labels at bottom

        chartData.forEachIndexed { index, (label, value) ->
            val barHeight = if (maxValue > 0) {
                ((value / maxValue) * chartHeight * 0.8).toFloat()
            } else {
                0f
            }

            val left = spacing + (barWidth + spacing) * index
            val top = chartHeight - barHeight
            val right = left + barWidth
            val bottom = chartHeight

            // Draw bar
            val rect = RectF(left, top, right, bottom)
            canvas.drawRoundRect(rect, 12f, 12f, barPaint)

            // Draw value on top of bar
            if (value > 0) {
                val valueText = formatValue(value)
                canvas.drawText(
                    valueText,
                    left + barWidth / 2,
                    top - 10f,
                    textPaint
                )
            }

            // Draw label
            canvas.drawText(
                label,
                left + barWidth / 2,
                height - 30f,
                labelPaint
            )
        }
    }

    private fun formatValue(value: Double): String {
        return when {
            value >= 1_000_000 -> String.format("%.1fM", value / 1_000_000)
            value >= 1_000 -> String.format("%.0fk", value / 1_000)
            else -> value.toInt().toString()
        }
    }
}
