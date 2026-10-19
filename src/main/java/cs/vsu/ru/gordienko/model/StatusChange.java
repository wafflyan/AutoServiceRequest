package cs.vsu.ru.gordienko.model;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Запись журнала переходов заявки
 * @param from предыдущий статус
 * @param to новый статус
 * @param at дата и время перехода
 */

public record StatusChange(OrderStatus from, OrderStatus to, LocalDateTime at) {
    /**
     * Проверка обязательных полей
     */
    public StatusChange {
        Objects.requireNonNull(from, "Новый статус не задан");
        Objects.requireNonNull(at, "Время перехода к другому статусу не задано");

    }
    @Override
    public String toString() {
        return at.toLocalDate() + " " + at.toLocalTime() + " " +  from + " -> " + to ;
    }
}
