package me.centauri07.quest.configuration.models;

public class QuestModel {
    public String name;
    public boolean enabled = true;
    public int quantity = 10;
    public QuestRewardModel questReward = new QuestRewardModel();
}