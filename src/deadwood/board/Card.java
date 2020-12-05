package deadwood.board;

import javafx.scene.image.Image;

import java.util.List;
import java.util.Random;

public class Card {

    private String name;
    private String image;
    private String description;
    private int number;
    //roles should be sorted in descending rank order
    private List<Role> roles;
    private int budget;
    private boolean shown;

    private static Random random = new Random();

    public Card(String name, String image, String description, int number, List<Role> roles, int budget) {
        this.name = name;
        this.image = image;
        this.description = description;
        this.number = number;
        this.roles = roles;
        this.budget = budget;
        shown = false;
    }

    public String getName() {
        return name;
    }

    public String showImage() {
        shown = true;
        return "/deadwood/img/cards/" + image;
    }

    public boolean isShown() {
        return shown;
    }

    public String hideImage() {
        shown = false;
        return "/deadwood/img/cards/CardBack-small.jpg";
    }

    public int getNumber() {
        return number;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public int getBudget() {
        return budget;
    }

}
