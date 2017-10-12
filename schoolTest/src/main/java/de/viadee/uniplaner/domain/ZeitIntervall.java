package de.viadee.uniplaner.domain;

public enum ZeitIntervall {

    erste_stunde(1, "07:30-8:15"), zweite_stunde(2, "8:20-11:55"), dritte_stunde(3, "12:10-13:45"), vierte_stunde(
            4,
            "13:15-14:50"), SECHZEHN_UHR(5, "16:00-18:00"), ACHTZEHN_UHR(6, "18:00-20:00");

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