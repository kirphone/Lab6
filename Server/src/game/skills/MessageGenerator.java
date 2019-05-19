package game.skills;

import game.Person;

@FunctionalInterface
public interface MessageGenerator<T1, T2> {
    public String generate(Person p, T1 before, T2 changes);
}
