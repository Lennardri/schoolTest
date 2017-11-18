package de.lennard.school.domain;

public enum ZeitIntervall {

    erste_stunde(1, "07:30-08:15"), zweite_stunde(2, "08:20-09:05"), dritte_stunde(3, "09:20-10:05"), vierte_stunde(
            4,
            "10:10-10:55"), fünfte_stunde(5, "11:10-11:45"), sechste_stunde(6, "12:00_12:45");

    private int id;
    private String name;

    private ZeitIntervall(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return name;
    }
}