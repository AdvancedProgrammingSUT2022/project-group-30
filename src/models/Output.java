package models;

public class Output {
    private int gold;
    private int food;
    private int production;

    public Output(int gold, int food, int production) {
        this.gold = gold;
        this.food = food;
        this.production = production;
    }

    public void add(Output other) {
        food += other.getFood();
        gold += other.getGold();
        production += other.getProduction();
    }

    public void subtract(Output other) {
        food -= other.getFood();
        gold -= other.getGold();
        production -= other.getProduction();
    }

    public void setEqualTo(Output other) {
        food = other.getFood();
        gold = other.getGold();
        production = other.getProduction();
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    public int getGold() {
        return this.gold;
    }

    public void setFood(int food) {
        this.food = food;
    }

    public int getFood() {
        return this.food;
    }

    public void setProduction(int production) {
        this.production = production;
    }

    public int getProduction() {
        return this.production;
    }

}
