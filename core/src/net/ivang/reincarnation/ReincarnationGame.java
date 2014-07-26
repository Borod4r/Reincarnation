package net.ivang.reincarnation;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.math.Vector3;


public class ReincarnationGame extends ApplicationAdapter {

    public PerspectiveCamera cam;
    public CameraInputController camController;

    public Environment environment;

    private VoxelRenderer voxelRenderer;

//    public ModelBatch modelBatch;
//    public Model model;
//    public List<ModelInstance> modelInstances;
	
	@Override
	public void create () {
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
//        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));
//        environment.add(new PointLight().set(Color.GREEN, 10f, 10f, 0f, 64f));


        Voxel rootVoxel = new Voxel(Vector3.Zero, 100f, 0);
        createChildrenRecursively(rootVoxel, 8);

        voxelRenderer = new VoxelRenderer(rootVoxel);
	}

    private void createChildrenRecursively(Voxel voxel, int depth) {
        if (depth-- > 0) {
            voxel.createChildren();
            createChildrenRecursively(voxel.getChildren()[4], depth);
        }
    }

	@Override
	public void render () {
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        camController.update();

        voxelRenderer.render(cam, environment);
	}

    @Override
    public void dispose() {
        voxelRenderer.dispose();
    }
}
