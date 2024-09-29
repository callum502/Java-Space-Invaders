package spaceinvaders.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import java.util.ArrayList;

public class Player extends GameObject{
    public ArrayList<Bullet> bullets = new ArrayList<>();
    private Texture bulletImage;
    private float shootCooldown = 0.5f;
    private float shootTimer = shootCooldown;

    public Player(Main game, Texture img, Texture bulletImg) {
        super(game);
        sprite = new Sprite(img);
        bulletImage = bulletImg;
        position = new Vector2(Gdx.graphics.getWidth()/2f, 0);
    }

    @Override
    public void Update(float delta) {
        // Receive movement input
        float speed = 300;
        if(Gdx.input.isKeyPressed(Keys.A))
            position.x -= speed * delta;
        if(Gdx.input.isKeyPressed(Keys.D))
            position.x += speed * delta;


        // Keep player within screen bounds
        if (position.x <= 0)
            position.x = 0;

        if (position.x + sprite.getWidth() >= Gdx.graphics.getWidth())
            position.x = Gdx.graphics.getWidth() - sprite.getWidth();

        // Update the shoot timer
        shootTimer += delta;

        // Shoot bullet when player presses spacebar cooldown has passed
        if (Gdx.input.isKeyJustPressed(Keys.SPACE) && shootTimer >= shootCooldown) {
            Vector2 bulletPosition = new Vector2(position.x + sprite.getWidth() / 2, position.y);
            bullets.add(new Bullet(game, bulletPosition, bulletImage));
            game.shootSound.play(0.1f);
            shootTimer = 0.0f;
        }

        // Update bullets
        for (int i = bullets.size() - 1; i >= 0; i--) { //iterate backwards to avoid skipping bullets
            Bullet bullet = bullets.get(i);
            bullet.Update(delta);
            if (!bullet.alive) {
                bullets.remove(i);
            }
        }

        sprite.setPosition(position.x, position.y);
    }

    @Override //override to draw bullets as well as player
    public void Draw(SpriteBatch batch) {
        sprite.draw(batch);
        for (Bullet bullet : bullets) {
            bullet.sprite.draw(batch);
        }
    }
}
