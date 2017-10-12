package de.lennard.school.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.optaplanner.core.api.domain.solution.PlanningEntityCollectionProperty;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.domain.solution.Solution;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;

@PlanningSolution
public class Unterrichtsplan implements Solution<HardSoftScore> {

    private List<Unterricht> unterrichtListe;
    private List<Klasse> klasseListe;
    private List<Raum> raumListe;
    private List<Termin> terminListe;
    private List<Wochentag> tagListe;

    private HardSoftScore score;

    public Collection<? extends Object> getProblemFacts() {
        List<Object> facts = new ArrayList<>();
        facts.addAll(klasseListe);
        facts.addAll(raumListe);
        facts.addAll(terminListe);
        facts.addAll(tagListe);
        return facts;
    }

    @Override
    public boolean equals(Object obj) {

        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != getClass()) {
            return false;
        }

        List<Unterricht> otherUnterrichtListe = ((Unterrichtsplan) obj).getUnterrichtListe();

        if (otherUnterrichtListe.size() != unterrichtListe.size()) {
            return false;
        }

        for (int i = 0; i < unterrichtListe.size(); i++) {
            if (!unterrichtListe.get(i).equals(otherUnterrichtListe.get(i))) {
                return false;
            }
        }

        return true;
    }

    @Override
    public int hashCode() {
        HashCodeBuilder builder = new HashCodeBuilder();
        for (Unterricht unterricht : unterrichtListe) {
            builder.append(unterricht);
        }
        return builder.toHashCode();
    }

    @ValueRangeProvider(id = "raumListe")
    public List<Raum> getRaumListe() {
        return raumListe;
    }

    @ValueRangeProvider(id = "terminListe")
    public List<Termin> getTerminListe() {
        return terminListe;
    }

    @PlanningEntityCollectionProperty
    public List<Unterricht> getUnterrichtListe() {
        return unterrichtListe;
    }

    public HardSoftScore getScore() {
        return score;
    }

    public void setScore(HardSoftScore score) {
        this.score = score;
    }

    public List<Klasse> getKlasseListe() {
        return klasseListe;
    }

    public void setKlasseListe(List<Klasse> klasseListe) {
        this.klasseListe = klasseListe;
    }

    public void setRaumListe(List<Raum> raumListe) {
        this.raumListe = raumListe;
    }

    public void setTerminListe(List<Termin> terminListe) {
        this.terminListe = terminListe;
    }

    public void setUnterrichtListe(List<Unterricht> unterrichtListe) {
        this.unterrichtListe = unterrichtListe;
    }

    public List<Wochentag> getTagListe() {
        return tagListe;
    }

    public void setTagListe(List<Wochentag> tagListe) {
        this.tagListe = tagListe;
    }
}