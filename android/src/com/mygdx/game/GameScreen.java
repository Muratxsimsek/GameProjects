package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Created by murat.simsek on 2/21/2017.
 */
public class GameScreen extends ScreenAdapter {
    private SpriteBatch batch;
    private Texture img;
    private Texture snakeHead;
    private Texture apple;

    private float MOVE_TIME = 0.4F;
    private float timer = MOVE_TIME;





    private int snakeX = 0, snakeY = 0;


    private static final int RIGHT = 0;
    private static final int LEFT = 1;
    private static final int UP = 2;
    private static final int DOWN = 3;
    private int snakeDirection = RIGHT;
    private boolean appleAvailable = false;
    private int appleX, appleY;
    private ShapeRenderer shapeRenderer;
    private static final int GRID_CELL = 128;
    private static final int SNAKE_MOVEMENT = 128;

    private Texture snakeBody;
    private Array<BodyPart> bodyParts = new Array<BodyPart>();
    private boolean hasHit = false;
    private boolean directionSet = false;
    private InputHandler.DirectionListener directionListener;
    private Viewport viewport;
    private Camera camera;

    private enum STATE {
        PLAYING, GAME_OVER
    }
    private STATE state = STATE.PLAYING;
    private static final float WORLD_WIDTH = GRID_CELL*(Gdx.graphics.getWidth()/GRID_CELL);
    private static final float WORLD_HEIGHT = GRID_CELL*(Gdx.graphics.getHeight()/GRID_CELL);;//Gdx.graphics.getHeight();

    private BitmapFont bitmapFont;
    private static final String GAME_OVER_TEXT = "Game Over!";
    private GlyphLayout layout = new GlyphLayout();

    @Override
    public void show() {
        bitmapFont = new BitmapFont();

        camera = new OrthographicCamera(WORLD_WIDTH+900,
                WORLD_HEIGHT+900);
        camera.position.set(WORLD_WIDTH / 2, WORLD_HEIGHT / 1.9f, 0);
        camera.update();
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        //viewport.apply(true);

        batch = new SpriteBatch();
        snakeHead = new Texture(Gdx.files.internal("snakehead.png"));
        apple = new Texture(Gdx.files.internal("apple.png"));
        shapeRenderer = new ShapeRenderer();


        snakeBody = new Texture(Gdx.files.internal("snakebody.png"));

        directionListener = new InputHandler.DirectionListener() {
            @Override
            public void onLeft() {
                snakeDirection = LEFT;
                //updateDirection(LEFT);
            }

            @Override
            public void onRight() {
                snakeDirection = RIGHT;
                //updateDirection(RIGHT);
            }

            @Override
            public void onUp() {
                snakeDirection = UP;
                //updateDirection(UP);
            }

            @Override
            public void onDown() {
                snakeDirection = DOWN;
                //updateDirection(DOWN);
            }

            @Override
            public void onLongPressed() {
                MOVE_TIME=0.1f;
            }

            @Override
            public void onTouchDown() {
                //MOVE_TIME=0.4f;
            }

            @Override
            public void onTouchUp() {
                MOVE_TIME=0.4f;
            }
        };

        InputHandler gd = new InputHandler(directionListener);
        Gdx.input.setInputProcessor(gd);





    }
    @Override
    public void render(float delta) {
        switch(state) {
            case PLAYING: {
                queryInput();
                updateSnake(delta);
                checkAppleCollision();
                checkAndPlaceApple();
            }
            break;
            case GAME_OVER: {

            }
            break;
        }
        clearScreen();
        drawGrid();
        draw();
    }
    private void updateDirection(int newSnakeDirection) {
        if (!directionSet && snakeDirection != newSnakeDirection) {
            directionSet = true;
            switch (newSnakeDirection) {
                case LEFT: {
                    updateIfNotOppositeDirection(newSnakeDirection, RIGHT);
                }
                break;
                case RIGHT: {
                    updateIfNotOppositeDirection(newSnakeDirection, LEFT);
                }
                break;
                case UP: {
                    updateIfNotOppositeDirection(newSnakeDirection, DOWN);
                }
                break;
                case DOWN: {
                    updateIfNotOppositeDirection(newSnakeDirection, UP);
                }
                break;
            }
        }
    }

    private void updateIfNotOppositeDirection(int newSnakeDirection, int
            oppositeDirection) {
        if (snakeDirection != oppositeDirection) snakeDirection =
                newSnakeDirection;
    }

    private void updateSnake(float delta) {
        timer -= delta;
        if (timer <= 0) {
            timer = MOVE_TIME;
            moveSnake();
            checkForOutOfBounds();
            updateBodyPartsPosition();
            checkSnakeBodyCollision();
            directionSet = false;
        }
    }

    private void checkSnakeBodyCollision() {
        for (BodyPart bodyPart : bodyParts) {
            if (bodyPart.x == snakeX && bodyPart.y == snakeY) state =
                    STATE.GAME_OVER;
        }
    }

    private void drawGrid() {
        shapeRenderer.setProjectionMatrix(camera.projection);
        shapeRenderer.setTransformMatrix(camera.view);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

        for (int x = 0; x < viewport.getWorldWidth(); x += GRID_CELL) {
            for (int y = 0; y < viewport.getWorldHeight(); y += GRID_CELL) {
                shapeRenderer.rect(x,y, GRID_CELL, GRID_CELL);
            }
        }
        shapeRenderer.end();
    }

    private void checkAndPlaceApple() {
        if (!appleAvailable) {
            do {
                appleX = MathUtils.random((int) (viewport.getWorldWidth() / SNAKE_MOVEMENT) - 1) * SNAKE_MOVEMENT;
                appleY = MathUtils.random((int) (viewport.getWorldHeight() / SNAKE_MOVEMENT) - 1) * SNAKE_MOVEMENT;

                appleAvailable = true;
            } while (appleX == snakeX && appleY == snakeY);
        }
    }


    private void draw() {
        batch.setProjectionMatrix(camera.projection);
        batch.setTransformMatrix(camera.view);
        batch.begin();
        batch.draw(snakeHead, snakeX, snakeY);
        for (BodyPart bodyPart : bodyParts) {
            bodyPart.draw(batch);
        }
        if (appleAvailable) {
            batch.draw(apple, appleX, appleY);
        }

        if (state == STATE.GAME_OVER) {
            layout.setText(bitmapFont, GAME_OVER_TEXT);
            bitmapFont.draw(batch, GAME_OVER_TEXT, (viewport.getWorldWidth() -
                    layout.width) / 2, (viewport.getWorldHeight() - layout.height) / 2);
        }

        batch.end();
    }

    private void clearScreen() {
        Gdx.gl.glClearColor(Color.BLACK.r, Color.BLACK.g,
                Color.BLACK.b, Color.BLACK.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    private void checkForOutOfBounds() {
        if (snakeX >= viewport.getWorldWidth()) {
            snakeX = 0;
        }
        if (snakeX < 0) {
            snakeX = (int) (viewport.getWorldWidth() - SNAKE_MOVEMENT);
        }
        if (snakeY >= viewport.getWorldHeight()) {
            snakeY = 0;
        }
        if (snakeY < 0) {
            snakeY = (int) (viewport.getWorldHeight() - SNAKE_MOVEMENT);
        }
    }
    private int snakeXBeforeUpdate = 0, snakeYBeforeUpdate = 0;
    private void moveSnake() {

        snakeXBeforeUpdate = snakeX;
        snakeYBeforeUpdate = snakeY;

        switch (snakeDirection) {
            case RIGHT: {
                snakeX += SNAKE_MOVEMENT;
                return;
            }
            case LEFT: {
                snakeX -= SNAKE_MOVEMENT;
                return;
            }
            case UP: {
                snakeY += SNAKE_MOVEMENT;
                return;
            }
            case DOWN: {
                snakeY -= SNAKE_MOVEMENT;
                return;
            }
        }
    }
    private void updateBodyPartsPosition() {
        if (bodyParts.size > 0) {
            BodyPart bodyPart = bodyParts.removeIndex(0);
            bodyPart.updateBodyPosition(snakeXBeforeUpdate,
                    snakeYBeforeUpdate);
            bodyParts.add(bodyPart);
        }
    }

    private void queryInput() {




    }

    private void checkAppleCollision() {
        if (appleAvailable && appleX == snakeX && appleY == snakeY) {
            BodyPart bodyPart = new BodyPart(snakeBody);
            bodyPart.updateBodyPosition(snakeX, snakeY);
            bodyParts.insert(0, bodyPart);
            appleAvailable = false;
        }
    }

    private class BodyPart {
        private int x, y;
        private Texture texture;
        public BodyPart(Texture texture) {
            this.texture = texture;
        }
        public void updateBodyPosition(int x, int y) {
            this.x = x;
            this.y = y;
        }
        public void draw(Batch batch) {
            if (!(x == snakeX && y == snakeY)) batch.draw(texture,
                    x, y);
        }
    }


}
