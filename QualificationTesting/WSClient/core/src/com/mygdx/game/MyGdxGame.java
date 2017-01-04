package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MyGdxGame extends ApplicationAdapter {

	SpriteBatch batch;
	NewGameManager gameManager = new NewGameManager();
	Music menuMusic;

	@Override
	public void create () {
		batch = new SpriteBatch();

		gameManager.readyToConnect();

		menuMusic = Gdx.audio.newMusic(Gdx.files.internal("music/game.mp3"));
		menuMusic.setLooping(true);
		menuMusic.play();
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		gameManager.render(batch);
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		gameManager.dispose();
		menuMusic.dispose();
	}
}
