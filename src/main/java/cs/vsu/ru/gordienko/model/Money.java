package cs.vsu.ru.gordienko.model;

/**
 * Денежная сумма в копейках. Неизменяемое значение.
 * @param kopecks количество копеек, не отрицательное
 */

public record Money(long kopecks) {

    /** Нулевая сумма */
    public static final Money ZERO = new Money(0);

    /**
     * Проверяем что сумма неотрицательная
     *
     * @throws IllegalArgumentException если kopecks < 0
     */
    public Money {
        if (kopecks < 0) {
            throw new IllegalArgumentException("Сумма не может быть меньше нуля: " + kopecks);
        }
    }

    /**
     * Создает сумму из целых рублей
     * @param rubles рубли
     * @return сумма
     */
    public static Money rubles(long rubles) {
        return new Money(Math.multiplyExact(rubles, 100L));
    }

    /**
     * Складывает две суммы
     *
     * @param other слагаемое
     * @return новая сумма
     */
    public Money rubles(Money other) {
        return new Money(Math.addExact(kopecks, other.kopecks));
    }

    @Override
    public String toString() {
        return "%d,%02d руб".formatted(kopecks / 100, kopecks % 100);
    }
}
