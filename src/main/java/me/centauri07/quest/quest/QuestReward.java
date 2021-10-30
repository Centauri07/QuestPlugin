package me.centauri07.quest.quest;

import me.centauri07.quest.configuration.models.ItemStackModel;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QuestReward {
    public List<ItemStack> items = new ArrayList<>();
    public int exp;
    public int level;

    public QuestReward ofItem(Material material, int amount) {
        items.add(new ItemStack(material, amount));
        return this;
    }

    public QuestReward ofItem(ItemStackModel stack) {
        items.add(new ItemStack(Material.valueOf(stack.materialName), stack.amount));
        return this;
    }

    public QuestReward ofItems(ItemStackModel... stacks) {
        for (ItemStackModel stack : stacks) {
            items.add(new ItemStack(Material.valueOf(stack.materialName), stack.amount));
        }
        return this;
    }

    public QuestReward ofItem(ItemStack stack) {
        items.add(stack);
        return this;
    }

    public QuestReward ofItems(ItemStack... stacks) {
        items.addAll(Arrays.asList(stacks));
        return this;
    }

    public QuestReward ofExp(int amount) {
        exp = amount;
        return this;
    }

    public QuestReward ofLevel(int amount) {
        level = amount;
        return this;
    }
}