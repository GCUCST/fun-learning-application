/*
 Navicat Premium Data Transfer

 Source Server         : local-mysql_master
 Source Server Type    : MySQL
 Source Server Version : 80027
 Source Host           : localhost:3307
 Source Schema         : edu_db_2

 Target Server Type    : MySQL
 Target Server Version : 80027
 File Encoding         : 65001

 Date: 20/01/2022 13:05:36
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for course_1
-- ----------------------------
DROP TABLE IF EXISTS `course_1`;
CREATE TABLE `course_1` (
  `cid` bigint NOT NULL,
  `cname` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `user_id` bigint NOT NULL,
  `cstatus` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  PRIMARY KEY (`cid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Table structure for course_2
-- ----------------------------
DROP TABLE IF EXISTS `course_2`;
CREATE TABLE `course_2` (
  `cid` bigint NOT NULL,
  `cname` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `user_id` bigint NOT NULL,
  `cstatus` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  PRIMARY KEY (`cid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Table structure for udict
-- ----------------------------
DROP TABLE IF EXISTS `udict`;
CREATE TABLE `udict` (
  `udid` bigint NOT NULL,
  `udkey` varchar(20) COLLATE utf8mb4_bin NOT NULL,
  `udvalue` varchar(20) COLLATE utf8mb4_bin NOT NULL,
  PRIMARY KEY (`udid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

SET FOREIGN_KEY_CHECKS = 1;
