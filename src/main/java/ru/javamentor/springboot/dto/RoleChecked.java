package ru.javamentor.springboot.dto;

public class RoleChecked {
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    private String name;
    private boolean checked;

    public RoleChecked() {
    }

    public RoleChecked(String name, boolean checked) {
        this.name = name;
        this.checked = checked;
    }
}
