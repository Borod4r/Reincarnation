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
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Camera;
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

    private BitmapFont mBitmapFont;
    private SpriteBatch mSpriteBatch;

    private PerspectiveCamera mCamera;
    private CameraInputController mCameraController;

    private Environment mEnvironment;
    private VoxelRenderer mVoxelRenderer;

    private int mWidth, mHeight;

    //---------------------------------------------------------------------
    // Public methods
    //---------------------------------------------------------------------
	
	@Override
	public void create() {
        initBitmapFont();
        mSpriteBatch = new SpriteBatch();

        mCamera = new PerspectiveCamera(67, mWidth, mHeight);
        mCamera.position.set(100f, 100f, 100f);
        mCamera.lookAt(0, 0, 0);
        mCamera.near = 0f;
        mCamera.far = 300f;
        mCamera.update();

        initInputProcessor(mCamera);

        mEnvironment = new Environment();
        mEnvironment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.8f, 0.8f, 0.8f, 1f));

        Voxel rootVoxel = new Voxel(Vector3.Zero, 100f, 0);
        createChildrenRecursively(rootVoxel, 8);

        mVoxelRenderer = new VoxelRenderer(rootVoxel);
	}

	@Override
	public void render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        renderVoxels();
        renderText();
	}

    @Override
    public void resize(int width, int height) {
        mWidth = width;
        mHeight = height;

        Gdx.gl.glViewport(0, 0, mWidth, mHeight);

        mCamera.viewportWidth = mWidth;
        mCamera.viewportHeight = mHeight;
        mCamera.update(false);

        mSpriteBatch.getProjectionMatrix().setToOrtho2D(0, 0, mWidth, mHeight);
    }

    @Override
    public void dispose() {
        mVoxelRenderer.dispose();
        mSpriteBatch.dispose();
        mBitmapFont.dispose();
    }

    //---------------------------------------------------------------------
    // Helper methods
    //---------------------------------------------------------------------

    private void initBitmapFont() {
        FileHandle trueTypeFont = Gdx.files.internal("fonts/Roboto-Regular.ttf");
        FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(trueTypeFont);
        FreeTypeFontParameter fontParameter = new FreeTypeFontParameter();
        fontParameter.size = 12;
        mBitmapFont = fontGenerator.generateFont(fontParameter);
        fontGenerator.dispose();
    }

    private void initInputProcessor(Camera camera) {
        mCameraController = new CameraInputController(camera);
        GameInputProcessor gameInputProcessor = new GameInputProcessor();

        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(mCameraController);
        inputMultiplexer.addProcessor(gameInputProcessor);

        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    private void createChildrenRecursively(Voxel voxel, int depth) {
        if (depth-- > 0) {
            voxel.createChildren();
            createChildrenRecursively(voxel.getChildren()[4], depth);
        }
    }

    private void renderVoxels() {
        mCameraController.update();
        mVoxelRenderer.render(mCamera, mEnvironment);
    }

    private void renderText() {
        mSpriteBatch.begin();
        CharSequence res = "Res: " + mWidth + "x" + mHeight;
        mBitmapFont.draw(mSpriteBatch, res, 5, mHeight - 5);
        CharSequence fps = "Fps: " + Gdx.graphics.getFramesPerSecond();
        mBitmapFont.draw(mSpriteBatch, fps, 5, mHeight - 20);
        mSpriteBatch.end();
    }

}
