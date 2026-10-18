package cs.vsu.ru.gordienko.model;

import java.util.Objects;

/**
 * Работа с фиксированной ценой, к примеру диагностика.
 */
public record FixedPricedJob(WorkType type, String description, int normMinutes, Money price) implements Job {

    public FixedPricedJob{
        Objects.requireNonNull(type, "Вид работ не задан");
        Objects.requireNonNull(price, "Цена не задана");
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("Не задано описание работы");
        }
        if (normMinutes <= 0) {
            throw new IllegalArgumentException("Нормо-минут должно быть больше нуля:" + normMinutes);
        }
        description = description.strip();
    }

    /**
     * Стоимость = фиксированная цена
     */
    @Override
    public Money cost() {
        return price;
    }
}
