package com.fortytwo.opent4c.proxy;

public class Shaders {
	public static String vertexShader = "attribute vec4 a_position;    \n" + 
            "attribute vec4 a_color;\n" +
            "attribute vec2 a_texCoord0;\n" + 
            "uniform mat4 u_projTrans;\n" + 
            "varying vec4 v_color;" + 
            "varying vec2 v_texCoords;" + 
            "void main()                  \n" + 
            "{                            \n" + 
            "   v_color = vec4(1, 1, 1, 1); \n" + 
            "   v_texCoords = a_texCoord0; \n" + 
            "   gl_Position =  u_projTrans * a_position;  \n"      + 
            "}                            \n" ;
	public static String fragmentShader = "#ifdef GL_ES\n" +
              "precision mediump float;\n" + 
              "#endif\n" + 
              "varying vec4 v_color;\n" + 
              "varying vec2 v_texCoords;\n" + 
              "uniform sampler2D u_texture;\n" + 
              "void main()                                  \n" + 
              "{                                            \n" + 
              "  gl_FragColor = v_color * texture2D(u_texture, v_texCoords);\n" +
              "}";
}
