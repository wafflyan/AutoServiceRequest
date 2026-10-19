package cs.vsu.ru.gordienko.model;

import java.util.Set;

/**
 * Статус заявки. Каждый статус знает в какие статусы из него можно перейти
 */
public enum OrderStatus {
    ACCEPTED("Принята"),
    DIAGNOSTICS("Диагностика"),
    APPROVAL("Согласование"),
    REPAIR("Ремонт"),
    READY("Готова"),
    ISSUED("Выдана"),
    CANCELLED("Отменена");

    private final String title;

    OrderStatus(String title) {
        this.title = title;
    }

    /**
     *
     * @return статус
     */

    public String title() {
        return title;
    }

    /**
     * Статусы в которые можно перейти из текущего
     * @return множество допустимых следующих статусов
     */
    public Set<OrderStatus> allowedNext(){
        return switch(this) {
            case ACCEPTED -> Set.of(DIAGNOSTICS, CANCELLED);
            case DIAGNOSTICS -> Set.of(APPROVAL, CANCELLED);
            case APPROVAL -> Set.of(REPAIR, CANCELLED);
            case REPAIR -> Set.of(READY, CANCELLED);
            case READY -> Set.of(ISSUED);
            case ISSUED, CANCELLED -> Set.of();
        };
    }

    /**
     * Допустим ли переход в другой статус
     * @param target целевой статус
     * @return возвращает истинну, если переход разрешен
     */

    public boolean canTransitionTo(OrderStatus target) {
        return target != null && allowedNext().contains(target);
    }

    /**
     * Является ли этот статус конечным
     * @return возвращает истинну для выдана(ISSUED) и отменена(CANCELLED)
     */
    public boolean isFinal() {
        return allowedNext().isEmpty();
    }

    @Override
    public String toString() {
        return title;
    }

}
