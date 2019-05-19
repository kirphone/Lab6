package game;

import game.skills.*;

import java.io.Serializable;
import java.util.*;

public abstract class Person implements Comparable<Person>, Serializable {
    private FIO name;
    private MutableDouble speed;
    private Location currentLoc;
    private Journey.SmartMap skills;
    private MutableDouble points;
    private Map<Skill, Person> teachers;
    private Journey.SmartMap possibleSkills;
    private final Date dateOfBirth;

    public Person(FIO _name, Date _dateOfBirth, Location _startPosition) {
        name = _name;
        skills = new Journey.SmartMap();
        teachers = new HashMap<>();
        speed = new MutableDouble(1.0d);
        points = new MutableDouble(100.0d);
        possibleSkills = new Journey.SmartMap();
        dateOfBirth = _dateOfBirth;
        currentLoc = _startPosition;
    }

    public Person(FIO _name, Location _startPosition){
        this(_name, new Date(), _startPosition);
    }

    public Person(FIO _name){
        this(_name, new Date(), null);
    }

    void doSkill(String skillName) throws ParametersNullException {
        if (skills.containsKey(skillName)) {
            skills.get(skillName).perform();
        } else {
            throw new SkillNotFoundException(this, skillName);
        }
    }

    public FIO getName() {
        return name;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public double getSpeed() {
        return speed.getValue();
    }

    public void setSpeed(double _speed) {
        speed.setValue(_speed);
    }

    public MutableDouble getMutableSpeed() {
        return speed;
    }

    void stop() {
        speed.setValue(0.0d);
    }

    void goToFriend(Person friend) {
        currentLoc = friend.currentLoc;
        System.out.println(name + " теперь проводит время вместе с " + friend.getName().toString());
    }

    void goToPlace(Location loc) {
        currentLoc = loc;
        System.out.println(name + " теперь находится в месте " + loc.toString());
    }

    void think(String text) {
        System.out.printf("%s подумал: %s\n", this.getName().toString(), text);
    }

    public void addSkills(Skill... newSkills) {
        ArrayList<Skill> operands = new ArrayList<>();
        Collections.addAll(operands, newSkills);
        addSkills(operands);
    }

    public void addPossibleSkill(Skill... skills) {
        for (Skill skill : skills) {
            possibleSkills.put(skill);
        }
    }

    Journey.SmartMap getPossibleSkills() {
        return possibleSkills;
    }

    void addSkills(ArrayList<Skill> newSkills) {
        for (Skill i : newSkills) {
            if (skills.containsKey(i.getName())) {
                System.out.printf("%s не получит новое умение %s, потому что он уже имеет его",
                        this.getName(), i.toString());
                continue;
            }
            skills.put(i);
            skills.get(i.getName()).setSubject(this);
            System.out.printf("%s получил новое умение - %s\n", this.getName().toString(), i.toString());
        }
    }

    void teachSkills(Person person, Skill... newSkills) {
        person.addSkills(newSkills);
        for (Skill i : newSkills) {
            person.teachers.put(i, this);
        }
    }

    void sayTo(Person p, String message) {
        System.out.printf("%s сказал %s : %s\n", this.name.toString(), p.name.toString(), message);
    }

    void changeName(FIO _name) {
        name = _name;
    }

    Skill getSkill(String name) {
        return skills.get(name);
    }

    public int getSkillsCount() {
        return skills.size();
    }

    public int getPossibleSkillsCount() {
        return possibleSkills.size();
    }

    public Location getCurrentLocation() {
        return currentLoc;
    }

    @Override
    public int compareTo(Person person) {
        if (this.speed.compareTo(person.speed) != 0)
            return this.speed.compareTo(person.speed);
        else if (this.currentLoc.compareTo(person.currentLoc) != 0)
            return this.currentLoc.compareTo(person.currentLoc);
        else if (dateOfBirth.compareTo(person.getDateOfBirth()) != 0)
            return dateOfBirth.compareTo(getDateOfBirth());
        else
            return this.name.toString().compareTo(person.name.toString());
    }

    @Override
    public String toString() {
        return name.toString();
    }

    @Override
    public abstract boolean equals(Object obj);

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}