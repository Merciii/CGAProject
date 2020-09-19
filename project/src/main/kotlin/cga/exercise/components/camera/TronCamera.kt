package cga.exercise.components.camera

import cga.exercise.components.geometry.Transformable
import cga.exercise.components.shader.ShaderProgram
import org.joml.Matrix4f
import org.joml.Vector3f
import org.joml.Math

class TronCamera(var fieldofView: Float = (Math.toRadians(90f)),
                 var seitenverhaeltnis: Float = 16.0f / 9.0f,
                 var nearPlane: Float = 0.1f,
                 var farPlane: Float = 100.0f) : Transformable(), ICamera {
    override fun getCalculateViewMatrix(): Matrix4f {

        return Matrix4f().lookAt(getWorldPosition(), getWorldPosition().sub(getWorldZAxis()), getWorldYAxis())
        //return getWorldModelMatrix().invert()
    }

    override fun getCalculateProjectionMatrix(): Matrix4f {

        return Matrix4f().perspective(fieldofView, seitenverhaeltnis, nearPlane, farPlane)

    }

    override fun bind(shader: ShaderProgram) {

        shader.setUniform("view_matrix", getCalculateViewMatrix(), false)

        shader.setUniform("projection_matrix", getCalculateProjectionMatrix(), false)

    }
}