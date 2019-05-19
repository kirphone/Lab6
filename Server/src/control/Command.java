package control;

import java.io.Serializable;

public class Command implements Serializable {
    private String name;
    private String body;
    private int size;
    public Command(String command){
        if (command == null)
            return;
        String[] fullCommand = command.trim().split(" ", 2);
        name = fullCommand[0];
        size = fullCommand.length;
        if(size == 2)
            body = fullCommand[1];
    }

    public String getName() {
        return name;
    }

    public String getBody() {
        return body;
    }

    public int getSize(){
        return  size;
    }

    public void setBody(String _body){
        body = _body;
    }

    @Override
    public String toString() {
        return name + " " + body;
    }
}

