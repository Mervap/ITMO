deallocate all;
prepare getUsersWithProjectRole(int, int) as
  select U.user_id, U.name
  from Users as U natural join UserRoles as UR
  where UR.project_id = $1
    and UR.role_id = $2;

-- Beko CEO
execute getUsersWithProjectRole(1, 1);
-- Beko Software Developer
execute getUsersWithProjectRole(1, 2);
-- Hw owner
execute getUsersWithProjectRole(2, 1);
