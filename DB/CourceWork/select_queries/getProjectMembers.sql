deallocate all;
prepare getProjectMembers(int) as
  select U.user_id, U.name
  from Users as U natural join ProjectTeamMembers as TM
  where TM.project_id = $1;

-- Project Beko Inc.
execute getProjectMembers(1);
-- Project dbms hw
execute getProjectMembers(2);
-- Project hw
execute getProjectMembers(3);
-- Project bnd
execute getProjectMembers(4);

