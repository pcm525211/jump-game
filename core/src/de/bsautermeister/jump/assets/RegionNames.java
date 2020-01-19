package de.bsautermeister.jump.assets;

public class RegionNames {
    public static String SMALL_PLAYER_WALK_TPL = "small_player_*_walk";
    public static String SMALL_PLAYER_STAND_TPL = "small_player_*_stand";
    public static String SMALL_PLAYER_JUMP_TPL = "small_player_*_jump";
    public static String SMALL_PLAYER_CROUCH_TPL = "small_player_*_crouch";
    public static String SMALL_PLAYER_DEAD_TPL = "small_player_*_dead";
    public static String SMALL_PLAYER_TURN_TPL = "small_player_*_turn";
    public static String SMALL_PLAYER_VICTORY = "small_player_3_victory";
    public static String SMALL_PLAYER_BEER_VICTORY = "small_player_3_beer_victory";
    public static String SMALL_PLAYER_DROWN_TPL = "small_player_*_drown";

    public static String BIG_PLAYER_WALK_TPL = "big_player_*_walk";
    public static String BIG_PLAYER_STAND_TPL = "big_player_*_stand";
    public static String BIG_PLAYER_JUMP_TPL = "big_player_*_jump";
    public static String BIG_PLAYER_CROUCH_TPL = "big_player_*_crouch";
    public static String BIG_PLAYER_TURN_TPL = "big_player_*_turn";
    public static String BIG_PLAYER_DROWN_TPL = "big_player_*_drown";
    public static String BIG_PLAYER_DEAD_TPL = "big_player_*_dead";
    public static String BIG_PLAYER_VICTORY = "big_player_3_victory";
    public static String BIG_PLAYER_BEER_VICTORY = "big_player_3_beer_victory";

    public static String BIG_PLAYER_PREZELIZED_WALK_TPL = "big_player_prezelized_*_walk";
    public static String BIG_PLAYER_PREZELIZED_STAND_TPL = "big_player_prezelized_*_stand";
    public static String BIG_PLAYER_PREZELIZED_JUMP_TPL = "big_player_prezelized_*_jump";
    public static String BIG_PLAYER_PREZELIZED_CROUCH_TPL = "big_player_prezelized_*_crouch";
    public static String BIG_PLAYER_PREZELIZED_TURN_TPL = "big_player_prezelized_*_turn";

    public static String GOOMBA = "goomba";
    public static String GOOMBA_STOMP = "goomba_stomp";
    public static String KOOPA = "koopa";
    public static String KOOPA_MOVING = "koopa_moving";
    public static String FLOWER = "flower";
    public static String FISH = "fish";
    public static String SPIKY = "spiky";
    public static String MUSHROOM = "mushroom";
    public static String COIN = "coin";
    public static String BRICK_FRAGMENT = "brick_fragment";
    public static String WATER = "water";
    public static String PLATFORM2 = "platform2";
    public static String PLATFORM3 = "platform3";
    public static String PLATFORM4 = "platform4";
    public static String BREAK_PLATFORM2 = "break_platform2";
    public static String BREAK_PLATFORM3 = "break_platform3";
    public static String BREAK_PLATFORM4 = "break_platform4";
    public static String BEER = "beer";
    public static String PRETZEL = "pretzel";
    public static String PRETZEL_BULLET = "pretzel_bullet";
    public static String BACKGROUND_OVERLAY = "bg-overlay";

    public static String LOADING_LOGO = "title";
    public static String LOADING_ANIMATION = "loading";
    public static String LOADING_FRAME = "frame";
    public static String LOADING_BAR_HIDDEN = "loading-hidden";
    public static String LOADING_BACKGROUND = "screen-bg";
    public static String LOADING_FRAME_BACKGROUND = "frame-bg";

    private RegionNames() {}

    public static String fromTemplate(String template, int value) {
        return template.replace("*", String.valueOf(value));
    }
}
