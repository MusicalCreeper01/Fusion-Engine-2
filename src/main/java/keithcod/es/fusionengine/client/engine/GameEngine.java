package keithcod.es.fusionengine.client.engine;

public class GameEngine implements Runnable {

    public static final int TARGET_FPS = 60;

    private final Window window;

    private final Thread gameLoopThread;

    private final Timer timer;

    private final IGameLogic gameLogic;

    private final Input input;

    public Window getWindow(){
        return window;
    }

    public GameEngine(String windowTitle, int width, int height, boolean vSync, IGameLogic gameLogic) throws Exception {
        gameLoopThread = new Thread(this, "GAME_LOOP_THREAD");
        window = new Window(windowTitle, width, height, vSync);
        input = new Input();
        this.gameLogic = gameLogic;
        timer = new Timer();
    }

    public void start() {
        String osName = System.getProperty("os.name");
        if ( osName.contains("Mac") ) {
            gameLoopThread.run();
        } else {
            gameLoopThread.start();
        }
    }

    @Override
    public void run() {
        try {
            init();
            gameLoop();
        } catch (Exception excp) {
            excp.printStackTrace();
        } finally {
            cleanup();
        }
    }



    protected void init() throws Exception {
        window.init();
        timer.init();
        input.init(window);
        gameLogic.init(window);
    }

    public static int fps = 0;

    public static int getFPS(){
        return fps;
    }

    protected void gameLoop() {

        long updateInterval = 1000 / TARGET_FPS;
        long updateTimer = System.currentTimeMillis();

        long lastTime = System.nanoTime();
        long now;
        long timer = System.currentTimeMillis();
        double delta = 0;
        int frames = 0;
        boolean running = true;
        while (running && !window.windowShouldClose()) {

            input();

            now = System.nanoTime();
            delta = (now - lastTime) / (1000000000f / TARGET_FPS);
            lastTime = now;

            //long currMillis = System.currentTimeMillis();
            //if(currMillis - updateTimer > updateInterval) {
            //    System.out.println(currMillis - updateTimer);
                update(delta); //interpolate using the delta
            //    updateTimer = currMillis;
            //}

            render();

            frames++;
            if (System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
//                System.out.println(frames + " fps");
                fps = frames;
                frames = 0;
            }
        }

        /*float elapsedTime;
        float accumulator = 0f;
        float interval = 1f / TARGET_UPS;

        boolean running = true;
        while (running && !window.windowShouldClose()) {
            elapsedTime = timer.getElapsedTime();
            accumulator += elapsedTime;

            input();

            while (accumulator >= interval) {
                update(interval);
                accumulator -= interval;
            }

            render();

            if (!window.isvSync()) {
                sync();
            }
        }*/
    }

    private void sync() {
        float loopSlot = 1f / TARGET_FPS;
        double endTime = timer.getLastLoopTime() + loopSlot;
        while (timer.getTime() < endTime) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException ie) {
            }
        }
    }

    protected void input() {
        input.input(window);
        gameLogic.input(window, input);
    }

    protected void update(double interval) {
        gameLogic.update(interval, input);
    }

    protected void render() {
        gameLogic.render(window);
        window.update();
    }

    protected void cleanup() {
        gameLogic.cleanup();
    }
}
