import java.util.*;
public class Card {

    int budget;
    List<Role> roles = new ArrayList<>();
    String name;
    String image;


    public Card(String name, List<Role> list, int budget, String png){
        this.budget = budget;
        this.roles = list;
        this.name = name;
        this.image = png;
    }

    public int getBudget() {
        return budget;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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
