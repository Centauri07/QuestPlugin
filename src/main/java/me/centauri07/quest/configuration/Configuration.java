package me.centauri07.quest.configuration;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Configuration<T> {
    public static final List<Configuration<?>> configurations = new ArrayList<>();

    private final Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
    private final File file;
    public T model;

    public Configuration(File parent, T model, String name) {
        file = new File(parent, name + ".json");
        this.model = model;

        if (!file.getParentFile().exists()) file.getParentFile().mkdirs();

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            create();
        }

        load();

        configurations.add(this);
    }

    public void create() {
        try {
            model = (T) model.getClass().getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        save();
    }

    public void load() {
        FileReader reader;
        try {
            reader = new FileReader(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }
        model = gson.fromJson(reader, (Type) model.getClass());
        try {
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void save() {
        FileWriter writer;
        try {
            writer = new FileWriter(file);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        gson.toJson(model, writer);
        try {
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
