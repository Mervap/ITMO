deallocate all;
prepare getUserNotifications(int) as
  select * from Notifications
  where notification_id in (select notification_id from UserNotificationSubscriptions
                            where user_id = $1);

-- Mervap
execute getUserNotifications(1);
-- Виктор Александрович
execute getUserNotifications(2);
-- Bilbo
execute getUserNotifications(3);
-- Mike Pervotin
execute getUserNotifications(4);
