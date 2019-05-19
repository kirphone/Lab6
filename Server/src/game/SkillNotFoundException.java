package game;

class SkillNotFoundException extends RuntimeException{
    public SkillNotFoundException(Person p, String skillname){
        super(String.format("У %s нет скилла под названием %s", p.getName().toString(), skillname));
    }
}