package jam.ld26.game;

import org.newdawn.slick.geom.Vector2f;

public class C {
    public static final boolean DEBUG_MODE = false;
    public static final boolean GOD_MODE = false;
    public static final int SCREEN_HEIGHT = 640;
    public static final int SCREEN_WIDTH = 800;

    public static enum Events {
        CLOSE_WINDOW("close_window"),
        MOVE_LEFT("move_left"),
        MOVE_RIGHT("move_right"),
        MOVE_UP("move_up"),
        MOVE_DOWN("move_down"),
        BACK("back"),
        CLICK_BUTTON("click_button"),
        CLICK_LEFT_EDITOR("click_left_editor"),
        CLICK_RIGHT_EDITOR("click_right_editor"),
        CROSSHAIR_MOVED("crosshair_moved"),
        NEXT_ZOMBIE("next_zombie"),
        PREV_ZOMBIE("prev_zombie"),
        LOAD_LEVEL("load_level"),
        SAVE_LEVEL("save_level"),
        ACTION("action");

        public String name;

        private Events(String name) {
            this.name = name;
        }
    }

    public static enum Textures {
        START_BACKGROUND("start_background", "resources/textures/start_background.png"),
        ZOMBIE("zombie", "resources/textures/zombie.png"),
        AVATAR("avatar", "resources/textures/zombie_0.png"),
        BUTTON_CREDITS("button_credits", "resources/textures/button_credits.png"),
        BUTTON_PLAY("button_play", "resources/textures/button_play.png"),
        BUTTON_INSTRUCTIONS("button_instructions", "resources/textures/button_instructions.png"),
        CROSSHAIR("crosshair", "resources/textures/crosshair.png"),
        WALL("wall", "resources/textures/wall.png"),
        DEFAULT_TILE_SET("default_tile_set", "resources/textures/tile_setbk.png"),
        TILE_SET("tile_set", "resources/textures/tile_set.png"),
        LOGO("logo", "resources/textures/logo.png"),
        ENEMY("enemy", "resources/textures/enemy.png");

        public String name;
        public String path;

        private Textures(String name, String path) {
            this.name = name;
            this.path = path;
        }
    }

    public static enum Entities {
        PLAYER("player"),
        LAZY_TRIANGLE_ENEMY("lazy"),
        
        
        ZOMBIE("zombie"),
        CROSSHAIR("crosshair"),
        ENEMY("enemy"),
        WALL("wall");

        public String name;

        private Entities(String name) {
            this.name = name;
        }
    }

    public static enum Groups {
        PLAYER("player"),
        LAZY_TRIANGLE_ENEMY("lazy"),
        
        
        BULLETS("bullets"),
        ZOMBIES("zombies"),
        BUTTONS("buttons"),
        ENEMIES("enemies"),
        WALLS("walls");

        public String name;

        private Groups(String name) {
            this.name = name;
        }
    }

    public static enum Sounds {
        ZOMBIE_BITE("zombie_bite", "resources/sounds/zombie4.wav"),
        ZOMBIE_GROWL("zombie_growl", "resources/sounds/zombie2.wav"),
        FIRE("fire", "resources/sounds/fire.wav"),
        MUSIC("music", "resources/music.ogg");

        public String name;
        public String path;

        private Sounds(String name, String path) {
            this.name = name;
            this.path = path;
        }
    }

    public static enum States {
        START_STATE("start", 0),
        MAIN_STATE("main", 1),
        PUBLISHER_STATE("publisher", 2),
        INSTRUCTIONS_STATE("instructions", 3),
        LEVEL_EDITOR_STATE("level_editor", 4),
        CREDITS_STATE("credits", 9);

        public String name;
        public int value;

        private States(String name, int value) {
            this.name = name;
            this.value = value;
        }
    }

    public static enum Logic {
        //Square Player
        RECTANGLE_INITIAL_SIZE(32),
        
        ZOMBIE_SPEED((float)0.15),
        ENEMY_SPEED((float)0.20),
        SELECT_OPTION_DELAY(500),
        CLICK_EDITOR_DELAY(250),
        PUBLISHER_TIME(2000),
        NEXT_LEVEL_TIME(1500),
        ENEMY_THINK_TIME(1000),
        TILE_SIZE(32);
        
        public Object data;

        private Logic(Object data) {
            this.data = data;
        }
    }

     public static enum Buttons {
        CREDITS(Textures.BUTTON_CREDITS.name, "", new Vector2f(150, 450), new Vector2f(45, 25)),
        START_GAME(Textures.BUTTON_PLAY.name, "", new Vector2f(340, 450), new Vector2f(45, 25)),
        INSTRUCTIONS(Textures.BUTTON_INSTRUCTIONS.name, "", new Vector2f(550, 450), new Vector2f(45, 25));

        public String textureName;
        public String label;
        public Vector2f position;
        public Vector2f labelPosition;

        Buttons(String textureName, String label, Vector2f position, Vector2f labelPosition) {
            this.textureName = textureName;
            this.label = label;
            this.position = position;
            this.labelPosition = labelPosition;
        }
    }
}