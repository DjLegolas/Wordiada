package UILogic;

import sharedStructures.PlayerData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UserManager {

    private final ArrayList<PlayerData> m_UsersList;
    private final Object lockObj = new Object();

    public UserManager() {
        m_UsersList = new ArrayList<PlayerData>();
    }

    public void addUser(PlayerData newUser) {
        m_UsersList.add(newUser);
    }

    public void removeUser(String userName) {

        synchronized (this) {
            for (PlayerData user : m_UsersList) {
                if (userName.equals(user.getName())) {
                    m_UsersList.remove(user);
                    break;
                }
            }
        }
    }

    public boolean isUserExists(String nameTocheck) {
            boolean result = false;
            for (PlayerData user : m_UsersList) {
                if (nameTocheck.equals(user.getName())) {
                    result = true;
                }
            }
            return result;
    }

    public List getUsers() {
        return Collections.unmodifiableList(m_UsersList);
    }
}
