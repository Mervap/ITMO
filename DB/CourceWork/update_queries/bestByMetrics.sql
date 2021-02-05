create or replace function getTop10ByMetrics() returns setof ProjectMetrics
  language plpgsql
as
$$
begin
  return query (select Q1.project_id, Q1.metric_id, Q1.value
                from (select row_number()
                             over (partition by PM.metric_id order by case when M.is_less_is_better then value else -value end) as topN,
                             Pm.*
                      from ProjectMetrics as PM natural join Metrics as M) Q1
                WHERE Q1.topN <= 10);
end;
$$;

select * from getTop10ByMetrics();

create or replace function getWorst10ByMetrics() returns setof ProjectMetrics
  language plpgsql
as
$$
begin
  return query (select Q1.project_id, Q1.metric_id, Q1.value
                from (select row_number()
                             over (partition by PM.metric_id order by case when M.is_less_is_better then -value else value end) as topN,
                             Pm.*
                      from ProjectMetrics as PM natural join Metrics as M) Q1
                WHERE Q1.topN <= 10);
end;
$$;

select * from getWorst10ByMetrics();