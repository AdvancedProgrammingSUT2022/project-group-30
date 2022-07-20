package models.diplomacy;

import models.Civilization;
import models.CivilizationPair;
import models.interfaces.TurnHandler;

public class StepWiseGoldTransferContract extends Diplomacy implements TurnHandler {
    private final Civilization recipient;
    private final Civilization payer;
    private final double totalAmount;
    private final double totalTurns;
    private double turnsPast;

    public StepWiseGoldTransferContract(Civilization recipient, Civilization payer, double totalAmount,
                                        double totalTurns) {
        this.pair = new CivilizationPair(recipient, payer);
        this.recipient = recipient;
        this.payer = payer;
        this.totalAmount = totalAmount;
        this.totalTurns = totalTurns;
        this.turnsPast = 0;
    }

    public Civilization getRecipient() {
        return this.recipient;
    }

    public Civilization getPayer() {
        return this.payer;
    }

    public double getTotalAmount() {
        return this.totalAmount;
    }

    public double getTotalTurns() {
        return this.totalTurns;
    }

    public double getTurnsPast() {
        return this.turnsPast;
    }

    public void setTurnsPast(double turnsPast) {
        this.turnsPast = turnsPast;
    }

    public void goToNextTurn() {
        // TODO : transfer the gold!
    }
}
