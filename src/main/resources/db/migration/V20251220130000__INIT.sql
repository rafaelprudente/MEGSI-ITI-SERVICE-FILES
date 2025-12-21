CREATE TABLE IF NOT EXISTS `tb_files`
(
    `id`                      uuid         NOT NULL,
    `user_id`                  uuid         NOT NULL,
    `original_name`           varchar(255) DEFAULT NULL,
    `stored_name`             varchar(255) DEFAULT NULL,
    `mime_type`               varchar(255) DEFAULT NULL,
    `size`                    BIGINT DEFAULT NULL,
    `version`                 BIGINT NOT NULL,
    `create_at`               datetime     NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_uca1400_ai_ci;