package spaceinvaders.game;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class GameObject extends Entity {
    public Sprite sprite;
    public Vector2 position;
    public boolean alive = true;

    public GameObject(Main game) {
        super(game);
    }

    public void Draw(SpriteBatch batch) {
        if(alive) sprite.draw(batch);
    }
}
