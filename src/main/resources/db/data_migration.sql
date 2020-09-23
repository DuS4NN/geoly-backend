INSERT INTO `badge` (`id`, `name`, `image_url`)
VALUES
	('1', 'FIRST_IN_SEASON','assets/images/badgeImages/season_1.svg'),
	('2', 'SECOND_IN_SEASON','assets/images/badgeImages/season_2.svg'),
  ('3', 'THIRD_IN_SEASON','assets/images/badgeImages/season_3.svg'),
  ('4', 'TOP_10_IN_SEASON','assets/images/badgeImages/season_10.svg'),
  ('5', 'TOP_50_IN_SEASON','assets/images/badgeImages/season_50.svg'),
  ('6', 'QUESTION_200','assets/images/badgeImages/question_200.svg'),
  ('7', 'QUESTION_100','assets/images/badgeImages/question_100.svg'),
  ('8', 'QUESTION_50','assets/images/badgeImages/question_50.svg'),
  ('9', 'QUESTION_10','assets/images/badgeImages/question_10.svg'),
  ('10', 'QRCODE_100','assets/images/badgeImages/qrcode_100.svg'),
  ('11', 'QRCODE_50','assets/images/badgeImages/qrcode_50.svg'),
  ('12', 'QRCODE_10','assets/images/badgeImages/qrcode_10.svg'),
  ('13', 'QRCODE_5','assets/images/badgeImages/qrcode_5.svg'),
  ('14', 'PLACE_300','assets/images/badgeImages/place_300.svg'),
  ('15', 'PLACE_150','assets/images/badgeImages/place_150.svg'),
  ('16', 'PLACE_50','assets/images/badgeImages/place_50.svg'),
  ('17', 'PLACE_20','assets/images/badgeImages/place_20.svg'),
  ('18', 'FINISH_500','assets/images/badgeImages/finish_500.svg'),
  ('19', 'FINISH_250','assets/images/badgeImages/finish_250.svg'),
  ('20', 'FINISH_100','assets/images/badgeImages/finish_100.svg'),
  ('21', 'FINISH_50','assets/images/badgeImages/finish_50.svg');
    
INSERT INTO `language` (`id`, `image_url`, `name`)
VALUES
	('1', '/static/image/language/slovak.png', 'Slovak'),
	('2', '/static/image/language/english.png', 'English');
    
INSERT INTO `user` (`id`, `nick_name`, `email`, `password`, `profile_image_url`, `about`, `active`, `verified`, `created_at`, `address`)
VALUES
	('1', '___DuS4NN', 'dusan@gmail.com', '$argon2id$v=19$m=4096,t=3,p=1$UCWqPEcfHpM0qrFioeg7Aw$oM93ZfNUGcxVv0/30bhdxm036wEA6yEUo1srcg4UWVQ', 'static/images/user/default_profile_picture.png', 'Vol�m sa Du�an :)', '1', '1', '2020-06-10 11:32:48', '51.952659, 7.632473'),
	('2', 'Marek123', 'marek@gmail.com','$argon2id$v=19$m=4096,t=3,p=1$UCWqPEcfHpM0qrFioeg7Aw$oM93ZfNUGcxVv0/30bhdxm036wEA6yEUo1srcg4UWVQ', 'static/images/user/default_profile_picture.png', 'Vol�m sa Marek :)', '1', '1', '2020-05-23 22:15:40', '48.141666319004486,17.15601670703128'),
    ('3', 'Petiir', 'petiir@gmail.com','$argon2id$v=19$m=4096,t=3,p=1$UCWqPEcfHpM0qrFioeg7Aw$oM93ZfNUGcxVv0/30bhdxm036wEA6yEUo1srcg4UWVQ', 'static/images/user/default_profile_picture.png', 'Vol�m sa Peto :)', '1', '1', '2020-04-18 18:23:12', '51.952659, 7.632473');
    
INSERT INTO `user_badge` (`id`, `user_id`, `badge_id`, `created_at`)
VALUES
    ('1', '1','1','2020-04-15 20:00:48'),
    ('2', '1','2','2020-06-10 11:32:48'),
    ('3', '2','3','2020-05-23 22:15:40');
    
INSERT INTO `user_option` (`id`, `user_id`, `language_id`, `dark_mode`, `map_theme`, `private_profile`)
VALUES
    ('1', '1', '1', '0', '1', '0'),
    ('2', '2', '1', '0', '1', '0'),
    ('3', '3', '2', '0', '1', '0');
    
INSERT INTO `role` (`id`, `name`)
VALUES
    ('1', 'USER'),
    ('2', 'MOD'),
    ('3', 'ADMIN');

INSERT INTO `point` (`id`, `user_id`, `amount`, `created_at`)
VALUES
    ('1', '1', '100', '2020-05-11 20:48:20'),
    ('2', '1', '70', '2020-06-22 19:25:15'),
    ('3', '2', '80', '2020-06-15 14:10:43'),
    ('4', '2', '55', '2020-06-12 23:54:24'),
    ('5', '3', '120', '2020-06-09 16:12:34');
    
INSERT INTO `user_report` (`id`, `reported`, `complainant`, `reason`, `created_at`)
VALUES
    ('1', '1', '2', 'INAPPROPRIATE_IMAGE', '2020-06-15 14:34:20');
    
INSERT INTO `category` (`id`,`name`, `image_url`)
VALUES
    ('1', 'HISTORY', '/static/image/category/history.png'),
    ('2', 'ART', '/static/image/category/art.png'),
    ('3', 'VIEW', '/static/image/category/view.png'),
    ('4', 'ARCHITECTURE', '/static/image/category/architecture.png'),
    ('5', 'NATURE', '/static/image/category/nature.png');
    
INSERT INTO `Quest` (`id`, `user_id`, `category_id`, `difficulty`, `description`, `created_at`, `active`, `private_quest`, `daily`, `name`)
VALUES
    ('1', '1', '1', '1', 'Popisok', '2020-05-11 20:48:20', '1', '0', '0', 'Jakubov Palác'),
    ('2', '2', '2', '3', '', '2020-05-11 20:48:20', '1', '0', '0', 'Test'),
    ('3', '1', '3', '5', 'Nejaky popis', '2020-05-11 20:48:20', '1', '1', '0', 'Košické jazero');
    
INSERT INTO `quest_review` (`id`, `user_id`, `quest_id`, `review_text`, `review`, `created_at`)
VALUES
    ('1', '1', '2', 'Je to paradne 5/5', '5', '2020-05-11 20:48:20');
    
INSERT INTO `party` (`id`, `user_id`, `name`, `created_at`)
VALUES
    ('1', '2', 'ELITA', '2020-06-20 13:05:10');
    
INSERT INTO `party_invite` (`id`, `user_id`, `party_id`, `status`, `created_at`)
VALUES
    ('1', '2', '1', 'ACCEPTED', '2020-06-20 10:45:10'),
    ('2', '3', '1', 'PENDING', '2020-06-19 14:45:10');
    
INSERT INTO `party_user` (`id`, `party_id`, `user_id`, `created_at`)
VALUES
    ('1', '1', '2', '2020-06-20 14:45:10');
    
INSERT INTO `party_quest` (`id`, `party_id`, `quest_id`, `active`)
VALUES
    ('1', '1', '3', '1');
    
INSERT INTO `stage` (`id`, `quest_id`, `type`, `latitude`, `longitude`, `question`, `answer`)
VALUES
    ('1', '1', 'GO_TO_PLACE', '48.336169', '17.838933', '', ''),
    ('2', '1', 'ANSWER_QUESTION', '', '', 'Ako sa mas', 'Dobre'),
    ('3', '2', 'GO_TO_PLACE', '48.336149', '17.832933', '', '');
    
INSERT INTO `user_quest` (`id`, `stage_id`, `user_id`, `status`, `created_at`, `updated_at`)
VALUES
    ('1', '1', '1' ,'FINISHED', '2020-06-20 14:45:10', '2020-06-20 15:45:10'),
    ('2', '2', '1' ,'ON_STAGE', '2020-06-20 14:45:10', '2020-06-20 14:45:10'),
    ('3', '3', '2' ,'CANCELED', '2020-07-01 12:15:18', '2020-07-01 12:20:42');