package com.mygdx.game;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;

public class InputHandler extends GestureDetector {
    public interface DirectionListener {
        void onLeft();

        void onRight();

        void onUp();

        void onDown();

        void onLongPressed();

        void onTouchDown();

        void onTouchUp();
    }

    public InputHandler(DirectionListener directionListener) {
        super(new DirectionGestureListener(directionListener));
    }

    private static class DirectionGestureListener  implements GestureListener, InputProcessor {
        DirectionListener directionListener;

        public DirectionGestureListener(DirectionListener directionListener){
            this.directionListener = directionListener;
        }

        @Override
        public boolean fling(float velocityX, float velocityY, int button) {
            if(Math.abs(velocityX)>Math.abs(velocityY)){
                if(velocityX>0){
                    directionListener.onRight();
                }else{
                    directionListener.onLeft();
                }
            }else{
                if(velocityY>0){
                    directionListener.onDown();
                }else{
                    directionListener.onUp();
                }
            }
            return false;
        }

        @Override
        public boolean longPress(float x, float y) {
            directionListener.onLongPressed();
            return false;
        }


        @Override
        public boolean touchDown(float x, float y, int pointer, int button) {
            directionListener.onTouchDown();
            return false;
        }

        @Override
        public boolean tap(float v, float v1, int i, int i1) {
            return false;
        }

        @Override
        public boolean pan(float v, float v1, float v2, float v3) {
            return false;
        }

        @Override
        public boolean panStop(float v, float v1, int i, int i1) {
            return false;
        }

        @Override
        public boolean zoom(float v, float v1) {
            return false;
        }

        @Override
        public boolean pinch(Vector2 vector2, Vector2 vector21, Vector2 vector22, Vector2 vector23) {
            return false;
        }

        @Override
        public void pinchStop() {

        }

        @Override
        public boolean keyDown(int i) {
            return false;
        }

        @Override
        public boolean keyUp(int i) {
            return false;
        }

        @Override
        public boolean keyTyped(char c) {
            return false;
        }

        @Override
        public boolean touchDown(int i, int i1, int i2, int i3) {
            return false;
        }

        @Override
        public boolean touchUp(int i, int i1, int i2, int i3) {
            directionListener.onTouchUp();
            return false;
        }

        @Override
        public boolean touchDragged(int i, int i1, int i2) {
            return false;
        }

        @Override
        public boolean mouseMoved(int i, int i1) {
            return false;
        }

        @Override
        public boolean scrolled(int i) {
            return false;
        }
    }

}