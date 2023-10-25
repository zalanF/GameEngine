package lwjgl;

import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.opengl.GL20.glUniform4f;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.GL_COMPILE_STATUS;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_INFO_LOG_LENGTH;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL20.glAttachShader;
import static org.lwjgl.opengl.GL20.glCompileShader;
import static org.lwjgl.opengl.GL20.glCreateProgram;
import static org.lwjgl.opengl.GL20.glCreateShader;
import static org.lwjgl.opengl.GL20.glDeleteShader;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;
import static org.lwjgl.opengl.GL20.glGetShaderi;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glLinkProgram;
import static org.lwjgl.opengl.GL20.glShaderSource;
import static org.lwjgl.opengl.GL20.glValidateProgram;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;

public class Shader {
	
	private int rendererId;
	private String filePathVertexShader, filePathFragmentShader; //not neccessary but cherno said it might be useful
	private HashMap<String, Integer> uniformLocationCache = new HashMap<String, Integer>(); 
	
	public Shader(String filePathVertexShader, String filePathFragmentShader) {
		this.filePathVertexShader = filePathVertexShader;
		this.filePathFragmentShader = filePathFragmentShader;
		
		rendererId = createShader(readShader(filePathVertexShader), readShader(filePathFragmentShader));
		
	}
	
	public void bind() {
		glUseProgram(rendererId);
	}
	
	public void unBind() {
		glUseProgram(0);
		
	}
	
	public void delete() {
		glDeleteShader(rendererId);
	}
	
	public void setUniform4f(String name, float v0, float v1, float v2, float v3) {
		glUniform4f(getUniformLocation(name), v0, v1, v2, v3);
	}
	
	private int getUniformLocation(String name) {
		
		if (uniformLocationCache.containsKey(name)) {
			return uniformLocationCache.get(name);
		}
		
		int location = glGetUniformLocation(rendererId, name);
		
		if(location == -1) {
			System.err.println("Warning: uniform "+name+" doesn't exist!");
		}
		uniformLocationCache.put(name, location);
		
		return location;
	}
	
	/**
	 * Read the shader code from a file
	 * @param filePath - file path of the shader
	 * @return String containing shader code
	 */
	static String readShader(String filePath) {

        FileReader fileReader;
        BufferedReader fileStream;
        StringBuilder shader = new StringBuilder();
        String line;
        try {

            fileReader = new FileReader(filePath);
            fileStream = new BufferedReader(fileReader);

            while ((line = fileStream.readLine()) != null ) { //until EOF
                shader.append(line).append("\n");
            }

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        return shader.toString();
    }

    /**
     * Compiles shader from String w/ error checking. Based on Cherno's OpenGL series
     * @param type - OpenGL constant referencing type of shader
     * @param source - String containing shader code
     * @return the id of the shader
     */
     int compileShader(int type, final String source) {

        int shaderId = glCreateShader(type);
        glShaderSource(shaderId, source);
        glCompileShader(shaderId);

        int result = glGetShaderi(shaderId, GL_COMPILE_STATUS);

        if (result == GL_FALSE) {
            int length = glGetShaderi(shaderId, GL_INFO_LOG_LENGTH);

            String message = glGetShaderInfoLog(shaderId, length);
            //if i recall correctly, this is kind of a shortcut - there is probably a most robust way of doing this
            System.err.println("Failed to compile "+  (type == GL_VERTEX_SHADER ? "vertex" : "fragment")+" shader!");
            System.err.println(message);

            glDeleteShader(shaderId);
            return 0;
        }

        return shaderId;
    }

    /**
     * Attaches and links Vertex and Fragment shader to program. Based on Cherno's OpenGL series
     * @param vertexShader - String containing shader code
     * @param fragmentShader - String containing shader code
     * @return program object as an int
     */
     int createShader(final String vertexShader, final String fragmentShader) {
        int program = glCreateProgram();
        int vs = compileShader(GL_VERTEX_SHADER, vertexShader);
        int fs = compileShader(GL_FRAGMENT_SHADER, fragmentShader);

        glAttachShader(program, vs);
        glAttachShader(program, fs);
        glLinkProgram(program);
        glValidateProgram(program);

        glDeleteShader(vs);
        glDeleteShader(fs);

        return program;
    }

}
