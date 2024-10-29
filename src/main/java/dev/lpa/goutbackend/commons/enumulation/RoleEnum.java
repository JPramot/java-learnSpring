package dev.lpa.goutbackend.commons.enumulation;

public enum RoleEnum {
    CONSUMER(1),
    ADMIN(2),
    COMPANY(3);

    private final int id;

    RoleEnum(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }
}
