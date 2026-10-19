package cs.vsu.ru.gordienko.model;

import java.util.Objects;

/**
 * Клиент автосервиса
 * @param name имя клиента
 * @param phone номер телефона клиента, в нем только символы 0-9 и +
 */
public record Client(String name, String phone) {
    /**
     * Проверяет заполненные поля
     * @throws IllegalArgumentException если нет имени или телефона
     */
    public Client {
        Objects.requireNonNull(name, "name");
        Objects.requireNonNull(phone, "phone");
        name = name.strip();
        phone = phone.replaceAll("^0-9+ ", "");
        if (name.isEmpty()) {
            throw new IllegalArgumentException("Нет имени клиента");
        }
        if (phone.isEmpty()) {
            throw new IllegalArgumentException("Нет телефона клиента");
        }
    }
    @Override
    public String toString() {
        return name + "(" + phone + ")";
    }
}
