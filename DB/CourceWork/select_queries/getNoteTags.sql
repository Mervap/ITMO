deallocate all;
prepare getNoteTags(bigint) as
  select * from Tags
  where tag_id in (select tag_id from NoteTags where note_id = $1);

-- Work
execute getNoteTags(3);
-- data.sql
execute getNoteTags(4);
-- BND
execute getNoteTags(5);
-- FP
execute getNoteTags(6);
