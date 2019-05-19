package de.bsautermeister.jump;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import de.bsautermeister.jump.assets.AssetPaths;
import de.bsautermeister.jump.commons.GameApp;
import de.bsautermeister.jump.screens.LoadingScreen;

public class JumpGame extends GameApp {
    private SpriteBatch batch;

    public static final short NOTHING_BIT = 0;
    public static final short GROUND_BIT = 1;
    public static final short MARIO_BIT = 2;
    public static final short BRICK_BIT = 4;
    public static final short COIN_BIT = 8;
    public static final short DESTROYED_BIT = 16;
    public static final short ENEMY_BIT = 32;
    public static final short OBJECT_BIT = 64;
    public static final short ENEMY_HEAD_BIT = 128;
    public static final short ITEM_BIT = 256;
    public static final short MARIO_HEAD_BIT = 512;
    public static final short MARIO_FEET_BIT = 1024;
    public static final short ENEMY_SIDE_BIT = 2048;
    public static final short BLOCK_TOP_BIT = 4096;

    @Override
    public void create() {
        super.create();
        Gdx.app.setLogLevel(Application.LOG_DEBUG);

        batch = new SpriteBatch();

        getMusicPlayer().selectMusic(AssetPaths.Music.BACKGROUND_AUDIO);
        getMusicPlayer().setVolume(1.0f, true);

        setScreen(new LoadingScreen(this));
    }

    public SpriteBatch getBatch() {
        return batch;
    }
}
