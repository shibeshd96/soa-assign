package de.uniba.dsg.jaxrs.db;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.lang.reflect.Type;

public class JsonFileHandler<T> {
    private final String fileName;

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public JsonFileHandler(String fileName) {
        this.fileName = fileName;
    }

    public void writeData(T data, Type bottleListType) throws IOException {
        try (Writer writer = new FileWriter(fileName)) {
            gson.toJson(data, writer);
        }
    }

    public T readData(Type type) throws IOException {
        try (Reader reader = new FileReader(fileName)) {
            return gson.fromJson(reader, type);
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException("Data file not found.");
        }
    }
}