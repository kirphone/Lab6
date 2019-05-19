package game;

import game.skills.Skill;

public class ParametersNullException extends java.lang.Exception {
    public ParametersNullException(Skill skill){
        super(String.format("Skill %s has target or change equal null", skill.toString()));
    }
}