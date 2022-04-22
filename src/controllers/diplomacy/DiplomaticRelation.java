package controllers.diplomacy;

import models.Civilization;
import models.CivilizationPair;

public class DiplomaticRelation extends Diplomacy{
    double friendliness;

    public DiplomaticRelation(CivilizationPair pair){
        this.friendliness = 0;
        this.pair = pair;
    }

    public DiplomaticRelation(Civilization civ1, Civilization civ2){
        this.friendliness = 0;
        this.pair = new CivilizationPair(civ1, civ2);
    }

    public double getFriendliness(){
        return this.friendliness;
    }

    public void setFriendliness(double friendliness){
        this.friendliness = friendliness;
    }
}
