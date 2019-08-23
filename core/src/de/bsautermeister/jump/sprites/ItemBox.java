package de.bsautermeister.jump.sprites;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import de.bsautermeister.jump.Cfg;
import de.bsautermeister.jump.GameCallbacks;
import de.bsautermeister.jump.physics.Bits;

public class ItemBox extends InteractiveTileObject {

    public enum Type {
        COIN,
        MUSHROOM
    }

    private final static int BLANK_COIN_IDX = 28;

    private Type type;
    private int remainingItems;

    private static TiledMapTileSet tileSet;

    public ItemBox(GameCallbacks callbacks, World world, TiledMap map, MapObject mapObject) {
        super(callbacks, Bits.ITEM_BOX, world, map, mapObject);
        tileSet = map.getTileSets().getTileSet("tileset");
        Boolean multiCoin = (Boolean) mapObject.getProperties().get("multi_coin");
        Boolean mushroom = (Boolean) mapObject.getProperties().get("mushroom");
        if (multiCoin != null && multiCoin) {
            type = Type.COIN;
            remainingItems = 5;
        } else if (mushroom != null && mushroom) {
            type = Type.MUSHROOM;
            remainingItems = 1;
        } else {
            type = Type.COIN;
            remainingItems = 1;
        }
    }

    @Override
    public void onHeadHit(Mario mario) {
        float xDistance = Math.abs(mario.getBody().getWorldCenter().x - getBody().getWorldCenter().x);
        boolean closeEnough = xDistance < Cfg.BLOCK_SIZE / 2 / Cfg.PPM;
        getCallbacks().hit(
                mario,
                this,
                new Vector2(getBody().getPosition().x, getBody().getPosition().y),
                closeEnough);

        if(closeEnough && !isBlank()) {
            // apply effect to objects on top
            for (String objectOnTop : getObjectsOnTop()) {
                getCallbacks().indirectObjectHit(this, objectOnTop);

                // ensure that object is un-registered from objects on top, because Box2D does not seem
                // to call endContact anymore
                steppedOff(objectOnTop);
            }

            remainingItems--;
            updateCellBlankState();
            bumpUp();
        }
    }

    public boolean isMushroomBox() {
        return type == Type.MUSHROOM;
    }

    public boolean isBlank() {
        return remainingItems <= 0;
    }

    private void updateCellBlankState() {
        if (isBlank()) {
            getCell().setTile(
                    new DynamicTiledMapTile(
                            tileSet.getTile(BLANK_COIN_IDX)));
        }
    }

    @Override
    public void write(DataOutputStream out) throws IOException {
        super.write(out);
        out.writeInt(remainingItems);
        out.writeUTF(type.name());
    }

    @Override
    public void read(DataInputStream in) throws IOException {
        super.read(in);
        remainingItems = in.readInt();
        type = Enum.valueOf(Type.class, in.readUTF());

        updateCellBlankState();
    }
}