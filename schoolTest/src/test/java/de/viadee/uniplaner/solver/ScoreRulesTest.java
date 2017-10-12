package de.viadee.uniplaner.solver;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScoreHolder;

import de.viadee.uniplaner.domain.Klasse;
import de.viadee.uniplaner.domain.Raum;
import de.viadee.uniplaner.domain.Termin;
import de.viadee.uniplaner.domain.Unterricht;
import de.viadee.uniplaner.domain.Wochentag;
import de.viadee.uniplaner.domain.ZeitIntervall;

public class ScoreRulesTest {

    private static KieContainer kContainer;
    private KieSession session;
    private HardSoftScoreHolder scoreHolder;

    @BeforeClass
    public static void createContainer() {
        KieServices kieServices = KieServices.Factory.get();
        kContainer = kieServices.getKieClasspathContainer();
    }

    @Before
    public void createSession() {
        session = kContainer.newKieSession();
        scoreHolder = new HardSoftScoreHolder(true);
        session.setGlobal("scoreHolder", scoreHolder);
        for (Wochentag tag : Wochentag.values()) {
            session.insert(tag);
        }
    }

    @After
    public void disposeSession() {
        session.dispose();
    }

    @Test
    public void testRuleRaumKonflikt() {

        Termin montag8Uhr = new Termin(Wochentag.MONTAG, ZeitIntervall.erste_stunde);
        Raum raum1 = new Raum(1, "Raum 1");

        Klasse wi = new Klasse(1, "WI");
        Klasse bwl = new Klasse(2, "BWL");

        Unterricht einfuehrungWI = new Unterricht(1, "Einführung WI", wi);
        einfuehrungWI.setTermin(montag8Uhr);
        einfuehrungWI.setRaum(raum1);

        Unterricht bwlI = new Unterricht(2, "BWL I", bwl);
        bwlI.setTermin(montag8Uhr);
        bwlI.setRaum(raum1);

        session.insert(einfuehrungWI);
        session.insert(bwlI);

        session.fireAllRules();

        // Zwei Vorlesungen (aus unterschiedlichen Studiengängen) zum gleichen Termin im gleichen Raum
        assertEquals(-1, scoreHolder.getHardScore());
        assertEquals(0, scoreHolder.getSoftScore());
    }

    @Test
    public void testRuleTerminKonflikt() {

        Termin montag8Uhr = new Termin(Wochentag.MONTAG, ZeitIntervall.erste_stunde);
        Klasse wi = new Klasse(1, "WI");

        Unterricht einfuehrungWI = new Unterricht(1, "Einführung WI", wi);
        einfuehrungWI.setTermin(montag8Uhr);

        Unterricht anwendungssysteme = new Unterricht(2, "Anwendungssysteme", wi);
        anwendungssysteme.setTermin(montag8Uhr);

        session.insert(einfuehrungWI);
        session.insert(anwendungssysteme);

        session.fireAllRules();

        // Zwei Vorlesungen des gleichen Studiengangs am gleichen Termin
        assertEquals(-1, scoreHolder.getHardScore());
        assertEquals(0, scoreHolder.getSoftScore());
    }

    @Test
    public void testRuleRaumKontinuitaet() {

        Klasse wi = new Klasse(1, "WI");

        Raum raum1 = new Raum(1, "Raum 1");
        Raum raum2 = new Raum(2, "Raum 2");

        Unterricht einfuehrungWI = new Unterricht(1, "Einführung WI", wi);
        einfuehrungWI.setRaum(raum1);

        Unterricht anwendungssysteme = new Unterricht(2, "Anwendungssysteme", wi);
        anwendungssysteme.setRaum(raum2);

        session.insert(wi);
        session.insert(raum1);
        session.insert(raum2);
        session.insert(einfuehrungWI);
        session.insert(anwendungssysteme);

        session.fireAllRules();

        // Zwei Vorlesungen des gleichen Studiengangs in unterschiedlichen Räumen
        assertEquals(0, scoreHolder.getHardScore());
        assertEquals(-1, scoreHolder.getSoftScore());
    }

    @Test
    public void testRuleWochenKompaktheit() {

        Klasse wi = new Klasse(1, "WI");

        Termin montag8Uhr = new Termin(Wochentag.MONTAG, ZeitIntervall.erste_stunde);
        Termin dienstag8Uhr = new Termin(Wochentag.DIENSTAG, ZeitIntervall.erste_stunde);

        Unterricht einfuehrungWI = new Unterricht(1, "Einführung WI", wi);
        einfuehrungWI.setTermin(montag8Uhr);

        Unterricht anwendungssysteme = new Unterricht(2, "Anwendungssysteme", wi);
        anwendungssysteme.setTermin(dienstag8Uhr);

        session.insert(wi);
        session.insert(einfuehrungWI);
        session.insert(anwendungssysteme);

        session.fireAllRules();

        // Zwei Vorlesungen des gleichen Studiengangs an unterschiedlichen Tagen
        assertEquals(0, scoreHolder.getHardScore());
        assertEquals(-1, scoreHolder.getSoftScore());
    }

    @Test
    public void testRuleTagKompaktheit() {

        Klasse wi = new Klasse(1, "WI");

        Termin montag8Uhr = new Termin(Wochentag.MONTAG, ZeitIntervall.erste_stunde);
        Termin montag12Uhr = new Termin(Wochentag.MONTAG, ZeitIntervall.dritte_stunde);

        Unterricht einfuehrungWI = new Unterricht(1, "Einführung WI", wi);
        einfuehrungWI.setTermin(montag8Uhr);

        Unterricht anwendungssysteme = new Unterricht(2, "Anwendungssysteme", wi);
        anwendungssysteme.setTermin(montag12Uhr);

        session.insert(einfuehrungWI);
        session.insert(anwendungssysteme);

        session.fireAllRules();

        // Zwei Vorlesungen des gleichen Studiengangs am gleichen Tag, die nicht aufeinander folgen
        assertEquals(0, scoreHolder.getHardScore());
        assertEquals(-2, scoreHolder.getSoftScore());
    }
}