package eu.sisik.addtexttovideo.gl

import android.opengl.Matrix
import android.util.Log
import kotlin.math.tan

/**
 * This class is responsible for moving text around a bit. Currently it only moves the
 * text slowly from top to bottom in a kind of "closing credits" fashion shown at the
 * end of movies. You can apply any kind of other transformations here.
 */
class TextAnimator {

    private val time = Time()

    private var speed = 150f
    private var textTarget = Vec3(0f, 0f, 0f)
    private var textPosition = Vec3(0f, 0f, 0f)

    // Text moves towards textTarget until it reached the target within
    // this distance. After that I reset positions and repeat the cycle
    // once again.
    private var reachDistance = 2f

    // Scale for the text
    private var scale = 12f

    // Camera properties
    private var camDistance = 20f
    private var fovY = 60f

    // Boundaries of the visible area
    private var frustumWidth = -1f
    private var frustumHeight = -1f

    // Used to calculate final MVP matrix
    private val projectionMatrix = FloatArray(16)
    private val viewMatrix = FloatArray(16)

    fun update() {
        updateText()
        time.update()
    }

    fun getMVP(): FloatArray {
        return getMVP(textPosition.x, textPosition.y, textPosition.z)
    }

    private fun updateText() {
        // Text reached destination?
        if (distance(textTarget, textPosition) > reachDistance)
            textPosition += normalize(textTarget - textPosition) * time.deltaTimeSec * speed
        else
            initPositions()
    }

    fun setCamera(width: Int, height: Int) {
        // Set perspective projection
        val aspect: Float = width.toFloat() / height.toFloat()
        Matrix.perspectiveM(projectionMatrix, 0, fovY, aspect, 1f, 100f)

        // Position camera
        Matrix.setLookAtM(viewMatrix, 0, 0f, 0f, -camDistance, 0f, 0f, 0f, 0f, 1.0f, 0.0f)

        // Calculate visible area boundaries within frustum
        calculateBoundaries(aspect)

        initPositions()
    }

    private fun calculateBoundaries(aspect: Float) {
        frustumHeight = 2.0f * camDistance * tan(Math.toRadians(fovY * 0.5)).toFloat()
        frustumWidth = frustumHeight * aspect
    }

    private fun initPositions() {
        textPosition = Vec3(0f,  scale, 0f)
        textTarget = Vec3(0f, -frustumHeight/2 - scale, 0f)
    }

    private fun getMVP(x: Float, y: Float, z: Float): FloatArray {
        // Move model to given position
        val modelMatrix = FloatArray(16)
        Matrix.setIdentityM(modelMatrix, 0)
        Matrix.translateM(modelMatrix, 0, x, y, z)

        // Scale to some reasonable size, flip
        Matrix.scaleM(modelMatrix, 0, -scale, -scale, scale)

        // Calculate final MVP matrix
        val mvMatrix = FloatArray(16)
        Matrix.multiplyMM(mvMatrix, 0, viewMatrix, 0, modelMatrix, 0)

        val mvpMatrix = FloatArray(16)
        Matrix.multiplyMM(mvpMatrix, 0, projectionMatrix, 0, mvMatrix, 0)

        return mvpMatrix
    }
}