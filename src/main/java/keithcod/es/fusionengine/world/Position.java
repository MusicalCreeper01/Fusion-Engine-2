package keithcod.es.fusionengine.world;

public class Position {

    private final float x;
    private final float y;
    private final float z;

    public Position(final float x, final float y, final float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ChunkPosition)) {
            return false;
        }

        final Position pair = (Position) o;

        if (x != pair.x) {
            return false;
        }
        if (y != pair.y) {
            return false;
        }
        if (z != pair.z) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int)x;
        result = 31 * result + ((int)y + (int)z);
        return result;
    }
}