CREATE DEFINER=`davevr`@`%` PROCEDURE `GetUserMatchingDates`(IN targetUserId INT)
BEGIN
SELECT  A.id, proposerid, title, description, starttime, paymentstyle, selfie , B.id IS NOT NULL AS pinned
FROM LettuceMaster.dates AS A 
LEFT OUTER JOIN LettuceMaster.pinneddates AS B
ON B.dateid = A.id AND B.userid = targetUserId
WHERE proposerid != 1 AND active = 1 AND A.proposerid != targetUserId;
END