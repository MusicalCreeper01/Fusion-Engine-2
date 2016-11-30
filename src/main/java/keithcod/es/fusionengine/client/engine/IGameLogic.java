package keithcod.es.fusionengine.client.engine;

public interface IGameLogic {

    void init(Window window) throws Exception;

    void input(Window window, Input input);

    void update(double interval, Input input);

    void render(Window window);

    void cleanup();
}
