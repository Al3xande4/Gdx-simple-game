package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.SortedIntList;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.math.Rectangle;
import java.util.Iterator;

public class GameScreen implements Screen {
	final Drop game;
	private OrthographicCamera camera;

	private Texture dropImage;
	private Texture bucketImage;

	private Rectangle bucket;
	private Array<Rectangle> raindrops;

	private long lastDropTime;
	private long startTime;

	private int dropsGathered = 0;
	private int health = 3;

	public GameScreen(Drop game, long startTime) {
		this.game = game;
		this.startTime = startTime;

		// load the images from assets, 64x64 pixels size
		dropImage = new Texture(Gdx.files.internal("raindrop.png"));
		bucketImage = new Texture(Gdx.files.internal("bucket.png"));

		// create the camera and SpriteBatch(provide easy work with 2d graphics)
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);

		// create the rectangle for bucket
		bucket = new Rectangle();
		bucket.x = 800 / 2 - 64 / 2; // left coordinate
		bucket.y = 20; // bottom coordinate
		bucket.height = 64;
		bucket.width = 64;

		// create the raindrops array and spawn the first raindrop
		raindrops = new Array<Rectangle>();
		spawnDrop();
	}

	public void spawnDrop(){
		Rectangle drop = new Rectangle();
		drop.x = MathUtils.random(0, 800 - 64); // random x position between right corner and left corner
		drop.y = 480; // top of our screen
		drop.width = 64;
		drop.height = 64;
		raindrops.add(drop);
		lastDropTime = TimeUtils.nanoTime(); // return the current time
	}

	@Override
	public void render(float delta) {
		// clear the screen with color (red, green, blue, alpha)
		ScreenUtils.clear(0, 0, .2f, 1); // dark blue color

		// tell the camera to update the matrix
		camera.update();

		// tell the SpriteBatch to render in coordinate system specified by camera
		game.batch.setProjectionMatrix(camera.combined);

		// begin the new batch and draw bucket and all drops
		game.batch.begin();
		game.batch.draw(bucketImage, bucket.x, bucket.y);
		game.font.draw(game.batch, "Drops collected: " + dropsGathered, 50, 460);
		game.font.draw(game.batch, "Health: " + health, 50, 440);
		for(Rectangle raindrop: raindrops){
			game.batch.draw(dropImage, raindrop.x, raindrop.y);
		}
		game.batch.end();

		// get the user touch
		if(Gdx.input.isTouched() && TimeUtils.nanoTime() - startTime > 1000000000){
			Vector3 touchPos = new Vector3();
			touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			camera.unproject(touchPos); // make the touchPos coordinate system to camera system
			bucket.x = (int) (touchPos.x - 64 / 2);
		}

		// keybord movement
		if(Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A))
			bucket.x -= 400 * Gdx.graphics.getDeltaTime();
		if(Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D))
			bucket.x += 400 * Gdx.graphics.getDeltaTime();

		// make sure the bucket stays between screen
		if(bucket.x < 0) bucket.x = 0;
		if(bucket.x > 800 - 64) bucket.x = 800 - 64;

		// check if we need to create a new raindrop, it creates every second
		if(TimeUtils.nanoTime() - lastDropTime > 1000000000) spawnDrop();

		// Go through raindrops and move them, remove any that are beneath the bottom edge of
		// the screen or that hit the bucket.
		for(Iterator<Rectangle> iter = raindrops.iterator(); iter.hasNext();){
			Rectangle raindrop = iter.next();
			raindrop.y -= 200 * Gdx.graphics.getDeltaTime(); // pixels per second
			if (raindrop.y + 64 < 0){
				iter.remove();
				health--;
			}
			if(raindrop.overlaps(bucket)){
				dropsGathered++;
				iter.remove();
			}
		}

		if(health == 0) {
			game.setScreen(new RestartScreen(game, TimeUtils.nanoTime()));
		}
	}

	@Override
	public void show() {

	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void hide() {

	}

	@Override
	public void dispose() {
		//dispose of all natives resources
		bucketImage.dispose();
		dropImage.dispose();
	}
}
