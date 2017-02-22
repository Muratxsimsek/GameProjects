package com.mygdx.game;

import android.os.Bundle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.backends.android.AndroidFragmentApplication;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.mygdx.game.LibgdxGame;

public class AndroidLauncher extends AndroidApplication   {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);


		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();

		initialize(new SnakeGame(), config);
	}


}
