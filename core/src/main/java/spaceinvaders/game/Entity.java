package spaceinvaders.game;

public class Entity
{
    protected Main game;
    public Entity(Main game) { // in a larger application would pass a gameState with only necessary data
        this.game = game;
    }

    public void Update(float delta) {
    }
}
