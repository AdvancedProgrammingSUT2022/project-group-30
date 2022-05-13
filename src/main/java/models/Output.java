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

    public void times(Output other) {
        food *= (100 + other.getFood()) / 100.0;
        gold *= (100 + other.getGold()) / 100.0;
        production *= (100 + other.getProduction()) / 100.0;
    }

    public void setEqualTo(Output other) {
        food = other.getFood();
        gold = other.getGold();
        production = other.getProduction();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Output))
            return false;
        return this.food == ((Output) o).getFood() && this.gold == ((Output) o).getGold() && this.production == ((Output) o).getProduction();
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
