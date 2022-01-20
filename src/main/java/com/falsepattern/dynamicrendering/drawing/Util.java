package com.falsepattern.dynamicrendering.drawing;

import com.falsepattern.json.node.JsonNode;
import de.javagl.obj.FloatTuple;
import lombok.val;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class Util {
    private static final float[] buf = new float[16]; //Has space for a 4x4 matrix
    public static <T> T readMatrix(JsonNode json, int columns, int rows, Supplier<T> constructor, BiConsumer<T, float[]> matrixSetter) {
        val matrix = constructor.get();
        for (int y = 0; y < rows; y++) {
            val row = json.get(y);
            for (int x = 0; x < columns; x++) {
                buf[x * rows + y] = row.get(x).floatValue();
            }
        }
        matrixSetter.accept(matrix, buf);
        return matrix;
    }

    public static Vector2f floatTupleToVector(FloatTuple tuple, Vector2f vector) {
        return vector.set(tuple.getX(), tuple.getY());
    }

    public static Vector3f floatTupleToVector(FloatTuple tuple, Vector3f vector) {
        return vector.set(tuple.getX(), tuple.getY(), tuple.getZ());
    }
}
