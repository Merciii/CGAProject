package cga.exercise.components.light

import cga.exercise.components.geometry.Transformable
import cga.exercise.components.shader.ShaderProgram
import org.joml.Vector3f

open class PointLight(var pos :Vector3f,var color :Vector3f) :Transformable(),IPointLight{


    var attenuation = Vector3f(1.0f,0.5f,0.1f)
init {

    translateGlobal(pos)
}
    override fun bind(shaderProgram: ShaderProgram, name: String) {
        shaderProgram.setUniform(name + "pointlight_col",color)
        shaderProgram.setUniform(name +"pointlight_pos",getWorldPosition())
        shaderProgram.setUniform(name +"pointlight_attenuation",attenuation)
    }

}