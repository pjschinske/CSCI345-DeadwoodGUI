package deadwood.board;

import deadwood.Player;

import java.util.List;
import java.util.Random;

//A Place that can hold a Scene
public class Set extends Place {
    private Card card;
    private List<Role> roles;
    private int shots;
    private int shotsLeft;
    private int cardX;
    private int cardY;

    private static Random random = new Random();

    public Set(String name, int shots, int cardX, int cardY) {
        super(name);
        this.shots = shots;
        this.shotsLeft = shots;
        this.cardX = cardX;
        this.cardY = cardY;
    }

    public void setCard(Card card) {
        this.card = card;
        if (card != null) {
            for (Role role: card.getRoles()) {
                role.setSceneLocation(cardX, cardY);
            }
        }
    }

    public Card getCard() {
        return card;
    }

    public boolean hasCard() {
        return card != null;
    }

    public void clearScene() {
        this.card = null;
    }

    public void reset() {
        shotsLeft = shots;
        clearScene();
    }

    public Role getRole(Player player) {
        for (Role role: roles)
            if (role.getPlayer() == player)
                return role;
        for (Role role: card.getRoles()) {
            if (role.hasPlayer() && role.getPlayer() == player)
                return role;
        }
        return null;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public void completeShot() {
        shotsLeft--;
    }

    public int getShotsCompleted() {
        return shots - shotsLeft;
    }

    public int getShotsLeft() {
        return shotsLeft;
    }

    public int getCardX() {
        return cardX;
    }

    public int getCardY() {
        return cardY;
    }
}
