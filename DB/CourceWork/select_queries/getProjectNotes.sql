deallocate all;
prepare getProjectNotes(int) as
  select * from Notes
  where note_id in (select note_id from ProjectNotes where project_id = $1);

-- Project Beko Inc.
execute getProjectNotes(1);
-- Project hw
execute getProjectNotes(2);
execute getProjectNotes(3);
execute getProjectNotes(4);
