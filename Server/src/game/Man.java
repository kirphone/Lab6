package game;

public class Man extends Person {
    Man(FIO _name) {
        super(_name);
    }

    Man(FIO _name, Location startLocation) {
        super(_name, startLocation);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Man && getName() == ((Man) obj).getName();
    }
}
