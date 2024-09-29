package spaceinvaders.game;

import  com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import  com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.Gdx;

public class Bullet extends GameObject{

    public Bullet (Main game, Vector2 _position, Texture img) {
        super(game);
        sprite = new Sprite(img);
        position = _position;
    }

    @Override
    public void Update(float delta) {
        float speed = 500;
        position.y += speed * delta;
        sprite.setPosition(position.x, position.y);

        if(position.y > Gdx.graphics.getHeight()) {
            alive = false;
        }
    }
}
