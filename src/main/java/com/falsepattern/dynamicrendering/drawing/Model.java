package com.falsepattern.dynamicrendering.drawing;

import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.util.ResourceLocation;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;

import java.nio.FloatBuffer;

public class Model implements AutoCloseable {
    private final Mesh mesh;
    private final ResourceLocation texture;
    public final Vector3f translation = new Vector3f();
    public final Quaternionf rotation = new Quaternionf().identity();
    public final Vector3f scale = new Vector3f(1, 1, 1);
    private static final Matrix4f transform = new Matrix4f();
    private static final FloatBuffer buf = GLAllocation.createDirectByteBuffer(16 * 4).asFloatBuffer();
    public Model(String modID, String mesh, String texture) {
        this.mesh = Mesh.getMesh(modID, mesh);
        this.texture = new ResourceLocation(modID, texture);
    }

    public Model(Model other) {
        this.mesh = other.mesh.getInstance();
        this.texture = other.texture;
        this.translation.set(other.translation);
        this.rotation.set(other.rotation);
        this.scale.set(other.scale);
    }

    @Override
    public void close() {
        mesh.close();
    }

    public void draw(MeshRenderer renderer, double x, double y, double z) {
        if (mesh.hasTexture) {
            renderer.bindTexture(texture);
        }
        transform.translation(0.5f, 0.5f, 0.5f).translate(translation).rotate(rotation).scale(scale).get(buf);
        GL11.glMultMatrix(buf);
        mesh.draw(x, y, z);
    }
}
