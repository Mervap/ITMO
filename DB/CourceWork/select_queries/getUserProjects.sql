deallocate all;
prepare getUserProjects(int) as
  select project_id, name from Projects
  where project_id in (select project_id from ProjectTeamMembers where user_id = $1);

-- User Mervap
execute getUserProjects(1);
-- User Виктор Александрович
execute getUserProjects(2);
-- User Bilbo
execute getUserProjects(3);
-- User Mike Pervotin
execute getUserProjects(4);
