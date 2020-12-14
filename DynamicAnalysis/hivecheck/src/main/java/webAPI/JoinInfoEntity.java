package webAPI;

import java.util.HashMap;
import java.util.Map;

public class JoinInfoEntity {
    private Map<String, Integer> keyMap1 = new HashMap<>();
    private Map<String, Integer> keyMap2 = new HashMap<>();

    public Map<String, Integer> getKeyMap1() {
        return keyMap1;
    }

    public void setKeyMap1(Map<String, Integer> keyMap1) {
        this.keyMap1 = keyMap1;
    }

    public Map<String, Integer> getKeyMap2() {
        return keyMap2;
    }

    public void setKeyMap2(Map<String, Integer> keyMap2) {
        this.keyMap2 = keyMap2;
    }
}
