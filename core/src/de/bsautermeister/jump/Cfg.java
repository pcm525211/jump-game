package de.bsautermeister.jump;

import com.badlogic.gdx.utils.Logger;

public interface Cfg {
    int LOG_LEVEL = Logger.DEBUG;
    boolean DEBUG_MODE = true;

    float HUD_WIDTH = 1280; // still world units!
    float HUD_HEIGHT = 720; // still world units!

    int WORLD_WIDTH = 400;
    int WORLD_HEIGHT = 208;

    int BLOCK_SIZE = 16;

    float PPM = 100f;
}