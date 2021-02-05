deallocate all;
prepare getUserNotificationsWithSeverity(int, int) as
  select * from Notifications
  where notification_id in (select notification_id from UserNotificationSubscriptions
                            where user_id = $1
                              and severity_id = $2);

-- Mervap major
execute getUserNotificationsWithSeverity(1, 3);
-- Mervap critical
execute getUserNotificationsWithSeverity(1, 4);
