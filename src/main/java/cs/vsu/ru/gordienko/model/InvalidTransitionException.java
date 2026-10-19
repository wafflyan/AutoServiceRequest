package cs.vsu.ru.gordienko.model;

import java.io.Serial;

/**
 * Исключение при недопустимом переходе от одного статуса к другому
 */
public class InvalidTransitionException extends Exception {
    @Serial
    private static final long serialVersionUID = 1L;
    private final OrderStatus from;
    private final OrderStatus to;

    public InvalidTransitionException(OrderStatus from, OrderStatus to) {
        super("Переход «" + from + "» -> «" + to + "» недопустим"
                + (from.isFinal() ? ": статус конечный" : ", разрешено: " + from.allowedNext()));
        this.from = from;
        this.to = to;
    }
    public OrderStatus getFrom() {
        return from;
    }
    public OrderStatus getTo() {
        return to;
    }
}
