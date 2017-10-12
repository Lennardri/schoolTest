package de.lennard.school.solver;

import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.lang.builder.CompareToBuilder;
import org.h2.tools.RunScript;
import org.junit.BeforeClass;
import org.junit.Test;
import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.lennard.school.domain.Klasse;
import de.lennard.school.domain.Raum;
import de.lennard.school.domain.Termin;
import de.lennard.school.domain.Unterricht;
import de.lennard.school.domain.Unterrichtsplan;
import de.lennard.school.domain.Wochentag;
import de.lennard.school.domain.ZeitIntervall;

public class SolverTest {

    Logger logger = LoggerFactory.getLogger(getClass());

    @BeforeClass
    public static void setupDatabase() throws Exception {
        try (Connection conn = getH2Connection(); Statement statement = conn.createStatement()) {
            statement.execute("drop all objects");
            RunScript.execute(conn, new FileReader("./src/test/resources/db-setup.sql"));
        }
    }

    @Test
    public void testSolver() throws Exception {

        try (Connection conn = getH2Connection()) {

            QueryRunner queryRunner = new QueryRunner();

            // Laden der Ausgangsdaten

            List<Klasse> klasseListe = queryRunner.query(conn, "SELECT * FROM klasse",
                    new BeanListHandler<>(Klasse.class));

            List<Unterricht> unterrichtListe = new ArrayList<>();
            for (Klasse klasse : klasseListe) {
                List<Unterricht> unterrichtenProStudiengang = queryRunner.query(conn,
                        "SELECT * FROM unterricht WHERE klasse_id = ?", new BeanListHandler<>(Unterricht.class),
                        klasse.getId());
                for (Unterricht unterricht : unterrichtenProStudiengang) {
                    unterricht.setKlasse(klasse);
                    unterrichtListe.add(unterricht);
                }
            }

            List<Raum> raumListe = queryRunner.query(conn, "SELECT * FROM raum", new BeanListHandler<>(Raum.class));

            List<Termin> terminListe = new ArrayList<>();
            for (Wochentag tag : Wochentag.values()) {
                for (ZeitIntervall intervall : ZeitIntervall.values()) {
                    terminListe.add(new Termin(tag, intervall));
                }
            }

            List<Wochentag> tagListe = Arrays.asList(Wochentag.values());

            // 1) Objekt der PlanningSolution-Klasse Unterrichtsplan erzeugen
            Unterrichtsplan planningProblem = new Unterrichtsplan();
            planningProblem.setKlasseListe(klasseListe);
            planningProblem.setUnterrichtListe(unterrichtListe);
            planningProblem.setRaumListe(raumListe);
            planningProblem.setTerminListe(terminListe);
            planningProblem.setTagListe(tagListe);

            // 2) Solver-Objekt erzeugen
            SolverFactory solverFactory = SolverFactory
                    .createFromXmlResource("de/lennard/school/solver/solverConfig.xml");
            Solver solver = solverFactory.buildSolver();

            // 3) Optimierung starten
            solver.solve(planningProblem);

            // 4) Beste gefundene Lösung abrufen
            Unterrichtsplan solution = (Unterrichtsplan) solver.getBestSolution();

            // Verarbeitung der Optimierungsergebnisse; in diesem einfachen Testbeispiel wird die Liste der Vorlesungen
            // nach Raum, Tag und Termin sortiert und ausgegeben

            List<Unterricht> solutionUnterrichtListe = solution.getUnterrichtListe();
            Collections.sort(solutionUnterrichtListe, new Comparator<Unterricht>() {

                @Override
                public int compare(Unterricht v1, Unterricht v2) {
                    CompareToBuilder builder = new CompareToBuilder();
                    builder.append(v1.getRaum().getId(), v2.getRaum().getId())
                            .append(v1.getTag().getId(), v2.getTag().getId())
                            .append(v1.getTermin().getIntervall().getId(), v2.getTermin().getIntervall().getId());
                    return builder.toComparison();
                }
            });

            StringBuffer sb = new StringBuffer();
            sb.append("Optimierungsergebnis:\n");
            for (Unterricht v : solutionUnterrichtListe) {
                sb.append(v.getRaum() + ", " + v.getTag() + ", " + v.getTermin().getIntervall() + " -> " + v + "\n");
            }
            logger.info(sb.toString());
        }
    }

    private static Connection getH2Connection() throws SQLException {
        return DriverManager.getConnection("jdbc:h2:~/uniplaner");
    }
}