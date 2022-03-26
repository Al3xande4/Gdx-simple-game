package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;

public class MainMenuScreen implements Screen {

    Drop game;
    OrthographicCamera camera;

    public MainMenuScreen(Drop game){
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0f, 0f, .2f, 1f);

        game.batch.begin();
        game.font.draw(game.batch, "Drop game", 280, 300);
        game.font.draw(game.batch, "Tap to start the game", 250, 240);
        game.batch.end();

        if (Gdx.input.isTouched()){
            game.setScreen(new GameScreen(game, TimeUtils.nanoTime()));
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

    }
}
