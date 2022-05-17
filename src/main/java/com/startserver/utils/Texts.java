package com.startserver.utils;

public enum Texts {

    TRACK_ALREADY_USED("Трек уже был выбран другим пользователем", "The track is used already by other user"),
    USER_NAME_BUSY("Пользователь с таким ником уже зарегистрирован", "The user with such nickname is already registered"),
    USER_NOT_FOUND("Пользователь не найден", "User not found"),
    UPDATE_APP("Обновите приложение", "Update application"),
    WORK_ON_SERVER("Проводятся работы на сервере, попробуйте позже", "Works on the server are carried out, please try again later"),
    WRONG_VERSION("Неизвестная версия приложения", "Unknown version of the application"),
    NOT_LOGIN("Вы не залогинены, попробуйте перезайти в приложение", "You are not login"),
    TRY_HARDER("Попробуй зайти на другой сайт, этот в покое оставь", "Try harder!"),
    WRONG_TIME("Время заканчивать период еще не пришло", "The time to end the period has not yet come"),
    WRONG_PROMO("Промокод неверен", "Promo code is not valid!"),
    WRONG_PASSWORD("Неверный пароль", "Wrong password");

    final String ruText;
    final String enText;

    Texts(String ruText, String enText) {
        this.ruText = ruText;
        this.enText = enText;
    }

    public String getText(String language) {
        if (language != null && language.equals("ru"))
            return ruText;
        else
            return enText;
    }
}
