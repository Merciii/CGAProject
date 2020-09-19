package cga.exercise.game

import cga.exercise.components.camera.TronCamera
import cga.exercise.components.geometry.Material
import cga.exercise.components.geometry.Mesh
import cga.exercise.components.geometry.Renderable
import cga.exercise.components.geometry.VertexAttribute
import cga.exercise.components.light.PointLight
import cga.exercise.components.light.SpotLight
import cga.exercise.components.shader.ShaderProgram
import cga.exercise.components.texture.Texture2D
import cga.framework.GLError
import cga.framework.GameWindow
import cga.framework.GameWindow.MousePosition
import cga.framework.ModelLoader
import cga.framework.OBJLoader
import org.joml.Math
import org.joml.Vector2f
import org.joml.Vector3f
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL15


/**
 * Created by Fabian on 16.09.2017.
 */
class Scene(private val window: GameWindow)  {
    private val staticShader: ShaderProgram

    var bodenr :Renderable
    var camera :TronCamera
    var material :Material
    var diff :Texture2D
    var emit :Texture2D
    var spec : Texture2D
    var cycle: Renderable
    var pointlight :PointLight
    var spotligt :SpotLight
    var ducky: Renderable
    var ducky2: Renderable
    var cat: Renderable
    var TableChairs : Renderable
    var Rug : Renderable
    var plant : Renderable

    //var stv : Renderable
    //var ikea : Renderable
    //var Bear : Renderable
    //var Coke : Renderable
    //var schrank1 : Renderable
    var lamp : Renderable
    var arc : Renderable
    var dog : Renderable

    var jumps = 0

    //scene setup
    init {

        //Shader
        //staticShader = ShaderProgram("assets/shaders/simple_vert.glsl", "assets/shaders/simple_frag.glsl")
        staticShader = ShaderProgram("assets/shaders/tron_vert.glsl", "assets/shaders/tron_frag.glsl")
        //staticShader = ShaderProgram("assets/shaders/ToonNeu/toon_vert.glsl", "assets/shaders/ToonNeu/toon_frag.glsl")

        //Modelle


        //TronBike -- NICHT GERENDERT
        cycle  = ModelLoader.loadModel("assets/Light Cycle/Light Cycle/HQ_Movie cycle.obj",Math.toRadians(-90f),Math.toRadians(90f),0f) ?: throw IllegalArgumentException("Could not load the model")
        cycle.translateGlobal(Vector3f(10f, 0f, 10f))
        cycle.scaleLocal(Vector3f(0.8f,0.8f,0.8f))

        //Enten
        ducky = ModelLoader.loadModel("assets/models/ducky/ducky.obj", Math.toRadians(0f), Math.toRadians(0f), 0f)
                ?: throw IllegalArgumentException("Could not load the model Ducky")
        ducky.translateGlobal(Vector3f(-5f, 0f, -1f))
        ducky.scaleLocal(Vector3f(0.002f, 0.002f, 0.002f))
        //ducky.rotateLocal(0f, 0f, Math.toRadians(-90f))

        //Ducky auf Tisch
        ducky2 = ModelLoader.loadModel("assets/models/ducky/ducky.obj", Math.toRadians(0f), Math.toRadians(0f), 0f)
                ?: throw IllegalArgumentException("Could not load the model Ducky")
        ducky2.translateGlobal(Vector3f(-4.3f, 1.3f, -3f))
        ducky2.scaleLocal(Vector3f(0.001f, 0.001f, 0.001f))

        //Katze
        cat = ModelLoader.loadModel("assets/models/lowpolycat/cat.obj", Math.toRadians(0f), Math.toRadians(90f), 0f)
                ?: throw IllegalArgumentException("Could not load the model Cat")
        cat.translateGlobal(Vector3f(1.5f, 0f, 3.2f))
        //cat.scaleLocal(Vector3f(0.0015f, 0.0015f, 0.0015f))
        cat.scaleLocal(Vector3f(0.005f, 0.005f, 0.005f)) //TODO Raus nehmen

        //Table & Chairs
        TableChairs = ModelLoader.loadModel("assets/models/TableChairs/Table And Chairs.obj", Math.toRadians(0f), Math.toRadians(90f), 0f)
                ?: throw IllegalArgumentException("Could not load the model Table & Chairs")
        TableChairs.translateGlobal(Vector3f(-4f, 0f, -3f))
        TableChairs.scaleLocal(Vector3f(0.03f, 0.03f, 0.03f))

        //Teppich
        Rug = ModelLoader.loadModel("assets/models/Rug/10417_Rectangular_rug_v1_iterations-2.obj", Math.toRadians(-90f), Math.toRadians(90f), 0f)
                ?: throw IllegalArgumentException("Could not load the model Rug")
        Rug.translateGlobal(Vector3f(-4f, 0.0001f, -3f))
        Rug.scaleLocal(Vector3f(0.03f, 0.03f, 0.03f))

        //Plant
        plant = ModelLoader.loadModel("assets/models/plant/01Alocasia_obj.obj", Math.toRadians(0f), Math.toRadians(0f), 0f)
                ?: throw IllegalArgumentException("Could not load the model Plant")
        plant.translateGlobal(Vector3f(-5.8f, 0f, -5.5f))
        plant.scaleLocal(Vector3f(0.0015f, 0.0015f, 0.0015f))

        //Lampe
        lamp = ModelLoader.loadModel("assets/models/lamp/lamp.obj", Math.toRadians(0f), Math.toRadians(0f), 0f)
                ?: throw IllegalArgumentException("Could not load the model Lamp")
        lamp.translateGlobal(Vector3f(-4f, 1.3f, -2.8f))
        lamp.scaleLocal(Vector3f(0.1f, 0.1f, 0.1f))

        arc = ModelLoader.loadModel("assets/models/arc/obj2/Arc170.obj", Math.toRadians(0f), Math.toRadians(140f), 0f)
                ?: throw IllegalArgumentException("Could not load the model ACR")
        arc.translateGlobal(Vector3f(-4f, 1.3f, -2.1f))
        arc.scaleLocal(Vector3f(0.001f, 0.001f, 0.001f))

        dog = ModelLoader.loadModel("assets/models/dog/13041_Beagle_v1_L1.obj", Math.toRadians(-90f), Math.toRadians(-30f), 0f)
                ?: throw IllegalArgumentException("Could not load the model DOG")
        dog.translateGlobal(Vector3f(-2f, 0f, -2.6f))
        dog.scaleLocal(Vector3f(0.015f, 0.015f, 0.015f))

        /*
        stv = ModelLoader.loadModel("assets/models/tv/Samsung Smart TV 55 Zoll.obj", Math.toRadians(0f), Math.toRadians(0f), 0f)
                ?: throw IllegalArgumentException("Could not load the model TV")
        stv.translateGlobal(Vector3f(1.6f, 0f, 1.3f))
        stv.scaleLocal(Vector3f(0.002f, 0.002f, 0.002f))

        ikea = ModelLoader.loadModel("assets/models/ikea/lemari ikea.obj", Math.toRadians(-90f), Math.toRadians(90f), 0f)
                ?: throw IllegalArgumentException("Could not load the model ikea")
        ikea.translateGlobal(Vector3f(-4f, 0.0001f, -3f))
        ikea.scaleLocal(Vector3f(0.003f, 0.003f, 0.003f))

        //Schrank hoch
        schrank1 = ModelLoader.loadModel("assets/models/schrank/Cabinet - Tall Black.obj", Math.toRadians(0f), Math.toRadians(0f), 0f)
                ?: throw IllegalArgumentException("Could not load the model Schrank hoch")
        schrank1.translateGlobal(Vector3f(-5.8f, 0f, -5.5f))
        schrank1.scaleLocal(Vector3f(0.0015f, 0.0015f, 0.0015f))

        //Bear
        Bear = ModelLoader.loadModel("assets/models/TheLittleOne/TheLittleOne.obj", Math.toRadians(0f), Math.toRadians(90f), 0f)
                ?: throw IllegalArgumentException("Could not load the model Bear")
        Bear.translateGlobal(Vector3f(-4f, 0.0001f, -3f))
        Bear.scaleLocal(Vector3f(0.000003f, 0.000003f, 0.000003f))

        //Coke
        Coke = ModelLoader.loadModel("assets/models/Coke/Bottle of coke.obj", Math.toRadians(0f), Math.toRadians(90f), 0f)
                ?: throw IllegalArgumentException("Could not load the model Coke")
        Coke.translateGlobal(Vector3f(-4f, 0.0001f, -3f))
        Coke.scaleLocal(Vector3f(0.00003f, 0.00003f, 0.00003f))
        */

        //TODO Werden hier aus dem Boden Object Mesh Daten gemacht?
        val res2: OBJLoader.OBJResult = OBJLoader.loadOBJ("assets/models/ground.obj")
        val objMesh2: OBJLoader.OBJMesh = res2.objects[0].meshes[0]

        //Boden
        diff = Texture2D.invoke("assets/textures/ground_diff.png",true)
        diff.setTexParams(GL_REPEAT, GL_REPEAT, GL_LINEAR_MIPMAP_LINEAR, GL_LINEAR)
        //emit = Texture2D.invoke("assets/textures/ground_emit.png",true)
        emit = Texture2D.invoke("assets/Tiles_036_SD/Tiles_036_basecolor.jpg",true)
        emit.setTexParams(GL_REPEAT, GL_REPEAT, GL_LINEAR_MIPMAP_LINEAR, GL_LINEAR)
        spec = Texture2D.invoke("assets/textures/ground_spec.png",true)
        spec.setTexParams(GL_REPEAT, GL_REPEAT, GL_LINEAR_MIPMAP_LINEAR, GL_LINEAR)

        material = Material(diff,emit,spec,60f, Vector2f(64f,64f))

        val stride: Int = 8 * 4
        val attrPos = VertexAttribute(3, GL15.GL_FLOAT, stride, 0)
        val attrTC =  VertexAttribute(2, GL15.GL_FLOAT, stride, 3 * 4)
        val attrNorm =  VertexAttribute(3, GL15.GL_FLOAT, stride, 5 * 4)
        val vertexAttributes = arrayOf(attrPos, attrTC, attrNorm)
        //kugelMesh = Mesh(objMesh.vertexData, objMesh.indexData, vertexAttributes)
        val bodenmesh  = Mesh(objMesh2.vertexData, objMesh2.indexData, vertexAttributes,material)


        bodenr = Renderable(mutableListOf(bodenmesh))
        //kugelr = Renderable(mutableListOf(kugelMesh))

        //bodenr.rotateLocal(Math.toRadians(90.0f),0f,0f)
        //bodenr.scaleLocal(Vector3f(0.03f,0.03f,0.03f))
        // kugelr.scaleLocal(Vector3f(0.5f,0.5f,0.5f))

        //Kamera
        // Wer hat die Kamera
        camera = TronCamera()
        camera.rotateLocal(Math.toRadians(-35.0f),0f,0f)
       // camera.translateLocal(Vector3f(0.0f,0.0f,4.0f))
        camera.translateLocal(Vector3f(0.0f,400.0f,200.0f)) //Kamera position
        camera.parent = cat

        //Licht (Position, Farbe)
        pointlight = PointLight(Vector3f(0f,-0.5f,0f),Vector3f(1f,1f,0.3f))
        pointlight.parent = lamp

        spotligt = SpotLight(Vector3f(0f,0f,0f),Vector3f(1f,0f,1f),Math.cos(Math.toRadians(30f)),Math.cos(Math.toRadians(50f)))
        spotligt.parent = cat


        glClearColor(0.0f, 0.0f, 0.0f, 1.0f); GLError.checkThrow()
        glEnable(GL_DEPTH_TEST); GLError.checkThrow()
        glDepthFunc(GL_LESS); GLError.checkThrow()

        glEnable(GL_CULL_FACE)
        glFrontFace(GL_CCW)
        glCullFace(GL_BACK)

    }

    //Scene rendern
    fun render(dt: Float, t: Float) {

        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)

        staticShader.use()
        camera.bind(staticShader)
        staticShader.setUniform("shadingcolor",Vector3f(1f,0f,0f))
        pointlight.bind(staticShader,"bike")
        spotligt.bind(staticShader," ", camera.getCalculateViewMatrix())
        staticShader.setUniform("shadingcolor",Vector3f(0f,1f,0f))
        bodenr.render(staticShader)

        //cycle.render(staticShader)
        ducky.render(staticShader)
        ducky2.render(staticShader)
        cat.render(staticShader)
        TableChairs.render(staticShader)
        Rug.render(staticShader)
        //Bear.render(staticShader)
        //Coke.render(staticShader)
        plant.render(staticShader)
        lamp.render(staticShader)
        arc.render(staticShader)
        dog.render(staticShader)
    }

    fun update(dt: Float, t: Float) {
        //vorwärts
        if(window.getKeyState(GLFW_KEY_W)){
            //cycle.translateLocal(Vector3f(0.0f,0.0f,-5.0f*dt))
            cat.translateLocal(Vector3f(0.0f,0.0f,-800.0f*dt))
        }
        //rückwärts
        if(window.getKeyState(GLFW_KEY_S)){
            //cycle.translateLocal(Vector3f(0f,0f,5.0f*dt))
            cat.translateLocal(Vector3f(0f,0f,800.0f*dt))
        }
        //links
        if(window.getKeyState(GLFW_KEY_A)){
            //cycle.translateLocal(Vector3f(0.0f,0.0f,0f*dt))
            //cycle.rotateLocal(0.0f,1f*dt,0.0f)
            cat.translateLocal(Vector3f(0.0f,0.0f,0f*dt))
            cat.rotateLocal(0.0f,2f*dt,0.0f)
        }
        //rechts
        if(window.getKeyState(GLFW_KEY_D)){
            //cycle.translateLocal(Vector3f(0.0f,0.0f,0f*dt))
            //cycle.rotateLocal(0.0f,-1f*dt,0.0f)
            cat.translateLocal(Vector3f(0.0f,0.0f,0f*dt))
            cat.rotateLocal(0.0f,-2f*dt,0.0f)
        }

        //Sprint
        if(window.getKeyState(GLFW_KEY_LEFT_SHIFT)&&window.getKeyState(GLFW_KEY_W)){
            cat.translateLocal(Vector3f(0.0f,0.0f,-1000.0f*dt))
        }

        //TODO: Springen - nur hoch; else statement funktioniert so nicht

        if(window.getKeyState(GLFW_KEY_SPACE)&&jumps == 0){
            cat.translateLocal(Vector3f(0.0f,100.0f,0f*dt))
            jumps = 1
        }
        else if(window.getKeyState(GLFW_KEY_SPACE)&&jumps == 1)
        {
            cat.translateLocal(Vector3f(0.0f,-100.0f,0f*dt))
            jumps = 0
        }

        //RESET
        if(window.getKeyState(GLFW_KEY_LEFT_ALT)&&jumps>0){
            cat.translateLocal(Vector3f(0.0f,-100f,0f*dt))
            jumps = 0
        }

        /*if(!window.getKeyState(GLFW_KEY_SPACE)){
            cycle.translateLocal(Vector3f(0.0f,0.0f,0f*dt))
        }*/

        /*
        //Nicht nötig
        //vorwärts&links
        if(window.getKeyState(GLFW_KEY_A)&&window.getKeyState(GLFW_KEY_W)){
            cycle.translateLocal(Vector3f(0.0f,0.0f,-5.0f*dt))
            cycle.rotateLocal(0.0f,1f*dt,0.0f)
        }
        //vorwärts&rechts
        if(window.getKeyState(GLFW_KEY_D)&&window.getKeyState(GLFW_KEY_W)) {
            cycle.translateLocal(Vector3f(0.0f, 0.0f, -5.0f * dt))
            cycle.rotateLocal(0.0f, -1f * dt, 0.0f)
        }
        */
    }
    fun onKey(key: Int, scancode: Int, action: Int, mode: Int) {}

    var alt_xpos: Double = 0.0
    var alt_ypos: Double = 0.0

    fun onMouseMove(xpos: Double, ypos: Double) {

        val diff_x = alt_xpos - xpos
        val diff_y = alt_ypos - ypos

        //Maus/Kamera um die Spielfigur bewegen
        camera.rotateAroundPoint(0f, diff_x.toFloat() * 0.001f, 0f, cat.getYAxis())
        //camera.rotateAroundPoint(diff_y.toFloat() * 0.001f, 0f, 0f, cat.getXAxis())

        alt_xpos = xpos
        alt_ypos = ypos

        /*
        var olpx =0.0
        var oldpy =0.0

        val distanceX = window.mousePos.xpos - olpx
        val distanceY = window.mousePos.ypos - oldpy

        if(distanceX > 0){
            //camera.rotateLocal(0f,Math.toRadians(distanceX.toFloat() *0.02f),0f)
        }
        if(distanceX < 0){
          // camera.rotateLocal(0f,Math.toRadians(distanceX.toFloat() * 0.02f),0f)
        }
        if(distanceY > 0){
           // camera.translateLocal(Vector3f(0f,0.01f,0f))
        }
        if(distanceY < 0){
           // camera.translateLocal(Vector3f(0f,-0.01f,0f))
        }

      olpx = window.mousePos.xpos
        oldpy = window.mousePos.ypos
      */
    }


    fun cleanup() {}

}
