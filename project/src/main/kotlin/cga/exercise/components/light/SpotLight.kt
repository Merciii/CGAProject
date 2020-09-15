package cga.exercise.components.light

import cga.exercise.components.shader.ShaderProgram
import org.joml.Matrix3f
import org.joml.Matrix4f
import org.joml.Vector3f
import org.joml.Vector4f

class SpotLight( pos :Vector3f, col: Vector3f,  val innerAngel :Float ,val outerAngel :Float ): PointLight(  pos,col),ISpotLight {
    var spotcolor = col
    var atteunation = Vector3f(0.5f,0.05f,0.01f)
    init {

       translateGlobal(pos)
   }

    override fun bind(shaderProgram: ShaderProgram, name: String, viewMatrix: Matrix4f) {

        var position :Vector4f  = Vector4f(getWorldPosition(),1.0f).mul(viewMatrix)
        shaderProgram.setUniform("spotlight_col",spotcolor)
        shaderProgram.setUniform("spotlight_pos", Vector3f(position.x,position.y,position.z))
        shaderProgram.setUniform("spotlight_dir",getWorldZAxis().negate().mul(Matrix3f(viewMatrix)))
        shaderProgram.setUniform("spot_inner",innerAngel)
        shaderProgram.setUniform("spot_outer",outerAngel)
        shaderProgram.setUniform("spotLightAttenuation",atteunation)
    }
}