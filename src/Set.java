import java.util.*;

public class Set extends Room{
    int maxShots;
    int currentShots;
    List<Role> roles = new ArrayList<>();
    Card card;

    public Set(String name, int shots, List<Role> roles, Card card){
        this.name = name;
        this.maxShots = shots;
        this.currentShots = maxShots;
        this.roles = roles;
    }
}
