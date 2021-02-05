deallocate all;
prepare getPersonalNotes(int) as
  select * from Notes
  where creator_id = $1
    and note_id not in (select note_id from ProjectNotes);

-- User Mervap
execute getPersonalNotes(1);
-- User Виктор Александрович
execute getPersonalNotes(2);
-- User Bilbo
execute getPersonalNotes(3);
-- User Mike Pervotin
execute getPersonalNotes(4);
execute getPersonalNotes(5);
execute getPersonalNotes(6);
execute getPersonalNotes(7);
