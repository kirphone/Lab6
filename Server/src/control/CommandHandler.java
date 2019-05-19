package control;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import game.Animal;
import game.Person;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

public class CommandHandler {
    private CollectionManager manager;

    public CommandHandler(CollectionManager collectionManager) {
        manager = new CollectionManager();
        if (collectionManager != null) manager = collectionManager;
    }

    private Person getElementFromJSON(Gson gson, String elementInString) throws ReadElementFromJsonException, JsonSyntaxException {
        Person element = gson.fromJson(elementInString, Animal.class);
        if (element == null || element.getName() == null || element.getMutableSpeed() == null
                || element.getCurrentLocation() == null || element.getDateOfBirth() == null) {
            throw new ReadElementFromJsonException();
        }
        return element;
    }

    private String getKeyFromJSON(Gson gson, String stringInJson) throws ReadKeyFromJsonException, JsonSyntaxException {
        String key = gson.fromJson(stringInJson, String.class);
        if (key == null) {
            throw new ReadKeyFromJsonException();
        }
        return key;
    }

    private class ReadElementFromJsonException extends Exception {
        public String toString() {
            return "Ошибка, элемент задан неверно, возможно вы указали не все значения.\n" +
                    "Требуется указать следующие поля: name, speed, currentLoc, dateOfBirth.\n";
        }
    }

    private class ReadKeyFromJsonException extends Exception {
        public String toString() {
            return "Ошибка, задан пустой ключ. Коллекция не работает с пустыми ключами.\n";
        }
    }

    public String doCommand(Command command) {
        switch (command.getName()) {
            case "insert":
            case "add_if_min":
            case "remove":
            case "remove_greater":
            case "remove_greater_key":
            case "load":
            case "save":
            case "import":
                if (command.getSize() == 1) {
                    return "Ошибка, команда " + command.getName() + " должна иметь аргумент.";
                }
                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

                switch (command.getName()) {
                    case "remove":
                    case "remove_greater_key":
                    case "load":
                    case "save":
                        try {
                            String key = getKeyFromJSON(gson, command.getBody());
                            switch (command.getName()) {
                                case "remove":
                                    return manager.remove(key) ? "Элемент удалён" : "Коллекция не содержит данный элемент";
                                case "remove_greater_key":
                                    return String.format("Удалено %d элементов\n", manager.removeGreaterKey(key));
                                case "load":
                                    return manager.importFile(new File(key));
                                case "save":
                                    return manager.finishWork(key);
                            }
                        } catch (JsonSyntaxException ex) {
                            return "Ошибка, ключ задан неверно. Используйте формат JSON.";
                        } catch (ReadKeyFromJsonException | IOException ex) {
                            return ex.toString();
                        }
                    case "remove_greater":
                    case "add_if_min":
                        try {
                            Person element = getElementFromJSON(gson, command.getBody());
                            switch (command.getName()) {
                                case "remove_greater":
                                    return String.format("Удалено %d элементов\n", manager.removeGreater(element));
                                case "add_if_min":
                                    return manager.addIfMin(element) ? "Элемент добавлен" : "Элемент не добавлен";
                            }
                        } catch (JsonSyntaxException ex) {
                            return "Ошибка, элемент задан неверно. Используйте формат JSON.";
                        } catch (ReadElementFromJsonException ex) {
                            return ex.toString();
                        }
                    case "insert":
                        String[] args = command.getBody().split(" ", 2);
                        if (args.length == 1) {
                            return "Ошибка, команда " + command.getName() + " должна иметь 2 аргумента.";
                        }
                        try {
                            String key = getKeyFromJSON(gson, args[0]);
                            Person element = getElementFromJSON(gson, args[1]);
                            manager.insert(key, element);
                            return "Элемент добавлен";
                        } catch (JsonSyntaxException ex) {
                            return "Ошибка, элемент задан неверно. Используйте формат JSON.\n" + ex.toString();
                        } catch (ReadElementFromJsonException | ReadKeyFromJsonException ex) {
                            return ex.toString();
                        }

                    case "import":
                        return manager.importString(command.getBody());
                }
                break;
            case "info":
                return manager.info();
            case "show":
                return manager.show();

            /*case "stop_server":
                manager.show();
                manager.finishWork();
                break;*/
            default:
                return "Ошибка, Неизвестная команда.";
        }
        return null;
    }

    public ConcurrentHashMap<String, Person> getCollection(){
        return manager.getCollection();
    }
}