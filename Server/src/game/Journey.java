package game;

import game.skills.Changeable;
import game.skills.MessageGenerator;
import game.skills.Skill;
import java.util.*;

public class Journey {

    private Person pyatachok, kenga, robin, kroshka, puch, rabbit, bear;
    private SmartMap skills;
    private Song music;
    public Journey() {
        initializePersons();
        addSkills();
    }

    public void start() throws ParametersNullException {
        pyatachok.doSkill("openAndCloseMouth");
        pyatachok.doSkill("treat");
        kenga.sayTo(pyatachok, "рыбий жир очень, очень вкусный, когда к нему как следует привыкнешь.");
        pyatachok.changeName(new FIO("Генри-Пушель", "Пяточок"));
        pyatachok.doSkill("run");
        pyatachok.doSkill("roll");
        pyatachok.doSkill("addColors");
        pyatachok.goToPlace(Location.PYATACHOKHOME);
        pyatachok.stop();
        kenga.goToPlace(Location.FORREST);
        kroshka.goToPlace(Location.FORREST);
        for(int i = 0; i < 28; ++i){
            if(i % 7 == 1){
                System.out.print("Сегодня вторник. ");
                kroshka.goToFriend(rabbit);
            }
            if(i % 7 == 2){
                System.out.print("Сегодня среда. ");
                kroshka.goToPlace(Location.FORREST);
            }
        }
        kroshka.goToFriend(rabbit);
        kenga.teachSkills(puch, kenga.getSkill("jump"));
        pyatachok.goToFriend(robin);

        puch.goToPlace(Location.FORREST);
        bear.goToPlace(Location.FORREST);

        puch.changeName(new FIO("Винни-Пух", ""));

        music = new Song("Шумелка");
        puch.doSkill("composeMusic");
        puch.doSkill("scratchHead");
        puch.think("Начало просто замечательное, но где же взять вторую строчку?");
        //puch.sayTo(puch, "Ура Ура Ура");
        puch.getSkill("composeMusic").setChanges("Хорошо быть медведем, ого!");
        puch.doSkill("composeMusic");

        for(int i = 1; i < 4; ++i){
            puch.doSkill("composeMusic");
        }
    }

    private void initializePersons(){
        pyatachok = new Animal(new FIO("", "Пяточок"), Location.ROBINHOME);
        kenga = new Animal(new FIO("Кенга", ""), Location.ROBINHOME);
        kroshka = new Animal(new FIO("Крошка", "Ру"));
        puch = new Animal(new FIO("Пух", ""));
        robin = new Man(new FIO("Кристофер", "Робин"),  Location.ROBINHOME);
        rabbit = new Animal(new FIO("Кролик", ""), Location.RABBITHOME);
        bear = new Animal(new FIO("Медведи", ""));
    }

    static class SmartMap extends HashMap<String, Skill>{
        void put(Skill skill){
            super.put(skill.getName(), skill);
        }
        ArrayList<Skill> get(String ... names){
            ArrayList<Skill> skills = new ArrayList<>();
            for(String s : names){
                skills.add(super.get(s));
            }
            return skills;
        }
    }

    private class Initializator{
        void initialize(){

            class MutableBoolean {
                private boolean value;

                private MutableBoolean(boolean _value) {
                    value = _value;
                }

                private boolean getValue() {
                    return value;
                }

                private void setValue(boolean _value) {
                    value = _value;
                }
            }

            //Открытие и закрытие рта
            skills = new SmartMap();
            Changeable<MutableBoolean, Boolean> openAndCloseMouthChanges = ((isOpen, change)
                    -> isOpen.setValue(isOpen.getValue() ^ change));
            MessageGenerator<MutableBoolean, Boolean> openAndCloseMouthMessage = (p, before, changes) -> String.format("%s %s рот",
                    p.getName().toString(), before.getValue() ? "закрыл" : "открыл");
            skills.put(new Skill<>("openAndCloseMouth", new MutableBoolean(false),
                    openAndCloseMouthChanges, true, openAndCloseMouthMessage));
            //лечить
            Changeable<MutableDouble, Double> treatChanges = ((points, change)
                    -> points.setValue(points.getValue() + change));
            MessageGenerator<MutableDouble, Double> treatMessage = (p, before, changes) -> String.format("%s %s %s поинтов",
                    p.getName().toString(), changes > 0 ? "вылечился на" : "потерял", changes);
            skills.put(new Skill<>("treat", "points",
                    treatChanges, 5.0d, treatMessage));
            // Бег
            Changeable<MutableDouble, Double> runChanges = ((speed, mult) -> speed.setValue(speed.getValue() * mult));
            MessageGenerator<MutableDouble, Double> runMessage = (p, before, ch) -> String.format("Никогда в жизни %s не бегал так быстро, как сейчас! " +
                            "Он несся, не останавливаясь ни на секунду. Лишь в сотне шагов от дома он прекратил бег.",
                    p.getName().toString());
            skills.put(new Skill<>("run", "speed", runChanges, 10.0d, runMessage));
            // Добавление цветов
            Changeable<HashSet<Color>, ArrayList<Color>> addColorsChanges = ((before, changes) -> {
                before.addAll(changes);
                changes.clear();
            });
            MessageGenerator<HashSet<Color>, ArrayList<Color>> addColorsMessage = (p, before, changes) -> {
                StringBuilder result = new StringBuilder();
                result.append(String.format("%s обретает новые цвета :", p.getName().toString()));
                for(Color i : changes){
                    result.append(" ").append(i.toString());
                }
                return result.toString();
            };
            ArrayList<Color> allColors = new ArrayList<>(Arrays.asList(Color.values()));
            allColors.remove(0);
            skills.put(new Skill<>("addColors", new HashSet<>(),
                    addColorsChanges, allColors, addColorsMessage));
            // Катиться
            Changeable<MutableDouble, Double> rollChanges = ((speed, add) -> speed.setValue(speed.getValue() + add));
            skills.put(new Skill<>("roll", "speed", rollChanges, 10.0d,
                    (p, before, ch) -> String.format("%s покатился по земле", p.getName().toString())));
            //прыгать
            Changeable<MutableDouble, Double> jumpChanges = ((speed, add) -> speed.setValue(speed.getValue() * Math.pow(add, 2)));
            skills.put(new Skill<>("jump", "speed", jumpChanges, 5.0d, (p, before, ch)
                    -> String.format("%s прыгнул далеко", p.getName().toString())));
            //сочинять песни
            Changeable<Song, String> composeMusicChanges = (before, ch) -> before.addString(" " + ch);
            skills.put(new Skill<>("composeMusic", new Song("Шумелка"), composeMusicChanges,
                    "Хорошо быть медведем, ура!", (p, before, ch)
                    -> String.format("%s исполняет песню %s : %s", p.getName().toString(), before.getName(), before.getText())));
            //почесать в голове
            skills.put(new Skill<>("scratchHead", (p, before, changes)
                    -> String.format("%s почесал голову", p.getName())));
        }
    }

    private void addSkills(){
        Initializator init = new Initializator();
        init.initialize();
        pyatachok.addSkills(skills.get("openAndCloseMouth", "run", "addColors", "roll", "treat"));
        kenga.addSkills(skills.get("jump"));
        puch.addSkills(skills.get("composeMusic"));
        puch.addSkills(skills.get("scratchHead"));
    }

    private void addPosibleSkill(Person p, String name){
        SmartMap skills = p.getPossibleSkills();
        if(skills.size() == 0)
            return;
        if(new Random().nextInt(10) <= 3){
            p.addSkills(skills.get(name));
        }
    }

    @Override
    public String toString() {
        return "Это наше путешествие";
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
