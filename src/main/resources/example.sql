-- AVERAGE GRADES FOR STUDENT FOR GIVEN SUBJECT
SELECT s.name, sub.name, ROUND(AVG(g.grade_value), 2)
FROM grade g
         INNER JOIN subject sub ON sub.id = g.subject_id
         INNER JOIN student s ON s.id = g.student_id
GROUP BY sub.name, s.name
ORDER BY s.name;

-- AGGREGATED GRADES FOR STUDENT GROUPED BY SUBJECTS
SELECT sub.name                              AS subject,
       string_agg(g.grade_value::text, ', ') AS grades,
       ROUND(AVG(g.grade_value), 2)          AS average
FROM grade g
         INNER JOIN student s ON g.student_id = s.id
         INNER JOIN subject sub ON g.subject_id = sub.id
WHERE s.id = 1
GROUP BY sub.name;