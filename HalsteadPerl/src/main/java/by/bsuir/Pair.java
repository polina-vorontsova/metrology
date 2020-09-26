package by.bsuir;

public class Pair implements Comparable<Pair>{

    private final String key;
    private final int value;

    public Pair(String key, int value) {
        this.key = key;
        this.value = value;
    }


    public String getKey() {
        return key;
    }

    public int getValue() {
        return value;
    }

    @Override
    public int compareTo(Pair o) {
        return key.compareTo(o.key);
    }
}
