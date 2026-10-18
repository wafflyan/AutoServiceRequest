package cs.vsu.ru.gordienko.model;

import java.util.Objects;

/**
 *
 * @param type вид работ
 * @param description описание
 * @param normMinutes нормо-минуты
 * @param hourRate цена одного нормо-часа
 */
public record LaborJob(WorkType type, String description, int normMinutes, Money hourRate) implements Job {

    /**
     * Проверяет поля
     * @throws NullPointerException если вид или цена не заданы
     * @throws IllegalArgumentException если описание пустое или количество нормо-минут не положительно
     */
    public LaborJob {
        Objects.requireNonNull(type, "type");
        Objects.requireNonNull(hourRate, "hourRate");
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("Не задано описание работы");
        }
        if (normMinutes <= 0) {
            throw new IllegalArgumentException("Нормо-минут должно быть больше нуля: " + normMinutes);
        }
        description = description.strip();
    }
    @Override
    public Money cost() {
        long total = Math.multiplyExact(hourRate.kopecks(), (long) normMinutes);
        return new Money((total + 30) / 60);
    }
}
