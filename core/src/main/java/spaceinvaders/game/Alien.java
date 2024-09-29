package spaceinvaders.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Alien extends GameObject{
    public Vector2 initialPosition;

    public Alien (Main game, Vector2 _position, Texture img) {
        super(game);
        sprite = new Sprite(img);
        position = _position;
        initialPosition = _position;
    }
}
