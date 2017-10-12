package de.viadee.uniplaner.domain;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.variable.PlanningVariable;

@PlanningEntity
public class Unterricht {

    private long id;
    private String name;
    private Klasse klasse;
    private Termin termin;
    private Raum raum;

    public Unterricht() {
    }

    public Unterricht(long id, String name, Klasse klasse) {
        this.id = id;
        this.name = name;
        this.klasse = klasse;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @PlanningVariable(valueRangeProviderRefs = { "raumListe" })
    public Raum getRaum() {
        return raum;
    }

    @PlanningVariable(valueRangeProviderRefs = { "terminListe" })
    public Termin getTermin() {
        return termin;
    }

    public void setRaum(Raum raum) {
        this.raum = raum;
    }

    public void setTermin(Termin termin) {
        this.termin = termin;
    }

    public Klasse getKlasse() {
        return klasse;
    }

    public void setKlasse(Klasse klasse) {
        this.klasse = klasse;
    }

    public Wochentag getTag() {
        return termin != null ? termin.getTag() : null;
    }

    public int getIntervallIndex() {
        return termin != null ? termin.getIntervall().getId() : Integer.MIN_VALUE;
    }

    @Override
    public String toString() {
        return klasse.getName() + " / " + name;
    }

    @Override
    public boolean equals(Object obj) {

        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != getClass()) {
            return false;
        }

        Unterricht other = (Unterricht) obj;
        return new EqualsBuilder().append(id, other.id).append(name, other.name).append(klasse, other.klasse)
                .append(termin, other.termin).append(raum, other.raum).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(id).append(name).append(klasse).append(termin).append(raum)
                .toHashCode();
    }
}