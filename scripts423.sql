SELECT s.name, s.age, f.name
FROM student s
JOIN faculty f
ON s.faculty_id = f.id;

SELECT s.name, s.age, f.name
FROM student s
JOIN faculty f
ON s.faculty_id = f.id
RIGHT JOIN avatar a
ON s.id = a.student_id;

SELECT s.name, s.age, f.name
FROM avatar a
LEFT JOIN student s
ON a.student_id = s.id
JOIN faculty f
ON s.faculty_id = f.id;