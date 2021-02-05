deallocate all;
prepare getUserProjectRoles(int, int) as
  select R.project_id, R.role_id, R.name, R.description, UR.grant_date
  from Roles as R natural join UserRoles as UR
  where UR.user_id = $1
    and R.project_id = $2;

-- User Mervap
execute getUserProjectRoles(1, 1);
-- User Mervap hw
execute getUserProjectRoles(1, 2);
-- User Виктор Александрович
execute getUserProjectRoles(2, 1);
-- User Bilbo
execute getUserProjectRoles(3, 1);
-- User Mike Pervotin
execute getUserProjectRoles(4, 1);
