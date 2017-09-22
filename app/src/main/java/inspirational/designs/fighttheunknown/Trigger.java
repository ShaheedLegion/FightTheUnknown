package inspirational.designs.fighttheunknown;

/**
 * Created by shaheed on 2017/09/19.
 */
class Trigger {
    int delay;
    int elapsed;

    public Trigger(int delta) {
        delay = delta;
        elapsed = 0;
    }

    public boolean triggered(int delta) {
        elapsed += delta;
        return (elapsed >= delay);
    }

    public void reset() {
        elapsed = 0;
    }
}
