package com.mygdx.game;

/**
 * Created by murat.simsek on 2/21/2017.
 */
import com.badlogic.gdx.Game;

public class SnakeGame extends Game {
    @Override
    public void create() {
        setScreen(new GameScreen());
    }
}