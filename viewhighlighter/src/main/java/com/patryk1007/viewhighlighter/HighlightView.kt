package com.patryk1007.viewhighlighter

import android.content.Context
import android.graphics.*
import android.os.Build
import android.view.View

class HighlightView : View {

    private val defaultColor = Color.parseColor("#A5000000")
    private val maxAlphaLevel = 255

    /**
     * List keeps reference to all highlighted views
     */
    private var highlightedViews: List<HighlightedView> = arrayListOf()
    /**
     * Set whether this highlighter ignores transparent pixels
     */
    var fillTransparentPixels = false

    private var path: Path? = null
    private var labels = arrayListOf<LabelWithScreenPosition>()
    private val paint = Paint()
    private var alphaLevel = 125


    constructor(context: Context?, color: Int, alphaLevel: Float) : super(context) {
        setColor(color)
        setAlphaLevel(alphaLevel)
    }

    fun setHighlightingViewWithLabels(views: List<HighlightedView>) {
        highlightedViews = views
        notifyViewSetChanged()
    }

    fun setHighlightingView(views: List<View>) {
        val newHighLighted = arrayListOf<HighlightedView>()
        views.forEach {
            newHighLighted.add(HighlightedView(it, arrayListOf()))
        }
        highlightedViews = newHighLighted
        notifyViewSetChanged()
    }

    /**
     * ViewHighlighter background color
     */
    fun setColor(color: Int) {
        paint.color = color
    }

    /**
     * Sets view alpha fill level to a value from 0 to 1, where 0 means the only completely transparent color is filled  and 1 means even solid color is filled.
     * Default value is 0.5
     */
    fun setAlphaLevel(alpha: Float) {
        alphaLevel = (maxAlphaLevel * alpha).toInt()
        alphaLevel = Math.max(Math.min(alphaLevel, maxAlphaLevel), 0)
    }

    /**
     * Notifies the view that highlighted views have changed and the view should refresh itself
     */
    fun notifyViewSetChanged() {
        path = null
        labels = arrayListOf()
        invalidate()
    }

    override fun draw(canvas: Canvas?) {
        val width = this.width
        val height = this.height

        prepareViewsPath()
        drawPaths(canvas)

        canvas?.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)
        super.draw(canvas)
    }

    private fun prepareViewsPath() {
        if (path == null) {
            path = Path()
            path?.let {
                highlightedViews.forEach { highlightedView ->
                    val view = highlightedView.view
                    if (view.height > 0 && view.width > 0) {
                        prepareLabelsView(highlightedView)
                        val viewPath =
                            if (fillTransparentPixels) preparePathForViewWithAlpha(view) else preparePathForViewNoAlpha(
                                view
                            )
                        it.addPath(viewPath)
                    }
                }
            }
        }
    }

    private fun drawPaths(canvas: Canvas?) {
        path?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                canvas?.clipOutPath(it)
            } else {
                canvas?.clipPath(it, Region.Op.DIFFERENCE)
            }
        }
    }

    private fun prepareLabelsView(highlightedView: HighlightedView): ArrayList<LabelWithScreenPosition> {
        val labels = arrayListOf<LabelWithScreenPosition>()
        val view = highlightedView.view
        val viewPosition = getViewPosition(view)
        highlightedView.labels.forEach {
            when (it.position) {
                LabelPosition.Top -> {
                    labels.add(LabelWithScreenPosition(it.label, Point(0, viewPosition.y - it.label.height)))
                }
                LabelPosition.Bottom -> {
                    labels.add(LabelWithScreenPosition(it.label, Point(0, viewPosition.y + view.height)))
                }
                LabelPosition.Start -> {
                    labels.add(LabelWithScreenPosition(it.label, Point(viewPosition.x - it.label.width, 0)))
                }
                LabelPosition.End -> {
                    labels.add(LabelWithScreenPosition(it.label, Point(viewPosition.x + view.height, 0)))
                }
            }
        }
        return labels
    }

    private fun preparePathForViewNoAlpha(view: View): Path {
        val path = Path()
        val viewPosition = getViewPosition(view)

        path.addRect(
            (viewPosition.x).toFloat(),
            (viewPosition.y).toFloat(),
            (viewPosition.x + view.width).toFloat(),
            (viewPosition.y + view.height).toFloat(),
            Path.Direction.CCW
        )
        return path
    }

    private fun preparePathForViewWithAlpha(view: View): Path {
        val path = Path()
        val viewPosition = getViewPosition(view)

        val viewData = loadPixelDataFromView(view)
        val viewWidth = view.width
        val viewHeight = view.height

        var startLine: Int
        var lastState: Boolean
        var state: Boolean

        for (y in 0 until viewHeight) {
            lastState = false
            startLine = -1
            for (x in 0 until viewWidth) {
                state = Color.alpha(viewData[x + viewWidth * y]) > alphaLevel
                if (lastState != state) {
                    if (state) {
                        startLine = x
                    } else {
                        if (startLine != -1) {
                            addRectTopPath(path, viewPosition, startLine, x, y)
                            startLine = -1
                        }
                    }
                    lastState = state
                }
            }
            if (startLine != -1) {
                addRectTopPath(path, viewPosition, startLine, viewWidth, y)
            }
        }
        return path
    }

    private fun addRectTopPath(path: Path, viewOffset: Point, start: Int, end: Int, top: Int) {
        path.addRect(
            (viewOffset.x + start).toFloat(),
            (viewOffset.y + top).toFloat(),
            (viewOffset.x + end).toFloat(),
            (viewOffset.y + top + 1).toFloat(),
            Path.Direction.CCW
        )
    }

    private fun getViewPosition(view: View): Point {
        val location = IntArray(2)
        getLocationOnScreen(location)

        val locationView = IntArray(2)
        view.getLocationOnScreen(locationView)

        return Point(locationView[0] - location[0], locationView[1] - location[1])
    }

    private fun loadPixelDataFromView(v: View): IntArray {
        if (v.height > 0 && v.width > 0) {
            val intArray = IntArray(v.height * v.width)
            val b = Bitmap.createBitmap(v.width, v.height, Bitmap.Config.ARGB_8888)
            v.draw(Canvas(b))
            b.getPixels(intArray, 0, v.width, 0, 0, v.width, v.height)
            return intArray
        }
        return IntArray(0)
    }

}