package game;

public class Song {

    private StringBuilder text;
    private final String name;

    public Song(String _name){
        this(_name, "");
    }

    public Song(String _name, String _text){
        text = new StringBuilder(_text);
        name = _name;
    }

    public void addString(String newString){
        text.append(newString);
    }

    public String getName(){
        return name;
    }

    public String getText() {return text.toString(); }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Song && ((Song) obj).getName().equals(name);
    }

    @Override
    public String toString() {
        return String.format("Песня под именем %s", name);
    }
}
