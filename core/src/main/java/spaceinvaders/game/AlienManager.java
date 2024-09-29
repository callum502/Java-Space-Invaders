package spaceinvaders.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

public class AlienManager extends Entity {
    private Texture alienImage;
    public ArrayList<Alien> aliens;
    private int numAliensWidth = 6;
    private int numAliensHeight = 2;
    private int alienSpacing = 50;
    private Vector2 alienPositionOffset;
    private int alienDirection = 1;
    private float alienSpeed = 100;
    private float addedSpeed = 0;

    public AlienManager(Main game, Texture alienImg) {
        super(game);
        alienImage = alienImg;
        aliens = new ArrayList<>();
        alienPositionOffset = new Vector2(0, 0);
        initializeAliens();
    }

    private void initializeAliens() {
        aliens.clear();
        for (int i = 0; i < numAliensHeight; i++) {
            for (int j = 0; j < numAliensWidth; j++) {
                Vector2 position = new Vector2(j * alienSpacing, i * alienSpacing);
                position.x += Gdx.graphics.getWidth() / 2f;
                position.y += Gdx.graphics.getHeight();
                position.x -= numAliensWidth / 2f * alienSpacing;
                position.y -= numAliensHeight * alienSpacing;
                aliens.add(new Alien(game, position, alienImage));
            }
        }
    }

    @Override
    public void Update(float deltaTime) {
        handleBulletCollisions();

        // If wave cleared, reset and increase difficulty
        if (numAliveAliens() == 0) {
            if(numAliensHeight <4) numAliensHeight++;
            initializeAliens(); // Reinitialize to add new row of aliens
            alienPositionOffset = new Vector2(0, 0);
            alienDirection = 1;
            addedSpeed+=20;
            alienSpeed = 100 + addedSpeed;
        }
        moveAliens(deltaTime);
    }

    // If bullet collides with alien, set alien to dead, remove bullet & increase score
    private void handleBulletCollisions() {
        for (int i = aliens.size() - 1; i >= 0; i--) { //iterate backwards to avoid skipping aliens
            Alien alien = aliens.get(i);
            if (alien.alive) {
                for (int j = game.player.bullets.size() - 1; j >= 0; j--) { //iterate backwards to avoid skipping bullets
                    Bullet bullet = game.player.bullets.get(j);
                    if (bullet.alive && bullet.sprite.getBoundingRectangle().overlaps(alien.sprite.getBoundingRectangle())) {
                        alien.alive = false;
                        bullet.alive = false;
                        game.score += 10;
                        return; //prevent bullet from hitting multiple aliens in single frame
                    }
                }
            }
        }
    }

    private void moveAliens(float deltaTime) {
        //move in X direction
        alienPositionOffset.x += alienDirection * alienSpeed * deltaTime;

        // Check if any aliens have reached edge of screen
        float leftMostAlienX = Float.MAX_VALUE;
        float rightMostAlienX = Float.MIN_VALUE;
        for (Alien alien : aliens) {
            if (alien.alive) {
                leftMostAlienX = Math.min(leftMostAlienX, alien.position.x);
                rightMostAlienX = Math.max(rightMostAlienX, alien.position.x + alien.sprite.getWidth());
            }
        }

        // when alien reaches edge, flip direction, move down and increase speed
        if (rightMostAlienX >= Gdx.graphics.getWidth() || leftMostAlienX <= 0) {
            alienDirection = leftMostAlienX <=0 ? 1 : -1; // cant *=-1 as flip can be triggered multiple times
            alienPositionOffset.y -= aliens.get(0).sprite.getHeight() * 0.25f;
            alienSpeed += 10;
        }

        // Update alien positions and check for player collision
        for (Alien alien : aliens) {
            alien.position = new Vector2(alien.initialPosition.x + alienPositionOffset.x, alien.initialPosition.y + alienPositionOffset.y);
            if (alien.alive) {
                if (alien.sprite.getBoundingRectangle().overlaps(game.player.sprite.getBoundingRectangle())
                    || alien.position.y < 0) { // For rare case when alien reaches bottom without colliding with player
                    game.gameOver = true;
                    game.music.stop();
                    return;
                }
            }
            alien.sprite.setPosition(alien.position.x, alien.position.y);
        }
    }

    private int numAliveAliens() {
        int count = 0;
        for (Alien alien : aliens) {
            if (alien.alive) count++;
        }
        return count;
    }
}
