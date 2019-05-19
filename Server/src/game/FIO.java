package game;

import java.io.Serializable;

public class FIO implements Serializable {
    private final String firstName;
    private final String secondName;

    public FIO(String _firstName, String _secondName){
        firstName = _firstName;
        secondName = _secondName;
    }

    public String getFirstName(){
        return firstName;
    }

    public String getSecondName(){
        return secondName;
    }

    @Override
    public String toString() {
        if(firstName.equals(""))
            return secondName;
        if(secondName.equals(""))
            return firstName;
        return firstName + " " + secondName;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null){
            return this.getFirstName() == null && this.getSecondName() == null;
        }
        if(!(obj instanceof FIO))
            return false;
        if(((FIO)obj).getFirstName() == null && firstName != null)
            return false;
        else if(((FIO)obj).getFirstName() != null && !((FIO)obj).getFirstName().equals(firstName))
            return false;
        if(((FIO)obj).getSecondName() == null && secondName != null)
            return false;
        else if(((FIO)obj).getSecondName() != null && !((FIO)obj).getFirstName().equals(firstName))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
