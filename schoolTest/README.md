### optaPlannerExample
-----
Beispielprojekt für JBoss OptaPlanner: Raum- und Terminplanung an einer Schule.

-----


-----

**BEISPIELSZENARIO**

Das Projekt implementiert beispielhaft die (stark vereinfachte) Raum- und Terminplanung an einer Schule
auf Basis von JBoss OptaPlanner und JBoss Drools Expert. Ziel ist dabei, dass jeder Vorlesung ein Termin
(Wochentag und Zeitintervall) und ein Raum zugeordnet wird.

Folgende Bedingungen müssen bei der Planung erfüllt werden (Hard Constraints):
- Zwei Vorlesungen dürfen nicht zeitgleich im gleichen Raum stattfinden.
- Die Vorlesungen eines Studiengangs dürfen sich zeitlich nicht überschneiden.

Folgende Bedingungen sollten erfüllt werden (Soft Constraints):
- Die Vorlesungen eines Studiengangs sollten im gleichen Raum stattfinden.
- Die Vorlesungen eines Studiengangs sollten an möglichst wenigen Wochentagen stattfinden.
- Die Vorlesungen eines Studiengangs sollten pro Tag kompakt sein, sodass es möglichst keine Freistunden gibt.

Diese Bedingungen werden durch Drools-Regeln formuliert.

-----


**TESTDATEN**

Zum Testen der Optimierung kann die Klasse `SolverTest` verwendet werden. Diese lädt einige Testdaten für Räume, Studiengänge
und Vorlesungen aus drei csv-Dateien (siehe `src/test/resources`) in eine H2-Datenbank und führt die Optimierung mit
dem OptaPlanner-Solver durch.


