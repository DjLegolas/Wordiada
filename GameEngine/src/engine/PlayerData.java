package engine;

import engine.Player.Type;

public class PlayerData {
    private String name;
    private Type playerType;
    private boolean isPlaying = false;

    public PlayerData(String name, String playerType){
        this.name = name;
        this.playerType = Type.valueOf(playerType.toUpperCase());
    }

    public Type getType() {
        return playerType;
    }

    public String getName() {
        return name;
    }

    public boolean getIsPlaying() {
        return isPlaying;
    }

    public void setIsPlaying(boolean isPlaying) {
        this.isPlaying = isPlaying;
    }

    public void setName(String i_Name) {
        this.name = i_Name;
    }

    public void setType(Type i_PlayerType) {
        this.playerType = i_PlayerType;
    }
}
