package cs.vsu.ru.gordienko.model;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Заявка на ремонт. Идентификатор заявки - номер. Заявки могут быть равны только если равны их номера
 * Статс меняется только через ChangeStatus, каждый переход записывается в журнал
 */
public final class RepairOrder {
    private final int id;
    private final Car car;
    private final LocalDate promisedDate;
    private final List<Job> jobs = new ArrayList<>();
    private final List<StatusChange> journal = new ArrayList<>();
    private OrderStatus status = OrderStatus.ACCEPTED;

    /**
     *
     * @param id номер заявки
     * @param car автомобиль
     * @param promisedDate обещанная дата выдачи
     * @param acceptedAt дата и время приема
     * @throws IllegalArgumentException если номер неверный, то есть <= 0 или обещанная дата раньше даты приёма
     */

    public RepairOrder(int id, Car car, LocalDate promisedDate, LocalDateTime acceptedAt) {
        if (id <= 0) {
            throw new IllegalArgumentException("Номер заявки должен быть положительным");
        }
        this.id = id;
        this.car = Objects.requireNonNull(car, "car");
        this.promisedDate = Objects.requireNonNull(promisedDate, "promisedDate");
        Objects.requireNonNull(acceptedAt, "acceptedAt");
        if (promisedDate.isBefore(acceptedAt.toLocalDate())) {
            throw new IllegalArgumentException("Обещанная дата раньше даты приёма");
        }
        journal.add(new StatusChange(null, OrderStatus.ACCEPTED, acceptedAt));
    }

    /**
     *
     * @return номер заявки
     */
    public int id() {
        return id;
    }

    /**
     * Автомобиль из заявки
     * @return автомобиль
     */
    public Car car() {
        return car;
    }

    /**
     * Обещанная дата выдачи
     * @return дата
     */
    public LocalDate promisedDate() {
        return promisedDate;
    }

    /**
     * Текущий статус
     * @return статус
     */
    public OrderStatus status() {
        return status;
    }
    /**
     * Работы по заявки
     * @return список работ
     */
    public List<Job> jobs() {
        return List.copyOf(jobs);
    }

    /**
     * Журнал переходов
     * @return журнал в хронологическом порядке
     */
    public List<StatusChange> journal() {
        return List.copyOf(journal);
    }

    /**
     * Дата и время приёма заявки
     * @return дата и время приёма
     */
    public LocalDateTime acceptedAt() {
        return journal.get(0).at();
    }

    /**
     * Стоимость заявки, то есть стоимость всех работ
     * @return стоимость( в копейках)
     */
    public Money totalCost() {
        Money sum = Money.ZERO;
        for (Job job : jobs) {
            sum = sum.plus(job.cost());
        }
        return sum;
    }
    /**
     * Виды работ, встречающиеся в заявке.
     * @return новое множество видов работ
     */
    public Set<WorkType>  workTypes() {
        Set<WorkType> types = EnumSet.noneOf(WorkType.class);
        for (Job job : jobs) {
            types.add(job.type());
        }
        return types;
    }
    /**
     * Добавляет работу. Нельзя добавлять в готовую и закрытые заявки.
     *
     * @param job работа
     * @throws IllegalStateException если статус - готова, выдана, отменена
     */
    public void addJob(Job job) {
        Objects.requireNonNull(job, "job");
        if (status == OrderStatus.READY || status.isFinal()) {
            throw new IllegalStateException("В заявку со статусом " + status + "работы добавлять нельзя");
        }
        jobs.add(job);
    }
    /**
     * Суммарная трудоёмкость.
     *
     * @return нормо-минуты всех работ
     */
    public int totalNormMinutes() {
        int sum = 0;
        for (Job job: jobs) {
            sum += job.normMinutes();
        }
        return sum;
    }
    /**
     * Переводит заявку в новый статус и записывает переход в журнал.
     * При любой ошибке состояние заявки не меняется.
     *
     * @param target новый статус
     * @param at время перехода, которое не может быть раньше предыдщей записи журнала
     * @throws InvalidTransitionException если переход не разрешён автоматом статусов
     * @throws IllegalArgumentException если время раньше последней записи журнала
     * @throws IllegalStateException если заявка без работ отправляется на согласование
     */
    public void changeStatus(OrderStatus target, LocalDateTime at) throws InvalidTransitionException {
        Objects.requireNonNull(target, "target");
        Objects.requireNonNull(at, "at");
        if (!status.canTransitionTo(target)) {
            throw new InvalidTransitionException(status, target);
        }
        if (at.isBefore(lastChange().at())) {
            throw new IllegalArgumentException("Время перехода раньше предыдущей записи журнала");
        }
        if (target == OrderStatus.APPROVAL && jobs.isEmpty()) {
            throw new IllegalStateException("Нельзя отправить на согласование заявку без работ");
        }
        journal.add(new StatusChange(status, target, at));
        status = target;
    }

    private StatusChange lastChange() {
        return journal.get(journal.size() - 1);
    }
    /**
     * Момент закрытия заявки, то есть либо выдача, либо отмена
     *
     * @return время закрытия или пусто, если заявка ещё открыта
     */
    public Optional<LocalDateTime> closedAt() {
        return status.isFinal() ? Optional.of(lastChange().at()) : Optional.empty();
    }
    /**
     * Фактический срок ремонта, от приёма до выдачи.
     *
     * @return срок или пусто, если заявка не выдана
     */
    public Optional<Duration> actualDuration() {
        if (status != OrderStatus.ISSUED) {
            return Optional.empty();
        }
        return Optional.of(Duration.between(acceptedAt(), lastChange().at()));
    }
    /**
     * Находилась ли заявка в работе в какой-то момент указанной даты:
     * принята не позже этой даты и не закрыта до её начала.
     *
     * @param date дата
     * @return истинно, если заявка в этот день была в работе
     */
    public boolean isInProgressOn(LocalDate date) {
        if (acceptedAt().toLocalDate().isAfter(date)) {
            return false;
        }
        return closedAt().map(c -> !c.toLocalDate().isBefore(date)).orElse(true);
    }
    /**
     * Просрочена ли заявка относительно обещанной даты выдачи
     * Выданная заявка просрочена, если выдана позже; открытая — если на момент now срок уже прошёл;
     * отменённая не бывает просроченной.
     *
     * @param now текущий момент
     * @return истинно, если срок нарушен
     */

    public boolean isOverdue(LocalDateTime now) {
        Objects.requireNonNull(now, "now");
        if (status == OrderStatus.CANCELLED) {
            return false;
        }
        LocalDateTime deadline = promisedDate.plusDays(1).atStartOfDay();
        LocalDateTime end = status == OrderStatus.ISSUED ? lastChange().at() : now;
        return end.isAfter(deadline);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        return o instanceof RepairOrder other && id == other.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }

    @Override
    public String toString() {
        return "Заявка №%d | %s | %s | срок до %s | %s | %s".formatted(
                id, car.model(), car.vin(), promisedDate, status, totalCost());
    }

}
