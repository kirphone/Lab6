package server;

import game.Person;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class Response implements Serializable {

    private String actions;
    private ConcurrentHashMap<String, Person> collection;

    public Response(String _actions, ConcurrentHashMap<String, Person> _col){
        actions = _actions;
        collection = _col.entrySet().stream().sorted(Map.Entry.comparingByValue())                   //сортировка по умолчанию
                .collect(Collectors.toConcurrentMap(Map.Entry::getKey, Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, ConcurrentHashMap::new));
    }

    public String getActions() {
        return actions;
    }

    public ConcurrentHashMap<String, Person> getCollection() {
        return collection;
    }
}

