package net.ivang.reincarnation;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;

import static com.badlogic.gdx.graphics.VertexAttributes.Usage;


public class ReincarnationGame extends ApplicationAdapter {

    public PerspectiveCamera cam;
    public CameraInputController camController;

    public Environment environment;

    public ModelBatch modelBatch;
    public Model model1, model2;
    public ModelInstance instance1, instance2;
	
	@Override
	public void create () {
        cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cam.position.set(10f, 10f, 10f);
        cam.lookAt(0,0,0);
        cam.near = 1f;
        cam.far = 300f;
        cam.update();

        camController = new CameraInputController(cam);
        Gdx.input.setInputProcessor(camController);

        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));
//        environment.add(new PointLight().set(Color.GREEN, 10f, 10f, 0f, 64f));

        modelBatch = new ModelBatch();

        ModelBuilder modelBuilder = new ModelBuilder();

        Material material = new Material(ColorAttribute.createDiffuse(Color.GREEN));
        model1 = modelBuilder.createBox(5f, 5f, 5f, material, Usage.Position | Usage.Normal);
        instance1 = new ModelInstance(model1);
        instance1.transform.translate(0f, 0f, 5f);

        Material material2 = new Material(new TextureAttribute(TextureAttribute.Diffuse, new Texture("badlogic.jpg")));
        model2 = modelBuilder.createBox(5f, 5f, 5f, material2, Usage.Position | Usage.Normal | Usage.TextureCoordinates);
        instance2 = new ModelInstance(model2);
        instance2.transform.translate(0f, 0f, -5f);
	}

	@Override
	public void render () {
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        camController.update();

        modelBatch.begin(cam);
        modelBatch.render(instance1, environment);
        modelBatch.render(instance2, environment);
        modelBatch.end();
	}

    @Override
    public void dispose() {
        modelBatch.dispose();
        model1.dispose();
        model2.dispose();
    }
}
