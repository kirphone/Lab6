package game.skills;

@FunctionalInterface
public interface Changeable<T1, T2> {
    void change(T1 before, T2 changes);
}
