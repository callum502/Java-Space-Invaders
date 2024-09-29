package spaceinvaders.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;

public class Main extends ApplicationAdapter {
    private SpriteBatch batch;
    private Texture playerImage;
    private Texture bulletImage;
    private Texture alienImage;
    private BitmapFont font;
    public Sound shootSound;
    public Music music;
    public Player player;
    private AlienManager alienManager;
    public boolean gameOver = false;
    public int score = 0;
    public GlyphLayout layout;

    @Override
    public void create() {
        batch = new SpriteBatch();
        // Load textures in main so they only have to be loaded once
        playerImage = new Texture("player.png");
        bulletImage = new Texture("bullet.png");
        alienImage = new Texture("alien.png");
        font = new BitmapFont();
        layout = new GlyphLayout();

        font.setColor(0.4157f, 0.7451f, 0.1882f, 1.0f); //6abe30 green
        shootSound = Gdx.audio.newSound(Gdx.files.internal("shoot.mp3"));
        music = Gdx.audio.newMusic(Gdx.files.internal("music.mp3"));
        music.setLooping(true);
        music.play();
        music.setVolume(0.1f);

        restartGame(); // Initial game setup
    }

    public void update() {
        float deltaTime = Gdx.graphics.getDeltaTime();
        player.Update(deltaTime);
        alienManager.Update(deltaTime);
    }

    @Override
    public void render() {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);// Background color
        batch.begin();

        if (gameOver) {
            DrawGameOverUI();
            batch.end();

            // Check for "Enter" key to restart on game over screen
            if (Gdx.input.isKeyJustPressed(Keys.ENTER) && gameOver)
                restartGame();
            return; // Don't update/draw anything else if the game is over
        }

        update();

        // Draw player and aliens
        player.Draw(batch);
        for (Alien alien : alienManager.aliens) {
            if (alien.alive) alien.Draw(batch);
        }

        // Draw the score
        font.draw(batch, "Score: " + score, 10, Gdx.graphics.getHeight() - 10);
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        playerImage.dispose();
        bulletImage.dispose();
        font.dispose();
        shootSound.dispose();
        music.dispose();
    }

    private void restartGame() {
        // Reset player, alienManager, and score
        player = new Player(this, playerImage, bulletImage);
        alienManager = new AlienManager(this, alienImage);
        score = 0;
        gameOver = false;
        music.play();
    }

    private void DrawGameOverUI() {
        String scoreText = "Score: " + score;
        String gameOverText = "Game Over";
        String restartText = "Press Enter To Restart";

        // Score text
        layout.setText(font, scoreText);
        font.draw(batch, scoreText, (Gdx.graphics.getWidth() - layout.width) / 2f, Gdx.graphics.getHeight() / 2f + 50f);

        // Game over text
        layout.setText(font, gameOverText);
        font.draw(batch, gameOverText, (Gdx.graphics.getWidth() - layout.width) / 2f, Gdx.graphics.getHeight() / 2f);

        // Restart text
        layout.setText(font, restartText);
        font.draw(batch, restartText, (Gdx.graphics.getWidth() - layout.width) / 2f, Gdx.graphics.getHeight() / 2f - 50f);
    }
}
