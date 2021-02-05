create or replace function calcMedian(in resolve_time_arr interval[]) returns interval
  language plpgsql
  security definer
as
$$
declare
  tasks_cnt int = -1;
begin
  tasks_cnt := (select count(*) from unnest(resolve_time_arr));
  if tasks_cnt = 0
  then
    return 0;
  else
    return (select resolve_time
            from unnest(resolve_time_arr) as resolve_time
            order by 1
            offset tasks_cnt / 2 limit 1);
  end if;
end
$$;

deallocate all;
prepare getMedianUserPersonalTaskResolveTime(int) as
  select *
  from calcMedian((select array_agg(resolve_time)
                   from (select T.process_state_assignment_date - T.creation_date as resolve_time
                         from Tasks as T natural join TaskProcessStates as TPS
                         where creator_id = $1
                           and TPS.name = 'Done'
                           and task_id not in (select task_id from ProjectTasks)) as ResolveTimes));

-- Mervap
execute getMedianUserPersonalTaskResolveTime(1);
-- Виктор Александрович
execute getMedianUserPersonalTaskResolveTime(2);
-- Bilbo
execute getMedianUserPersonalTaskResolveTime(3);
-- Mike Pervotin
execute getMedianUserPersonalTaskResolveTime(4);
