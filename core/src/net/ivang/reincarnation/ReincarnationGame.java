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

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.math.Vector3;

import static com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

/**
 * @author Ivan Gadzhega
 * @since 0.1
 */
public class ReincarnationGame extends ApplicationAdapter {

    private BitmapFont bitmapFont;
    private SpriteBatch spriteBatch;

    public PerspectiveCamera cam;
    public CameraInputController camController;

    public Environment environment;
    private VoxelRenderer voxelRenderer;
	
	@Override
	public void create() {
        cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cam.position.set(100f, 100f, 100f);
        cam.lookAt(0,0,0);
        cam.near = 0f;
        cam.far = 300f;
        cam.update();

        camController = new CameraInputController(cam);
        Gdx.input.setInputProcessor(camController);

        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.8f, 0.8f, 0.8f, 1f));

        Voxel rootVoxel = new Voxel(Vector3.Zero, 100f, 0);
        createChildrenRecursively(rootVoxel, 8);

        voxelRenderer = new VoxelRenderer(rootVoxel);

        generateBitmapFont();
        spriteBatch = new SpriteBatch();
	}

	@Override
	public void render() {
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        renderVoxels();
        renderText();
	}

    @Override
    public void dispose() {
        voxelRenderer.dispose();
        spriteBatch.dispose();
        bitmapFont.dispose();
    }

    //---------------------------------------------------------------------
    // Helper methods
    //---------------------------------------------------------------------

    private void createChildrenRecursively(Voxel voxel, int depth) {
        if (depth-- > 0) {
            voxel.createChildren();
            createChildrenRecursively(voxel.getChildren()[4], depth);
        }
    }

    private void generateBitmapFont() {
        FileHandle trueTypeFont = Gdx.files.internal("fonts/Roboto-Regular.ttf");
        FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(trueTypeFont);
        FreeTypeFontParameter fontParameter = new FreeTypeFontParameter();
        fontParameter.size = 12;
        bitmapFont = fontGenerator.generateFont(fontParameter);
        fontGenerator.dispose();
    }

    private void renderVoxels() {
        camController.update();
        voxelRenderer.render(cam, environment);
    }

    private void renderText() {
        spriteBatch.begin();
        CharSequence fps = "FPS: " + Gdx.graphics.getFramesPerSecond();
        float x = 5;
        float y = Gdx.graphics.getHeight() - x;
        bitmapFont.draw(spriteBatch, fps, x, y);
        spriteBatch.end();
    }

}
