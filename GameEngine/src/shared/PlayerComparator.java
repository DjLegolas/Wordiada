package shared;

import SharedStructures.PlayerData;

import java.util.Comparator;

public class PlayerComparator implements Comparator {

    @Override
    public int compare(Object o1, Object o2) {
        PlayerData p1 = (PlayerData)o1;
        PlayerData p2 = (PlayerData)o2;
        return p2.getScore() - p1.getScore();
    }
}
