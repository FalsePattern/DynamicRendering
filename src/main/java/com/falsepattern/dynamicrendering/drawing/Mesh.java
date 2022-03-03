package com.falsepattern.dynamicrendering.drawing;

import com.falsepattern.dynamicrendering.ModInfo;
import com.falsepattern.dynamicrendering.ResourceUtil;
import com.falsepattern.json.node.JsonNode;
import de.javagl.obj.Obj;
import de.javagl.obj.ObjReader;
import de.javagl.obj.ObjUtils;
import lombok.SneakyThrows;
import lombok.val;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.shader.TesselatorVertexState;
import net.minecraft.util.ResourceLocation;
import org.joml.Matrix3x2f;
import org.joml.Matrix4x3f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;

import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class Mesh implements AutoCloseable {
    private static final Map<String, Map<String, Mesh>> MESHES = new HashMap<>();
    public static Mesh getMesh(String modID, String mesh) {
        return MESHES.computeIfAbsent(modID, (ignored) -> new HashMap<>()).computeIfAbsent(mesh, (loc) -> new Mesh(modID, mesh)).getInstance();
    }

    @SneakyThrows
    private static Obj readObj(String modID, String modelDir) {
        val objInputStream = Minecraft.getMinecraft().getResourceManager()
                                      .getResource(new ResourceLocation(modID, Paths.get(modelDir, "mesh.obj").toString()))
                                      .getInputStream();
        return ObjUtils.triangulate(ObjReader.read(objInputStream));
    }

    private final Obj obj;

    private final boolean transparent;
    public final boolean hasTexture;
    private final boolean lighting;
    private final boolean normal;
    private final boolean cullBackFaces;
    private final Matrix3x2f uvMatrix;
    private final Matrix4x3f vertexMatrix;

    private boolean hasList = false;
    private int displayList = -1;
    private double lastX = 0;
    private double lastY = 0;
    private double lastZ = 0;
    private TesselatorVertexState vertexState = null;
    private Mesh(String modID, String modelName) {
        val metadata = JsonNode.parse(ResourceUtil.toString(new ResourceLocation(modID, Paths.get(modelName, "metadata.json").toString())));
        transparent = metadata.getBool("transparent");
        hasTexture = metadata.getBool("texture");
        lighting = metadata.getBool("lighting");
        normal = metadata.getBool("normal");
        cullBackFaces = metadata.getBool("cullBackFaces");
        vertexMatrix = Util.readMatrix(metadata.get("vertexMatrix"), 4, 3, Matrix4x3f::new, Matrix4x3f::set);
        uvMatrix = hasTexture ? Util.readMatrix(metadata.get("uvMatrix"), 3, 2, Matrix3x2f::new, Matrix3x2f::set) : null;

        obj = readObj(modID, modelName);
    }

    private Mesh(Obj obj, boolean hasTexture, boolean lighting, boolean normal, boolean cullBackFaces, Matrix4x3f vertexMatrix, Matrix3x2f uvMatrix) {
        this.obj = obj;
        this.transparent = true;
        this.hasTexture = hasTexture;
        this.lighting = lighting;
        this.normal = normal;
        this.cullBackFaces = cullBackFaces;
        this.vertexMatrix = vertexMatrix;
        this.uvMatrix = uvMatrix;
    }

    public Mesh getInstance() {
        if (!transparent) return this; else return new Mesh(obj, hasTexture, lighting, normal, cullBackFaces, vertexMatrix, uvMatrix);
    }

    public void draw(double x, double y, double z) {
        sync(x, y, z);
        GL11.glCallList(displayList);
    }

    private static final Vector2f uvBuffer = new Vector2f();
    private static final Vector3f vertexBuffer = new Vector3f();

    private void sync(double x, double y, double z) {
        boolean sortNeeded = sortNeeded(x, y, z);
        if (!hasList || (transparent && sortNeeded)) {
            if (!hasList) {
                displayList = GLAllocation.generateDisplayLists(1);
            }
            GL11.glNewList(displayList, GL11.GL_COMPILE);
            GL11.glPushAttrib(GL11.GL_ENABLE_BIT | GL11.GL_POLYGON_BIT);
            if (hasTexture) {
                GL11.glEnable(GL11.GL_TEXTURE_2D);
            } else {
                GL11.glDisable(GL11.GL_TEXTURE_2D);
            }
            if (lighting) {
                GL11.glEnable(GL11.GL_LIGHTING);
            } else {
                GL11.glDisable(GL11.GL_LIGHTING);
            }
            if (transparent) {
                GL11.glEnable(GL11.GL_BLEND);
                OpenGlHelper.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            } else {
                GL11.glDisable(GL11.GL_BLEND);
            }
            if (cullBackFaces) {
                GL11.glEnable(GL11.GL_CULL_FACE);
                GL11.glCullFace(GL11.GL_BACK);
            } else {
                GL11.glDisable(GL11.GL_CULL_FACE);
            }
            Tessellator tess = Tessellator.instance;
            tess.startDrawing(GL11.GL_TRIANGLES);
            if (!hasList) {
                int faces = obj.getNumFaces();
                for (int i = 0; i < faces; i++) {
                    val face = obj.getFace(i);
                    for (int j = 0; j < 3; j++) {
                        if (normal) {
                            val normal = obj.getNormal(face.getNormalIndex(j));
                            tess.setNormal(normal.getX(), normal.getY(), normal.getZ());
                        }
                        if (hasTexture) {
                            Util.floatTupleToVector(obj.getTexCoord(face.getTexCoordIndex(j)), uvBuffer).mulPosition(uvMatrix);
                            tess.setTextureUV(uvBuffer.x, uvBuffer.y);
                        }
                        {
                            Util.floatTupleToVector(obj.getVertex(face.getVertexIndex(j)), vertexBuffer).mulPosition(vertexMatrix);
                            tess.addVertex(vertexBuffer.x, vertexBuffer.y, vertexBuffer.z);
                        }
                    }
                }
            } else {
                tess.setVertexState(vertexState);
            }
            if (transparent) {
                vertexState = tess.getVertexState((float)-x, (float)-y, (float)-z);
                tess.setVertexState(vertexState);
            }
            tess.draw();
            GL11.glPopAttrib();
            GL11.glEndList();
            lastX = x;
            lastY = y;
            lastZ = z;
            hasList = true;
        }
    }

    private boolean sortNeeded(double x, double y, double z) {
        double distanceSq = x * x + y * y + z * z;
        float delta = distanceSq > 1024 ? 1 : 0.1f;
        return Math.abs(lastX - x) > delta || Math.abs(lastY - y) > delta || Math.abs(lastZ - z) > delta;
    }

    @Override
    public void close() {
        if (hasList && transparent)
            GLAllocation.deleteDisplayLists(displayList);
    }
}
