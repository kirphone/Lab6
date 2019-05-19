package control;

import game.Animal;
import game.FIO;
import game.Location;
import game.Person;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CSVReaderAndWriter {    //порядок в csv файле: name, speed, currentLoc

    String write(Map<String, Person> collection){
        StringBuilder csvFormat = new StringBuilder();

        collection.forEach((key, value) -> csvFormat.append(key).append(",")
                .append(value.getName().toString().replace(" ", "_")).append(",")
                .append(value.getSpeed()).append(",")
                .append(value.getCurrentLocation()).append(",")
                .append(value.getDateOfBirth().getTime()).append("\n"));

        return csvFormat.toString();
    }

    class CSVReadException extends Exception{

        @Override
        public String toString() {
            return "Обнаружена ошибка в CSV формате. Либо вы используете другой формат, либо неправильно задали элемент.";
        }
    }

    boolean read(List<String> lines, Map<String, Person> collection){
        for (String str : lines) {
            try {
                String[] fields = str.split(",", 5);
                if (fields.length != 5) {
                    throw new CSVReadException();
                } else {
                    FIO name;
                    String[] splitter = fields[1].split("_", 2);
                    if (splitter.length == 2)
                        name = new FIO(splitter[0], splitter[1]);
                    else if (splitter.length == 1)
                        name = new FIO(splitter[0], null);
                    else
                        throw new CSVReadException();
                    Person p = new Animal(name, new Date(Long.parseLong(fields[4])), Location.valueOf(fields[3]));
                    p.setSpeed(Double.parseDouble(fields[2]));
                    collection.put(fields[0], p);
                }
            } catch (CSVReadException e) {
                System.out.println(e.toString());
                return true;
            } catch (NumberFormatException e){
                System.out.println("Числовые значения полей заданы неправильно.");
                return true;
            } catch (ArrayIndexOutOfBoundsException e){
                System.out.println("Локации с таким номером не существует");
                return true;
            }
        }
        return false;
    }
}