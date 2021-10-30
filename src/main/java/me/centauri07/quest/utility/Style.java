package me.centauri07.quest.utility;

import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Style {
    public static String main(String prefix, String message) {
        return color("&a&l" + prefix + " &8» &7" + message);
    }

    public static String error(String prefix, String message) {
        return color("&c&l" + prefix + " &8» &7" + message);
    }

    public static List<String> colorLines(Collection<String> lines) {
        List<String> newLines = new ArrayList<>();
        for (String line : lines)
            newLines.add(color(line));
        return newLines;
    }

    public static String color(String message) {
        return ChatColor.translateAlternateColorCodes('&', message.replaceAll("§", "&"));
    }
}
