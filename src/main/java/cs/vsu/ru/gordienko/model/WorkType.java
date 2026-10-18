package cs.vsu.ru.gordienko.model;

/** Вид работ. По ним считается средний срок ремонта */
public enum WorkType {
    MAINTENANCE("Техобслуживание"),
    ENGINE("Двигатель"),
    TRANSMISSION("Трансмиссия"),
    BRAKES("Тормоза"),
    SUSPENSION("Ходовая"),
    ELECTRICS("Электрика"),
    BODY("Кузов");

    private final String title;

    WorkType(String title) {
        this.title = title;
    }

    /**
     * Название для вывода
     * @return название вида работ
     */
    public String title() {
        return title;
    }

    @Override
    public String toString() {
        return title;
    }
}
