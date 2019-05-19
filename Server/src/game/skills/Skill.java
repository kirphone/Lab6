package game.skills;

import game.ParametersNullException;
import game.Person;

import java.lang.reflect.Field;

public class Skill<T1, T2> {
    private T1 target;
    private T2 changes;
    private String name;
    private String targetField;
    private MessageGenerator<T1, T2> specialTextMessage;
    private Changeable<T1, T2> action;
    private Person subject;

    public Skill(String _name, T1 _target, Changeable<T1, T2> _action, T2 _changes,
                 MessageGenerator<T1, T2> _specialTextMessage) {
        name = _name;
        target = _target;
        action = _action;
        changes = _changes;
        specialTextMessage = _specialTextMessage;
    }

    public Skill(String _name, String _targetField, Changeable<T1, T2> _action, T2 _changes,
                 MessageGenerator<T1, T2> _specialTextMessage) {
        this(_name, (T1) null, _action, _changes, _specialTextMessage);
        targetField = _targetField;
    }

    public Skill(String _name, T1 _target) {
        this(_name, _target, null, null, null);
    }

    public Skill(String _name, MessageGenerator<T1, T2> _specialTextMessage) {
        this(_name, (T1) null, null, null, _specialTextMessage);
    }

    public void setTarget(T1 _target) {
        target = _target;
    }

    public String getName() {
        return name;
    }

    public T1 getTarget() {
        return target;
    }

    public void setSpecialTextMessage(MessageGenerator<T1, T2> message) {
        specialTextMessage = message;
    }

    private void printSpecialTextMessage() {
        try {
            String message = specialTextMessage.generate(subject, target, changes);
            System.out.println(message);
        } catch (NullPointerException e) {
            System.out.println("Не задан Person для скилла, но используется при выводе сообщения");
        }
    }

    public void setAction(Changeable<T1, T2> _action) {
        action = _action;
    }

    public void setChanges(T2 _changes) {
        changes = _changes;
    }

    public void perform() throws ParametersNullException {
        if (target == null && changes != null || target != null && changes == null) {
            throw new ParametersNullException(this);
        } else {
            try {
                action.change(target, changes);
                printSpecialTextMessage();
            } catch (NullPointerException ignored) {

            }
        }
    }

    public void setSubject(Person _subject) {
        subject = _subject;
        if (targetField != null) {
            try {
                Field f = Person.class.getDeclaredField(targetField);
                f.setAccessible(true);
                setTarget((T1) f.get(subject));
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public String toString() {
        return "Скилл под названием " + name;
    }

    public boolean equals(Object obj) {
        return obj instanceof Skill && ((Skill) obj).getName().equals(name);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}