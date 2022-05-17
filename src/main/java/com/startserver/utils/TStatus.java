package com.startserver.utils;

public enum TStatus {

    TRACKLISTENER(0, "TRACKLISTENER"),   // базовый
    TRACKVIP(1, "TRACKVIP"),     // ввел промокод
    TRACKKING(2, "TRACKKING"),       // 1 трек попал в топ
    TRACKKING_SECOND(3, "TRACKKING"),       // 1 трек попал в топ
    TRACKDEALER(4, "TRACKDEALER");     // 3 треков попало в топ

    public static TStatus getStatusByName(String name) {
        for (TStatus tStatus : values()) {
            if (tStatus.getName().equals(name))
                return tStatus;
        }
        return TRACKLISTENER;
    }

    public static TStatus getStatusByLevel(int level) {
        for (TStatus tStatus : values()) {
            if (tStatus.getLevel() == level)
                return tStatus;
        }
        return TRACKDEALER;
    }

    public static String getUpgradedStatus(String oldStatus, boolean promo) {
        TStatus tStatus = getStatusByName(oldStatus);
        int nextLevel;
        if(promo)
             nextLevel = tStatus.getLevel() + 1;
        else
            nextLevel = tStatus.getLevel() + 2;
        TStatus newStatus = getStatusByLevel(nextLevel);
        return newStatus.getName();
    }

    private int level;
    private String name;

    TStatus(int level, String name) {
        this.level = level;
        this.name = name;
    }

    public int getLevel() {
        return level;
    }

    public String getName() {
        return name;
    }
}
