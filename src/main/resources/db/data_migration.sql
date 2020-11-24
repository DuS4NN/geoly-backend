INSERT INTO `CATEGORY` (`id`, `image_url`, `name`) VALUES
  (1, 'assets/images/categoryImages/history.svg', 'HISTORY'),
  (2, 'assets/images/categoryImages/art.svg', 'ART'),
  (3, 'assets/images/categoryImages/view.svg', 'VIEW'),
  (4, 'assets/images/categoryImages/architecture.svg', 'ARCHITECTURE'),
  (5, 'assets/images/categoryImages/nature.svg', 'NATURE');

INSERT INTO `BADGE` (`id`, `name`, `image_url`) VALUES
  (1, 'FIRST_IN_SEASON','assets/images/badgeImages/season_1.svg'),
  (2, 'SECOND_IN_SEASON','assets/images/badgeImages/season_2.svg'),
  (3, 'THIRD_IN_SEASON','assets/images/badgeImages/season_3.svg'),
  (4, 'TOP_10_IN_SEASON','assets/images/badgeImages/season_10.svg'),
  (5, 'TOP_50_IN_SEASON','assets/images/badgeImages/season_50.svg'),
  (6, 'QUESTION_200','assets/images/badgeImages/question_200.svg'),
  (7, 'QUESTION_100','assets/images/badgeImages/question_100.svg'),
  (8, 'QUESTION_50','assets/images/badgeImages/question_50.svg'),
  (9, 'QUESTION_10','assets/images/badgeImages/question_10.svg'),
  (10, 'QRCODE_100','assets/images/badgeImages/qrcode_100.svg'),
  (11, 'QRCODE_50','assets/images/badgeImages/qrcode_50.svg'),
  (12, 'QRCODE_10','assets/images/badgeImages/qrcode_10.svg'),
  (13, 'QRCODE_5','assets/images/badgeImages/qrcode_5.svg'),
  (14, 'PLACE_300','assets/images/badgeImages/place_300.svg'),
  (15, 'PLACE_150','assets/images/badgeImages/place_150.svg'),
  (16, 'PLACE_50','assets/images/badgeImages/place_50.svg'),
  (17, 'PLACE_20','assets/images/badgeImages/place_20.svg'),
  (18, 'FINISH_500','assets/images/badgeImages/finish_500.svg'),
  (19, 'FINISH_250','assets/images/badgeImages/finish_250.svg'),
  (20, 'FINISH_100','assets/images/badgeImages/finish_100.svg'),
  (21, 'FINISH_50','assets/images/badgeImages/finish_50.svg');

INSERT INTO `LANGUAGE` (`id`, `image_url`, `name`) VALUES
  (1, '/static/image/language/slovak.png', 'Slovak'),
  (2, '/static/image/language/english.png', 'English');

INSERT INTO `ROLE` (`id`, `name`) VALUES
  (1, 'USER'),
  (2, 'MOD'),
  (3, 'ADMIN');

INSERT INTO `USER` (`id`, `about`, `active`, `address`, `created_at`, `email`, `nick_name`, `password`, `profile_image_url`, `verified`) VALUES
(1, '', 1, NULL, '2020-11-13 10:41:56', 'info@geoly.com', 'Geoly', '$argon2id$v=19$m=4096,t=3,p=1$aMe4tkdEcHSWMMhAqzoq/A$/3+cnAVHW6FTkd8L8QaxHqBhTksETd34ERHaGz+N3wU', 'static/images/user/default_profile_picture.png', 1),
(2, '', 1, NULL, '2020-11-13 10:41:56', 'admin@geoly.com', 'Admin', '$argon2id$v=19$m=4096,t=3,p=1$aMe4tkdEcHSWMMhAqzoq/A$/3+cnAVHW6FTkd8L8QaxHqBhTksETd34ERHaGz+N3wU', 'static/images/user/default_profile_picture.png', 1);

INSERT INTO `NOTIFICATION` (`id`, `created_at`, `data`, `seen`, `user_id`, `type`) VALUES
(1, '2020-11-13 10:41:56', '{\"userId\":1}', 0, 1, 'WELCOME'),
(2, '2020-11-13 10:41:56', '{\"userId\":2}', 0, 2, 'WELCOME');

INSERT INTO `QUEST` (`id`, `active`, `created_at`, `daily`, `description`, `difficulty`, `private_quest`, `category_id`, `user_id`, `name`, `premium`) VALUES
(1, 1, '2020-11-10 10:58:24', 1, 'Daily', 1, 0, 1, 1, 'Daily', 0);

INSERT INTO `STAGE` (`id`, `answer`, `latitude`, `longitude`, `qr_code_url`, `question`, `type`, `quest_id`, `advise`, `note`, `answers_list`) VALUES
(1, NULL, NULL, NULL, NULL, NULL, NULL, 1, NULL, NULL, NULL);

INSERT INTO `USER_OPTION` (`id`, `dark_mode`, `map_theme`, `private_profile`, `language_id`, `user_id`) VALUES
(1, 0, 1, 0, 2, 1),
(2, 0, 1, 0, 2, 2);

INSERT INTO `USER_ROLE` (`user_id`, `role_id`) VALUES
(1, 1),
(2, 3);