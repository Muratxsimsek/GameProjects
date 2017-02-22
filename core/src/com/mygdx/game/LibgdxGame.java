package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class LibgdxGame extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	protected Screen screen;
	BitmapFont font;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		//batch.draw(img, 0, 0);
		font = new BitmapFont(Gdx.files.internal("data/font.fnt"), false);
		//font.setUseIntegerPositions(false);
		//font.setColor(0f, 0f, 0f, 1.0f);
		font.draw(batch, "some text on the screen", 50, 50);
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}

	@Override
	public void resize (int width, int height) {
		if (screen != null) screen.resize(width, height);
	}
}
