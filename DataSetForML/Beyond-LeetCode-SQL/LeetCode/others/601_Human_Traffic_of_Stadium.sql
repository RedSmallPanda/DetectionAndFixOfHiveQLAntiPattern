-- be careful with duplicate
SELECT DISTINCT 
  s1.* 
FROM
  stadium AS s1
  ,stadium AS s2
  ,stadium AS s3
WHERE s1.people >= 100 
  AND s2.people >= 100 
  AND s3.people >= 100 
  AND ((s1.id = s2.id - 1 AND s1.id = s3.id - 2) -- start of window
    OR (s1.id = s2.id + 1 AND s1.id = s3.id - 1) -- middle of window
    OR (s1.id = s2.id + 2 AND s1.id = s3.id + 1)) -- end of window
ORDER BY s1.id; 

-- MySQL: pre-filtering
SELECT DISTINCT 
  s1.* 
FROM
  (SELECT * FROM stadium WHERE people >= 100) AS s1
  ,(SELECT * FROM stadium WHERE people >= 100) AS s2
  ,(SELECT * FROM stadium WHERE people >= 100) AS s3
WHERE (s1.id = s2.id - 1 AND s1.id = s3.id - 2) 
  OR (s1.id = s2.id + 1 AND s1.id = s3.id - 1) 
  OR (s1.id = s2.id + 2 AND s1.id = s3.id + 1)
ORDER BY s1.id;

-- MS SQL: cleaner code
WITH good_day AS (
  SELECT * FROM stadium WHERE people >= 100
)
SELECT DISTINCT s1.* FROM
good_day AS s1,
good_day AS s2,
good_day AS s3
WHERE (s1.id = s2.id - 1 AND s1.id = s3.id - 2) 
  OR (s1.id = s2.id + 1 AND s1.id = s3.id - 1) 
  OR (s1.id = s2.id + 2 AND s1.id = s3.id + 1)
ORDER BY s1.id;

