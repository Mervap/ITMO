deallocate all;
prepare getTaskTags(bigint) as
  select * from Tags
  where tag_id in (select tag_id from TaskTags where task_id = $1);

-- Йога 1
execute getTaskTags(4);
-- Йога 2
execute getTaskTags(5);
execute getTaskTags(6);
-- Course work
execute getTaskTags(7);
-- UI
execute getTaskTags(8);
-- BE
execute getTaskTags(9);
execute getTaskTags(10);
execute getTaskTags(23);
