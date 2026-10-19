package cs.vsu.ru.gordienko.model;

import java.util.Locale;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Автомобиль. Идентификатором является VIN
 */
public final class Car {
    private final Pattern VIN_PATTERN = Pattern.compile("[A-HJ-NPR-Z0-9]{17}");

    private final String vin;
    private final String model;
    private final Client owner;

    /**
     * Создает автомобиль
     * @param vin VIN: 17 символов, латинские буквы (все кроме I, O, Q) и цифры, регистр неважен
     * @param model модель авто
     * @param owner владелец авто
     * @throws IllegalArgumentException если VIN некорректный илии модель пуста
     */
    public Car(String vin, String model, Client owner) {
        String normalized = normalizeVin(Objects.requireNonNull(vin, "vin"));
        if (!VIN_PATTERN.matcher(normalized).matches()) {
            throw new IllegalArgumentException("Некорректный VIN:" + vin);
        }
        if (model == null || model.isBlank()) {
            throw new IllegalArgumentException("Не задана модель авто");
        }
        this.vin = normalized;
        this.model = model.strip();
        this.owner = Objects.requireNonNull(owner, "owner");
    }

    /**
     *
     * @param raw введёный VIN
     * @return нормализованный VIN или {@code null} если {@code raw == null}
     */
    public static String normalizeVin(String raw) {
        return raw == null ? null : raw.strip().toUpperCase(Locale.ROOT);
    }

    /**
     * @return нормализованный VIN
     */
    public String vin() {
        return vin;
    }

    /**
     * @return модель автомобиля
     */
    public String model() {
        return model;
    }

    /**
     *
     * @return владельца автомобиля
     */
    public Client owner() {
        return owner;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        return o instanceof Car other && vin.equals(other.vin);
    }

    @Override
    public int hashCode() {
        return vin.hashCode();
    }

    @Override
    public String toString() {
        return model + " [" + vin + "], владелец: " + owner;
    }
}
