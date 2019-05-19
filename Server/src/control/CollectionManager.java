package control;

import game.Person;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class CollectionManager {
    private ConcurrentHashMap<String, Person> collection;
    private Date initDate;
    private File fileForIO;

    public CollectionManager() {
        collection = new ConcurrentHashMap<>();
        fileForIO = null;
        initDate = new Date();
    }

    public boolean isImported() {
        return fileForIO != null;
    }

    /**
     * Выводит информацию о коллекции
     */

    String info() {
        return String.format("Коллекция имеет тип HashSet и содержит объекты класса Person\n" +
                "Коллекция инициализировалась на основе следующих данных: %s\n" +
                "Коллекция содержит %d элементов\n", initDate.toString(), collection.size());
    }

    /**
     * Удаляет элемент по ключу
     *
     * @param key : (Things) - Remove key
     */

    boolean remove(String key) {
        return collection.remove(key) != null;
    }

    /**
     * Выводит все элементы коллекции.
     */

    public String show() {
        StringBuilder result = new StringBuilder();
        collection.forEach((key, value) -> result.append(key).append(" ").append(value.toString()).append("\n"));
        return result.toString();
    }

    /**
     * Метод удаляет из коллекции все элементы, превышающие заданный
     *
     * @param element (Person) - Object of class Person
     */

    public int removeGreater(Person element) {
        int beginSize = collection.size();
        collection.values().removeIf(i -> i.compareTo(element) > 0);
        return beginSize - collection.size();
    }

    /**
     * Метод удаляет из коллекции все элементы, ключ которых превышает заданный
     *
     * @param key : (String) - remove key
     */

    public int removeGreaterKey(String key) {
        int beginSize = collection.size();
        collection.keySet().removeIf(i -> i.compareTo(key) > 0);
        return beginSize - collection.size();
    }

    /**
     * Метод добавляет новый элемент в коллекцию, если его значение меньше, чем у наименьшего элемента этой коллекции
     *
     * @param element : (Person) - Object of class Person
     */

    public boolean addIfMin(Person element) {
        if (collection.values().stream().allMatch(a -> a.compareTo(element) > 0)) {
            insert(element.getName().toString().replace(" ", "_"), element);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Метод добавляет элемент для хранения в коллекции
     *
     * @param key     : (String) - Insert key
     * @param element : (Person) - Object of class Person
     */

    public void insert(String key, Person element) {
        collection.put(key, element);
    }


    /**
     * Импортирует коллекцию из строки
     *
     * @param importString - строка для чтения
     */


    public String importString(String importString){
        boolean res = readCSVFromString(importString);
        if (!res) {
            return "Добавлены все элементы";
        } else {
            return "Ничего не добавлено, возможно импортируемая коллекция пуста, или элементы заданы неверно";
        }
    }

    /**
     * Импортирует коллекцию из файла
     *
     * @param importFile:(java.io.File) - файл для чтения
     */

    public String importFile(File importFile) throws IOException {
        if (!importFile.isFile() && !importFile.createNewFile()) {
            throw new FileNotFoundException("Ошибка. Указаный путь не ведёт к файлу, " +
                    "и файл с таким названием не может быть создан");
        }
        //   if (!(importFile.exists()))
        //      throw new FileNotFoundException("Фаил коллекцией не найден. Добавьте элементы вручную или импортируйте из другого файла");
        if (!importFile.canRead()) throw new SecurityException("Доступ запрещён. Файл защищен от чтения");

        boolean res = readCSVFromFile(importFile);
        fileForIO = importFile;
        if (!res) {
            return "Добавлены все элементы из файла";
        } else {
            return "Ничего не добавлено, возможно импортируемая коллекция пуста, или элементы заданы неверно";
        }
    }

    /**
     * @param fileForRead:(java.io.File) - файл для чтения
     * @throws IOException - бросает ошибку ввода-вывода
     */

    private boolean readCSVFromFile(File fileForRead) throws IOException {
        try (InputStreamReader reader = new InputStreamReader(new FileInputStream(fileForRead))) {
            List<String> allLines = new BufferedReader(reader).lines().collect(Collectors.toList());
            return new CSVReaderAndWriter().read(allLines, collection);
        }
    }

    /**
     * @param stringForRead - файл для чтения
     */

    private boolean readCSVFromString(String stringForRead){
        List<String> allLines = Arrays.asList(stringForRead.trim().split("\n"));
        return new CSVReaderAndWriter().read(allLines, collection);
    }

    /**
     * Завершает работу с коллекцией элементов, сохраняя ее в фаил из которого она была считана.
     * Если сохранение в исходный фаил не удалось, то сохранение происходит в фаил с уникальным названием.
     */

 /*   String finishWork() {              //порядок в csv файле: name, speed, currentLoc, dateOfBirth
        File saveFile = (fileForIO != null) ? fileForIO : new File("");
        CSVReaderAndWriter csv = new CSVReaderAndWriter();
        try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(saveFile))) {
            writer.write(csv.write(collection));
            writer.flush();
            return "Коллекция сохранена в файл " + saveFile.getAbsolutePath();
        } catch (IOException | NullPointerException e) {
            saveFile = new File("saveFile" + new SimpleDateFormat("yyyy.MM.dd.hh.mm.ss").format(new Date()) + ".txt");
            try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(saveFile))) {
                if (saveFile.createNewFile()) throw new IOException();
                writer.write(csv.write(collection));
                writer.flush();
                return "Коллекция сохранена в файл " + saveFile.getAbsolutePath();

            } catch (IOException ex) {
                return "Сохранение коллекции не удалось";
            }
        }
    }*/

    String finishWork(String filePath){
        File saveFile = new File(filePath);
        CSVReaderAndWriter csv = new CSVReaderAndWriter();
        try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(saveFile))) {
            writer.write(csv.write(collection));
            writer.flush();
            return "Коллекция сохранена в файл " + saveFile.getAbsolutePath();
        } catch (IOException | NullPointerException e) {
            return "Сохранение коллекции не удалось\n" + e.toString();
        }
    }

    public ConcurrentHashMap<String, Person> getCollection(){
        return collection;
    }
}