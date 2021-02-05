deallocate all;
prepare getProjectRoles(int) as
  select * from Roles
  where project_id = $1;

-- Project Beko Inc.
execute getProjectRoles(1);
-- Project hw
execute getProjectRoles(2);
execute getProjectRoles(3);
execute getProjectRoles(4);