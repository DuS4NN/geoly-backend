INSERT INTO `language` (`id`, `image_url`, `name`)
VALUES
	('1', '/static/image/language/slovak.png', 'Slovak'),
	('2', '/static/image/language/english.png', 'English');
    
INSERT INTO `badge` (`id`, `name`, `image_url`)
VALUES
	('1', 'FIRST_IN_SEASON','/static/image/badge/first_in_season.png'),
	('2', 'SECOND_IN_SEASON','/static/image/badge/second_in_season.png'),
    ('3', 'THIRD_IN_SEASON','/static/image/badge/third_in_season.png'),
    ('4', 'TOP_10_IN_SEASON','/static/image/badge/top_50_in_season.png'),
    ('5', 'TOP_50_IN_SEASON','/static/image/badge/top_50_in_season.png');
    
INSERT INTO `user` (`id`, `nick_name`, `email`, `password`, `profile_image_url`, `about`, `active`, `verified`, `created_at`, `address`)
VALUES
	('1', '___DuS4NN', 'dusan@gmail.com', 'c8dce9d507f88191ca7b1f65a42810eb', 'static/image/default_profile_picture.png', 'Volám sa Dušan :)', '1', '1', '2020-06-10 11:32:48', '51.952659, 7.632473'),
	('2', 'Marek123', 'marek@gmail.com','c8dce9d507f88191ca7b1f65a42810eb', 'static/image/default_profile_picture.png', 'Volám sa Marek :)', '1', '1', '2020-05-23 22:15:40', '48.141666319004486,17.15601670703128'),
    ('3', 'Petiir', 'petiir@gmail.com','c8dce9d507f88191ca7b1f65a42810eb', 'static/image/default_profile_picture.png', 'Volám sa Peto :)', '1', '1', '2020-04-18 18:23:12', '51.952659, 7.632473');
    
INSERT INTO `user_badge` (`id`, `user_id`, `badge_id`, `received_at`)
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
    
INSERT INTO `user_role` (`id`, `role_id`, `user_id`)
VALUES
    ('1', '1', '1'),
    ('2', '2', '1'),
    ('3', '3', '2');
    
INSERT INTO `point` (`id`, `user_id`, `amount`, `receive_at`)
VALUES
    ('1', '1', '100', '2020-05-11 20:48:20'),
    ('2', '1', '70', '2020-06-22 19:25:15'),
    ('3', '2', '80', '2020-06-15 14:10:43'),
    ('4', '2', '55', '2020-06-12 23:54:24'),
    ('5', '3', '120', '2020-06-09 16:12:34');
    
INSERT INTO `transaction` (`id`, `user_id`, `uuid`, `amount`, `created_at`, `verified`)
VALUES
    ('1', '1', 'c9e9a2e2-b9fd-11ea-b3de-0242ac130004', '2.0', '2020-05-1 20:48:20', '1'),
    ('2', '1', '3dc22ffd-172a-4822-bcbd-1aae56a8fd85', '3.0','2020-06-1 19:25:15', '1'),
    ('3', '2', '46961a6f-e64a-4dac-b36d-24c6bf212984', '3.0','2020-06-2 14:10:43', '1');
    
INSERT INTO `premium` (`id`, `user_id`, `transaction_id`, `start_at`, `end_at`)
VALUES
    ('1', '1', '1', '2020-05-1 20:48:20', '2020-06-1 20:48:20'),
    ('2', '1', '2', '2020-06-1 19:25:15', '2020-07-1 19:25:15'),
    ('3', '2', '3', '2020-06-2 14:10:43', '2020-07-2 14:10:43');
    
INSERT INTO `user_report` (`id`, `reported`, `complainant`, `reason`, `created_at`)
VALUES
    ('1', '1', '2', 'INAPPROPRIATE_IMAGE', '2020-06-15 14:34:20');

INSERT INTO `category` (`id`,`name`)
VALUES
    ('1', 'HISTORY'),
    ('2', 'ART'),
    ('3', 'VIEW'),
    ('4', 'ARCHITECTURE'),
    ('5', 'NATURE');
    
INSERT INTO `Quest` (`id`, `user_id`, `category_id`, `difficulty`, `description`, `created_at`, `active`, `private_quest`, `daily`)
VALUES
    ('1', '1', '1', '1', 'Popisok', '2020-05-11 20:48:20', '1', '0', '0'),
    ('2', '2', '2', '3', '', '2020-05-11 20:48:20', '1', '0', '0'),
    ('3', '1', '3', '5', 'Nejaky popis', '2020-05-11 20:48:20', '1', '1', '0');
    
INSERT INTO `quest_review` (`id`, `user_id`, `quest_id`, `review_text`, `review`)
VALUES
    ('1', '1', '1', 'Je to paradne 5/5', '5');
    
INSERT INTO `status` (`id`, `name`)
VALUES
    ('1', 'ON_STAGE'),
    ('2', 'FINISHED'),
    ('3', 'CANCELED');
    
INSERT INTO `type` (`id`, `name`)
VALUES
    ('1', 'GO_TO_PLACE'),
    ('2','ANSWER_QUESTION'),
    ('3','SCAN_QR_CODE');
    
INSERT INTO `party` (`id`, `user_id`, `name`, `created_at`)
VALUES
    ('1', '2', 'ELITA', '2020-06-20 13:05:10');
    
INSERT INTO `party_invate` (`id`, `user_id`, `party_id`, `state`, `created_at`)
VALUES
    ('1', '2', '1', 'ACCEPTED', '2020-06-20 10:45:10'),
    ('2', '3', '1', 'PENDING', '2020-06-19 14:45:10');
    
INSERT INTO `party_user` (`id`, `party_id`, `user_id`, `created_at`)
VALUES
    ('1', '1', '2', '2020-06-20 14:45:10');
    
INSERT INTO `party_quest` (`id`, `party_id`, `quest_id`, `active`)
VALUES
    ('1', '1', '3', '1');
    
INSERT INTO `stage` (`id`, `quest_id`, `type_id`, `latiude`, `longitude`, `question`, `answer`)
VALUES
    ('1', '1', '1', '48.336169', '17.838933', '', ''),
    ('2', '1', '2', '', '', 'Ako sa mas', 'Dobre'),
    ('3', '2', '1', '48.336149', '17.832933', '', '');
    
INSERT INTO `user_quest` (`id`, `quest_id`, `user_id`, `stage_id`, `status_id`, `created_at`)
VALUES
    ('1', '1', '1', '1', '2', '2020-06-20 14:45:10'),
    ('2', '1', '1', '2', '1', '2020-06-20 14:45:10'),
    ('3', '2', '2', '3', '3', '2020-06-20 14:45:10');
    