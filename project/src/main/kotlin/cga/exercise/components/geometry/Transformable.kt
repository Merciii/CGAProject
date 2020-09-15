package cga.exercise.components.geometry

import org.joml.Matrix4f
import org.joml.Vector3f


open class Transformable(var matrix:Matrix4f = Matrix4f(),var parent: Transformable? = null ): ITransformable {





    override fun rotateLocal(pitch: Float, yaw: Float, roll: Float) {

        matrix.rotateXYZ(pitch, yaw, roll)

    }

    override fun rotateAroundPoint(pitch: Float, yaw: Float, roll: Float, altMidpoint: Vector3f) {
        val tempmatrix = Matrix4f()
        tempmatrix.translate(altMidpoint)
        tempmatrix.rotateXYZ(pitch, yaw, roll)
        tempmatrix.translate(Vector3f(altMidpoint).negate())
        matrix = tempmatrix.mul(matrix)
    }

    override fun translateLocal(deltaPos: Vector3f) {
        matrix.translate(deltaPos)
    }

    override fun translateGlobal(deltaPos: Vector3f) {
        val tempmatrix = Matrix4f()
        tempmatrix.translate(deltaPos)
        matrix = tempmatrix.mul(matrix)


    }

    override fun scaleLocal(scale: Vector3f) {
        matrix.scale(scale)
    }

    override fun getPosition(): Vector3f {

       // var ziel = Vector3f()
        var position = matrix.getColumn(3, Vector3f())
        return position
    }

    override fun getWorldPosition(): Vector3f {
        val world = getWorldModelMatrix()
        val ziel = Vector3f()
        val position = world.getColumn(3, ziel)
        return position
    }

    override fun getXAxis(): Vector3f {
        val ziel = Vector3f()
        var ergebnis = Matrix4f(matrix).normalize3x3()
        var xaxis =  ergebnis.getColumn(0,ziel)
        return xaxis
    }

    override fun getYAxis(): Vector3f {
        val ziel = Vector3f()
        var ergebnis = Matrix4f(matrix).normalize3x3()
        var Yaxis =  ergebnis.getColumn(1,ziel)
        return Yaxis
    }

    override fun getZAxis(): Vector3f {
        val ziel = Vector3f()
        var ergebnis = Matrix4f(matrix).normalize3x3()
        var Zaxis =  ergebnis.getColumn(2,ziel)
        return Zaxis
    }

    override fun getWorldXAxis(): Vector3f {
        val world = getWorldModelMatrix()
        val ziel = Vector3f()
        val ergebnis = world.normalize3x3()
        val xaxis =  ergebnis.getColumn(0,ziel)
        return xaxis


    }

    override fun getWorldYAxis(): Vector3f {
        val world = getWorldModelMatrix()
        val ziel = Vector3f()
        val ergebnis = world.normalize3x3()
        val yaxis =  ergebnis.getColumn(1,ziel)
        return yaxis
    }

    override fun getWorldZAxis(): Vector3f {
        val world = getWorldModelMatrix()
        val ziel = Vector3f()
        val ergebnis = world.normalize3x3()
        val zaxis =  ergebnis.getColumn(2,ziel)
        return zaxis
    }


    override fun getWorldModelMatrix(): Matrix4f {
        val result = getLocalModelMatrix()
        parent?.getWorldModelMatrix()?.mul(getLocalModelMatrix(),result)
        return result
    }

    override fun getLocalModelMatrix(): Matrix4f {
        return  Matrix4f (matrix)
    }

}