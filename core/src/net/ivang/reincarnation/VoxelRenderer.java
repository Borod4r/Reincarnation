/*
 * Copyright (c) 2014 Ivan Gadzhega.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of Ivan Gadzhega.
 * ("Confidential Information"). You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the license agreement
 * you entered into with Ivan Gadzhega.
 */

package net.ivang.reincarnation;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ivan Gadzhega
 * @since 0.1
 */
public class VoxelRenderer {

    private Model model;
    private ModelBatch modelBatch;
    private List<ModelInstance> modelInstances;

    //---------------------------------------------------------------------
    // Constructors
    //---------------------------------------------------------------------

    public VoxelRenderer(Voxel voxel) {
        modelBatch = new ModelBatch();
        modelInstances = new ArrayList<ModelInstance>();

        Material material = new Material(ColorAttribute.createDiffuse(Color.GREEN));
        ModelBuilder modelBuilder = new ModelBuilder();
        model = modelBuilder.createBox(1f, 1f, 1f, GL20.GL_LINES, material, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
        createModelInstances(voxel);
    }

    //---------------------------------------------------------------------
    // Public methods
    //---------------------------------------------------------------------

    public void render(PerspectiveCamera cam, Environment environment) {
        modelBatch.begin(cam);
        for (ModelInstance instance : modelInstances) {
            modelBatch.render(instance, environment);
        }
        modelBatch.end();
    }

    public void dispose() {
        modelBatch.dispose();
        model.dispose();
    }

    //---------------------------------------------------------------------
    // Helper methods
    //---------------------------------------------------------------------

    private void createModelInstances(Voxel voxel) {
        if (voxel.isLeaf()) {
            ModelInstance instance = new ModelInstance(model, voxel.getOrigin());
            float size = voxel.getSize();
            instance.transform.scale(size, size, size);
            modelInstances.add(instance);
        } else {
            for (Voxel subVoxel: voxel.getChildren()) {
                createModelInstances(subVoxel);
            }
        }
    }

}
