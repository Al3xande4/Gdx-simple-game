package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ai.btree.leaf.Wait;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;

public class RestartScreen implements Screen {
    final Drop game;
    long loseTime;

    OrthographicCamera camera;

    int wight = 800;
    int height = 480;

    RestartScreen(Drop game, long loseTime){
        this.game = game;
        this.loseTime = loseTime;
        camera = new OrthographicCamera();

        wight = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();
        camera.setToOrtho(false, wight, height);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, .2f, 1);

        game.batch.begin();
        game.font.draw(game.batch, "You lose", wight / 2 - 5 * 4, height / 2 + 50);
        game.font.draw(game.batch, "Tap to play again", wight / 2 - 8 * 5, height / 2 - 50);
        game.batch.end();

        if(Gdx.input.isTouched() && TimeUtils.nanoTime() - loseTime > 1000000000){
            game.setScreen(new GameScreen(game, TimeUtils.nanoTime()));
        }
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

    }
}
