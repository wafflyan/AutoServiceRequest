package cs.vsu.ru.gordienko.model;

/**
 * Работа в заявке. Стоимость считается по разному(почасовая и фиксированная
 */
public sealed interface Job permits LaborJob, FixedPricedJob {

    WorkType type();

    String description();

    int normMinutes();

    Money cost();

    default double normHours() {
        return normMinutes() / 60.0;
    }
}
