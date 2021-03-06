package de.bsautermeister.jump.physics;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

import de.bsautermeister.jump.sprites.CollectableItem;
import de.bsautermeister.jump.sprites.DrunkenGuy;
import de.bsautermeister.jump.sprites.Enemy;
import de.bsautermeister.jump.sprites.Fox;
import de.bsautermeister.jump.sprites.Hedgehog;
import de.bsautermeister.jump.sprites.InteractiveTileObject;
import de.bsautermeister.jump.sprites.Item;
import de.bsautermeister.jump.sprites.Platform;
import de.bsautermeister.jump.sprites.Player;
import de.bsautermeister.jump.sprites.PretzelBullet;

public class WorldContactListener implements ContactListener {

    @Override
    public void beginContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        int collisionDef = fixtureA.getFilterData().categoryBits | fixtureB.getFilterData().categoryBits;

        Player player;
        Enemy enemy;
        PretzelBullet bullet;
        TaggedUserData<Item> taggedItem;
        InteractiveTileObject tileObject;
        TaggedUserData<Enemy> taggedEnemy;
        TaggedUserData<CollectableItem> taggedCollectableItem;
        switch (collisionDef) {
            case Bits.PLAYER_HEAD | Bits.BRICK:
            case Bits.PLAYER_HEAD | Bits.ITEM_BOX:
                player = (Player) resolveUserData(fixtureA, fixtureB, Bits.PLAYER_HEAD);
                tileObject = (InteractiveTileObject) resolveUserData(fixtureA, fixtureB, Bits.ITEM_BOX | Bits.BRICK);
                tileObject.onHeadHit(player);
                break;
            case Bits.ENEMY_HEAD | Bits.PLAYER_FEET:
                player = (Player) resolveUserData(fixtureA, fixtureB, Bits.PLAYER_FEET);
                enemy = (Enemy) resolveUserData(fixtureA, fixtureB, Bits.ENEMY_HEAD);
                if (player.getBody().getLinearVelocity().y < 0.1) {
                    enemy.onHeadHit(player);
                    player.pumpUp();
                }
                break;
            case Bits.ENEMY_SIDE | Bits.COLLIDER:
                taggedEnemy = (TaggedUserData<Enemy>) resolveUserData(fixtureA, fixtureB, Bits.ENEMY_SIDE);
                if (taggedEnemy.getUserData() instanceof Fox) {
                    ((Fox) taggedEnemy.getUserData()).waitAndThenChangeDirectionBySideSensorTag(taggedEnemy.getTag());
                } else if (taggedEnemy.getUserData() instanceof Hedgehog) {
                    ((Hedgehog) taggedEnemy.getUserData()).changeDirectionBySideSensorTag(taggedEnemy.getTag());
                }
                break;
            case Bits.ENEMY_SIDE | Bits.GROUND:
            case Bits.ENEMY_SIDE | Bits.PLATFORM:
            case Bits.ENEMY_SIDE | Bits.BRICK:
            case Bits.ENEMY_SIDE | Bits.ITEM_BOX:
                taggedEnemy = (TaggedUserData<Enemy>) resolveUserData(fixtureA, fixtureB, Bits.ENEMY_SIDE);
                if (taggedEnemy.getUserData() instanceof Fox) {
                    ((Fox) taggedEnemy.getUserData()).changeDirectionBySideSensorTag(taggedEnemy.getTag());
                } else if (taggedEnemy.getUserData() instanceof Hedgehog) {
                    ((Hedgehog) taggedEnemy.getUserData()).changeDirectionBySideSensorTag(taggedEnemy.getTag());
                }
                break;
            case Bits.PLAYER | Bits.ENEMY:
                player = (Player) resolveUserData(fixtureA, fixtureB, Bits.PLAYER);
                enemy = (Enemy) resolveUserData(fixtureA, fixtureB, Bits.ENEMY);
                player.hit(enemy);
                break;
            case Bits.ITEM | Bits.PLAYER:
                taggedCollectableItem = (TaggedUserData<CollectableItem>) resolveUserData(fixtureA, fixtureB, Bits.ITEM);
                player = (Player) resolveUserData(fixtureA, fixtureB, Bits.PLAYER);
                taggedCollectableItem.getUserData().collectBy(player);
                break;
            case Bits.PLAYER_GROUND | Bits.GROUND:
            case Bits.PLAYER_GROUND | Bits.PLATFORM:
            case Bits.PLAYER_GROUND | Bits.ITEM_BOX:
            case Bits.PLAYER_GROUND | Bits.BRICK:
                player = (Player) resolveUserData(fixtureA, fixtureB, Bits.PLAYER_GROUND);
                Object other = resolveUserData(fixtureA, fixtureB, ~Bits.PLAYER_GROUND);
                player.touchGround(other);
                break;
            case Bits.BLOCK_TOP | Bits.ENEMY:
                enemy = (Enemy) resolveUserData(fixtureA, fixtureB, Bits.ENEMY);
                tileObject = (InteractiveTileObject) resolveUserData(fixtureA, fixtureB, Bits.BLOCK_TOP);
                tileObject.steppedOn(enemy.getId());
                break;
            case Bits.BLOCK_TOP | Bits.ITEM:
                taggedItem = (TaggedUserData<Item>) resolveUserData(fixtureA, fixtureB, Bits.ITEM);
                tileObject = (InteractiveTileObject) resolveUserData(fixtureA, fixtureB, Bits.BLOCK_TOP);
                tileObject.steppedOn(taggedItem.getUserData().getId());
                break;
            case Bits.PLAYER | Bits.ENEMY_SIDE:
                taggedEnemy = (TaggedUserData<Enemy>) resolveUserData(fixtureA, fixtureB, Bits.ENEMY_SIDE);
                if (taggedEnemy.getUserData() instanceof DrunkenGuy) {
                    DrunkenGuy drunkenGuy = (DrunkenGuy) taggedEnemy.getUserData();
                    drunkenGuy.setBlocked(true);
                }
                break;
            case Bits.BULLET | Bits.GROUND:
            case Bits.BULLET | Bits.PLATFORM:
            case Bits.BULLET | Bits.BRICK:
            case Bits.BULLET | Bits.ITEM_BOX:
                bullet = (PretzelBullet) resolveUserData(fixtureA, fixtureB, Bits.BULLET);
                Vector2 contactPos = getAvgContactPosition(contact);
                angleVector.set(contactPos);
                float angle = angleVector.sub(bullet.getBody().getPosition()).angle();
                if (!(angle > 210 && angle < 330 || angle > 30 && angle < 150)) {
                    bullet.hitWall(contactPos);
                }
                break;
        }
    }

    private final Vector2 angleVector = new Vector2();
    private final Vector2 contactPositon = new Vector2();
    private Vector2 getAvgContactPosition(Contact contact) {
        contactPositon.set(0, 0);
        for (int i = 0; i < contact.getWorldManifold().getNumberOfContactPoints(); ++i) {
            contactPositon.add(contact.getWorldManifold().getPoints()[i]);
        }
        return contactPositon.scl(1f / contact.getWorldManifold().getNumberOfContactPoints());
    }

    @Override
    public void endContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        int collisionDef = fixtureA.getFilterData().categoryBits | fixtureB.getFilterData().categoryBits;

        Player player;
        Enemy enemy;
        TaggedUserData<Item> taggedItem;
        InteractiveTileObject tileObject;
        TaggedUserData<Enemy> taggedEnemy;
        switch (collisionDef) {
            case Bits.PLAYER_GROUND | Bits.GROUND:
            case Bits.PLAYER_GROUND | Bits.PLATFORM:
            case Bits.PLAYER_GROUND | Bits.ITEM_BOX:
            case Bits.PLAYER_GROUND | Bits.BRICK:
                player = (Player) resolveUserData(fixtureA, fixtureB, Bits.PLAYER_GROUND);
                Object other = resolveUserData(fixtureA, fixtureB, ~Bits.PLAYER_GROUND);
                player.leftGround(other);
                break;
            case Bits.BLOCK_TOP | Bits.ENEMY:
                enemy = (Enemy) resolveUserData(fixtureA, fixtureB, Bits.ENEMY);
                tileObject = (InteractiveTileObject) resolveUserData(fixtureA, fixtureB, Bits.BLOCK_TOP);
                tileObject.steppedOff(enemy.getId());
                break;
            case Bits.BLOCK_TOP | Bits.ITEM:
                taggedItem = (TaggedUserData<Item>) resolveUserData(fixtureA, fixtureB, Bits.ITEM);
                tileObject = (InteractiveTileObject) resolveUserData(fixtureA, fixtureB, Bits.BLOCK_TOP);
                tileObject.steppedOff(taggedItem.getUserData().getId());
                break;
            case Bits.PLAYER | Bits.ENEMY_SIDE:
                taggedEnemy = (TaggedUserData<Enemy>) resolveUserData(fixtureA, fixtureB, Bits.ENEMY_SIDE);
                if (taggedEnemy.getUserData() instanceof DrunkenGuy) {
                    DrunkenGuy drunkenGuy = (DrunkenGuy) taggedEnemy.getUserData();
                    drunkenGuy.setBlocked(false);
                }
        }
    }

    private Object resolveUserData(Fixture fixtureA, Fixture fixtureB, int categoryBits) {
        return ((fixtureA.getFilterData().categoryBits & categoryBits) != 0)
                ? fixtureA.getUserData() : fixtureB.getUserData();
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        int collisionDef = fixtureA.getFilterData().categoryBits | fixtureB.getFilterData().categoryBits;

        Player player;
        Enemy enemy;
        Platform platform;
        PretzelBullet pretzelBullet;
        switch (collisionDef) {
            case Bits.PLAYER | Bits.PLATFORM:
                player = (Player) resolveUserData(fixtureA, fixtureB, Bits.PLAYER);
                platform = (Platform) resolveUserData(fixtureA, fixtureB, Bits.PLATFORM);

                if (player.hasLastJumpThroughPlatformId()) {
                    if (platform.getId().equals(player.getLastJumpThroughPlatformId())) {
                        contact.setEnabled(false);
                    }
                } else if (player.getVelocityRelativeToGround().y > 0.5) {
                    player.setLastJumpThroughPlatformId(platform.getId());
                    contact.setEnabled(false);
                }
                break;

            case Bits.ENEMY | Bits.BULLET:
                // done in pre-solve to don't have an impulse from the pretzelBullet to the other object
                pretzelBullet = (PretzelBullet) resolveUserData(fixtureA, fixtureB, Bits.BULLET);
                enemy = (Enemy) resolveUserData(fixtureA, fixtureB, Bits.ENEMY);
                pretzelBullet.explode(pretzelBullet.getBody().getPosition());
                enemy.kill(true);
                contact.setEnabled(false);
                break;
            case Bits.ENEMY: // enemy with enemy
                ((Enemy) fixtureA.getUserData()).onEnemyHit((Enemy) fixtureB.getUserData());
                ((Enemy) fixtureB.getUserData()).onEnemyHit((Enemy) fixtureA.getUserData());
                contact.setEnabled(false);
                break;
        }
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
