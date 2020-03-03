import java.util.*;
public class Card {

    int budget;
    List<Role> roles = new ArrayList<>();
    String name;
    String png;


    public Card(String name, List<Role> list, int budget, String png){
        this.budget = budget;
        this.roles = list;
        this.name = name;
        this.png = png;
    }

    public int getBudget() {
        return budget;
    }

    public void setBudget(int budget) {
        this.budget = budget;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
