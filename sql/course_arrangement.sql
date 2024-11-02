/*
 Navicat Premium Data Transfer

 Source Server         : For
 Source Server Type    : MySQL
 Source Server Version : 80030
 Source Host           : localhost:3306
 Source Schema         : course_arrangement

 Target Server Type    : MySQL
 Target Server Version : 80030
 File Encoding         : 65001

 Date: 02/11/2024 15:00:44

*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for class_info
-- ----------------------------
DROP TABLE IF EXISTS `class_info`;
CREATE TABLE `class_info`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'id',
  `college_no` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '学院编号',
  `grade_no` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '年级编号',
  `major_no` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '专业编号',
  `class_no` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '班级编号',
  `class_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '班级名称',
  `teacher_no` varchar(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '班主任工号',
  `student_number` int NULL DEFAULT NULL COMMENT '班级人数',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `teacher_no`(`teacher_no` ASC) USING BTREE,
  INDEX `class_no`(`class_no` ASC) USING BTREE,
  CONSTRAINT `class_info_ibfk_1` FOREIGN KEY (`teacher_no`) REFERENCES `teacher` (`teacher_no`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 16 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of class_info
-- ----------------------------
INSERT INTO `class_info` VALUES (7, '22', '20', '11', '20221101', '2020级计算机科学与技术1班', '22001', 26);
INSERT INTO `class_info` VALUES (8, '22', '20', '12', '20221201', '2020级信息安全班', '22002', 0);
INSERT INTO `class_info` VALUES (9, '11', '20', '17', '20111701', '2020级临床医学1班', '11001', 42);
INSERT INTO `class_info` VALUES (10, '17', '20', '29', '20172901', '2020级英语1班', '17001', 0);
INSERT INTO `class_info` VALUES (11, '16', '20', '27', '20162701', '2020级汉语言文学1班', '16001', 0);
INSERT INTO `class_info` VALUES (12, '22', '20', '13', '20221301', '2020级通信工程班', '22006', 0);
INSERT INTO `class_info` VALUES (13, '22', '21', '11', '21221101', '2021级计算机科学与技术1班', '22003', 1);
INSERT INTO `class_info` VALUES (14, '13', '22', '20', '22132001', '2022级护理学1班', '13001', 0);
INSERT INTO `class_info` VALUES (15, '22', '20', '14', '20221401', '2020级数学与应用数学班', '22007', 0);
INSERT INTO `class_info` VALUES (16, '24', '20', '30', '20243001', '2020级学前教育1班', '24001', 0);

-- ----------------------------
-- Table structure for classroom
-- ----------------------------
DROP TABLE IF EXISTS `classroom`;
CREATE TABLE `classroom`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `teaching_building_no` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '教学楼编号',
  `classroom_type` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '教室类型',
  `classroom_no` varchar(6) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '教室编号',
  `classroom_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '教室名称',
  `capacity` int NULL DEFAULT NULL COMMENT '教室容量',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `classroom_no`(`classroom_no` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 42 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of classroom
-- ----------------------------
INSERT INTO `classroom` VALUES (2, '1', '02', '10201', '一教一大', 120);
INSERT INTO `classroom` VALUES (3, '1', '02', '10202', '一教二大', 120);
INSERT INTO `classroom` VALUES (4, '1', '01', '10101', 'J1-101', 90);
INSERT INTO `classroom` VALUES (5, '1', '01', '10102', 'J1-416', 60);
INSERT INTO `classroom` VALUES (6, '2', '02', '20201', 'J2A-101', 120);
INSERT INTO `classroom` VALUES (7, '2', '02', '20202', 'J2A-102', 120);
INSERT INTO `classroom` VALUES (8, '2', '01', '20101', 'J2B-101', 60);
INSERT INTO `classroom` VALUES (9, '4', '03', '40301', '工501', 80);
INSERT INTO `classroom` VALUES (10, '4', '03', '40302', '工503', 80);
INSERT INTO `classroom` VALUES (11, '3', '04', '30401', 'S3-201', 60);
INSERT INTO `classroom` VALUES (12, '3', '04', '30402', 'S3-202', 60);
INSERT INTO `classroom` VALUES (13, '7', '09', '70901', '体育馆东侧', 120);
INSERT INTO `classroom` VALUES (14, '7', '09', '70902', '体育馆西侧', 120);
INSERT INTO `classroom` VALUES (15, '8', '08', '80801', '南操场', 120);
INSERT INTO `classroom` VALUES (16, '8', '08', '80802', '南操场', 120);
INSERT INTO `classroom` VALUES (17, '9', '08', '90801', '北操场', 120);
INSERT INTO `classroom` VALUES (18, '9', '08', '90802', '北操场', 120);
INSERT INTO `classroom` VALUES (20, '6', '07', '60701', '音乐教室1', 80);
INSERT INTO `classroom` VALUES (21, '6', '07', '60702', '音乐教室2', 80);
INSERT INTO `classroom` VALUES (22, '6', '06', '60601', '舞蹈教室1', 60);
INSERT INTO `classroom` VALUES (23, '6', '06', '60602', '舞蹈教室2', 60);
INSERT INTO `classroom` VALUES (24, '1', '01', '10103', 'J1-102', 50);
INSERT INTO `classroom` VALUES (25, '1', '01', '10104', 'J1-103', 50);
INSERT INTO `classroom` VALUES (26, '1', '01', '10105', 'J1-104', 50);
INSERT INTO `classroom` VALUES (27, '1', '01', '10106', 'J1-105', 50);
INSERT INTO `classroom` VALUES (28, '1', '01', '10107', 'J1-106', 50);
INSERT INTO `classroom` VALUES (29, '1', '01', '10108', 'J1-107', 50);
INSERT INTO `classroom` VALUES (30, '1', '01', '10109', 'J1-108', 50);
INSERT INTO `classroom` VALUES (31, '2', '01', '20102', 'J2B-102', 50);
INSERT INTO `classroom` VALUES (32, '2', '01', '20103', 'J2B-103', 50);
INSERT INTO `classroom` VALUES (33, '2', '01', '20104', 'J2B-104', 50);
INSERT INTO `classroom` VALUES (34, '2', '01', '20105', 'J2B-105', 50);
INSERT INTO `classroom` VALUES (35, '2', '01', '20106', 'J2B-106', 50);
INSERT INTO `classroom` VALUES (36, '4', '03', '40303', '工504', 80);
INSERT INTO `classroom` VALUES (37, '4', '03', '40304', '工506', 80);
INSERT INTO `classroom` VALUES (38, '1', '02', '10203', '一教三大', 120);
INSERT INTO `classroom` VALUES (39, '1', '02', '10204', '一教四大', 120);
INSERT INTO `classroom` VALUES (40, '3', '03', '30301', 'S-210', 120);
INSERT INTO `classroom` VALUES (41, '3', '03', '30302', 'S-211', 120);
INSERT INTO `classroom` VALUES (42, '3', '02', '30201', 'S-110', 100);

-- ----------------------------
-- Table structure for course_info
-- ----------------------------
DROP TABLE IF EXISTS `course_info`;
CREATE TABLE `course_info`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'id',
  `college_no` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '开课学院',
  `course_attribute` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '课程属性',
  `course_no` varchar(6) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '课程编号',
  `course_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '课程名称',
  `teacher_no` varchar(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '授课教师工号',
  `classroom_type` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '教室类型',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `teacher_no`(`teacher_no` ASC) USING BTREE,
  INDEX `course_no`(`course_no` ASC) USING BTREE,
  CONSTRAINT `course_info_ibfk_1` FOREIGN KEY (`teacher_no`) REFERENCES `teacher` (`teacher_no`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 63 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of course_info
-- ----------------------------
INSERT INTO `course_info` VALUES (4, '22', '1', '221001', '数据结构（理论）', '22002', '01');
INSERT INTO `course_info` VALUES (5, '22', '3', '223001', '数据结构（实验）', '22002', '03');
INSERT INTO `course_info` VALUES (6, '22', '1', '221002', '计算机组成原理（理论）', '22003', '01');
INSERT INTO `course_info` VALUES (7, '22', '3', '223002', '计算机组成原理（实验）', '22003', '03');
INSERT INTO `course_info` VALUES (8, '22', '1', '221003', '操作系统（理论）', '22005', '01');
INSERT INTO `course_info` VALUES (9, '22', '3', '223003', '操作系统（实验）', '22005', '03');
INSERT INTO `course_info` VALUES (10, '22', '1', '221004', '计算机网络（理论）', '22006', '01');
INSERT INTO `course_info` VALUES (11, '22', '3', '223004', '计算机网络（实验）', '22006', '03');
INSERT INTO `course_info` VALUES (12, '19', '4', '194001', '大学体育1', '19001', '08');
INSERT INTO `course_info` VALUES (13, '19', '4', '194002', '大学体育2（瑜伽）', '19002', '09');
INSERT INTO `course_info` VALUES (14, '17', '1', '171001', '大学英语1', '17001', '02');
INSERT INTO `course_info` VALUES (15, '17', '1', '171002', '大学英语2', '17002', '02');
INSERT INTO `course_info` VALUES (16, '17', '2', '172001', '大学英语3', '17003', '02');
INSERT INTO `course_info` VALUES (17, '22', '1', '221005', '面向对象程序设计（理论）', '22004', '01');
INSERT INTO `course_info` VALUES (18, '22', '3', '223005', '面向对象程序设计（实验）', '22004', '03');
INSERT INTO `course_info` VALUES (19, '18', '1', '181003', '马克思主义基本原理概论', '18001', '02');
INSERT INTO `course_info` VALUES (20, '18', '1', '181002', '毛泽东思想和中国特色社会主义理论体系概论', '18002', '02');
INSERT INTO `course_info` VALUES (21, '18', '1', '181004', '中国近现代史纲要', '18004', '02');
INSERT INTO `course_info` VALUES (22, '18', '1', '181005', '思想道德修养与法律基础', '18004', '02');
INSERT INTO `course_info` VALUES (23, '16', '1', '161001', '文史综合1', '16002', '02');
INSERT INTO `course_info` VALUES (24, '16', '1', '161002', '文史综合2', '16002', '02');
INSERT INTO `course_info` VALUES (25, '22', '1', '221006', '数理综合 1', '22007', '02');
INSERT INTO `course_info` VALUES (26, '22', '1', '221007', '数理综合 2', '22007', '02');
INSERT INTO `course_info` VALUES (27, '18', '1', '181006', '形势与政策', '18005', '02');
INSERT INTO `course_info` VALUES (28, '25', '1', '251001', '艺术通识与鉴赏', '25001', '07');
INSERT INTO `course_info` VALUES (29, '25', '1', '251002', '艺术通识与鉴赏', '25002', '07');
INSERT INTO `course_info` VALUES (30, '16', '1', '161003', '现代汉语 1', '16004', '01');
INSERT INTO `course_info` VALUES (31, '16', '1', '161004', '中国古代文学1', '16003', '01');
INSERT INTO `course_info` VALUES (32, '16', '1', '161005', '大学写作', '16002', '01');
INSERT INTO `course_info` VALUES (33, '24', '1', '241001', '普通教育学', '24001', '02');
INSERT INTO `course_info` VALUES (34, '24', '1', '241002', '普通心理学', '24002', '02');
INSERT INTO `course_info` VALUES (35, '24', '1', '241003', '教育心理学', '24003', '01');
INSERT INTO `course_info` VALUES (36, '24', '1', '241004', '中外教育史', '24004', '01');
INSERT INTO `course_info` VALUES (37, '24', '1', '241005', '幼儿园课程理论与实践', '24005', '02');
INSERT INTO `course_info` VALUES (38, '24', '1', '241006', '幼儿园游戏理论与实践', '24006', '01');
INSERT INTO `course_info` VALUES (39, '24', '1', '241007', '幼儿教师艺术技能', '24007', '01');
INSERT INTO `course_info` VALUES (40, '11', '1', '111001', '人体解剖学', '11001', '01');
INSERT INTO `course_info` VALUES (41, '11', '1', '111002', '组织与胚胎学', '11002', '02');
INSERT INTO `course_info` VALUES (42, '13', '1', '131001', '护理学导论', '13001', '01');
INSERT INTO `course_info` VALUES (43, '11', '1', '111003', '生理学', '11004', '01');
INSERT INTO `course_info` VALUES (44, '11', '1', '111004', '生物化学', '11005', '01');
INSERT INTO `course_info` VALUES (45, '13', '1', '131002', '内科护理学 1', '13002', '01');
INSERT INTO `course_info` VALUES (46, '13', '1', '131003', '外科护理学 1', '13003', '01');
INSERT INTO `course_info` VALUES (47, '22', '1', '221008', '模拟电子技术', '22008', '01');
INSERT INTO `course_info` VALUES (48, '22', '1', '221009', '复变函数与积分变换', '22009', '01');
INSERT INTO `course_info` VALUES (49, '22', '1', '221010', '概率论与数理统计', '22010', '01');
INSERT INTO `course_info` VALUES (50, '22', '1', '221011', '数字电子技术', '22011', '01');
INSERT INTO `course_info` VALUES (51, '22', '1', '221012', '信号与系统', '22012', '01');
INSERT INTO `course_info` VALUES (52, '22', '1', '221013', '现代通信原理', '22013', '01');
INSERT INTO `course_info` VALUES (53, '22', '1', '221014', '解析几何', '22014', '01');
INSERT INTO `course_info` VALUES (54, '22', '1', '221015', '数学分析1', '22015', '01');
INSERT INTO `course_info` VALUES (55, '22', '1', '221016', '高等代数1', '22017', '01');
INSERT INTO `course_info` VALUES (56, '22', '1', '221017', '离散数学', '22016', '01');
INSERT INTO `course_info` VALUES (57, '22', '1', '221018', '大学数学1', '22014', '02');
INSERT INTO `course_info` VALUES (58, '22', '1', '221019', '大学数学2', '22015', '02');
INSERT INTO `course_info` VALUES (59, '22', '1', '221020', '高等数学1', '22010', '01');
INSERT INTO `course_info` VALUES (60, '22', '1', '221021', '高等数学2', '22012', '01');
INSERT INTO `course_info` VALUES (61, '19', '4', '194003', '大学体育1', '19004', '09');
INSERT INTO `course_info` VALUES (62, '22', '1', '221022', '数据库原理及应用（理论）', '22002', '01');
INSERT INTO `course_info` VALUES (63, '22', '1', '221023', '数据库原理及应用（实验）', '22002', '03');

-- ----------------------------
-- Table structure for course_plan
-- ----------------------------
DROP TABLE IF EXISTS `course_plan`;
CREATE TABLE `course_plan`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'id',
  `term` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '学期',
  `college_no` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '学院编号',
  `grade_no` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '年级编号',
  `class_no` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '班级编号',
  `course_no` varchar(6) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '课程编号',
  `week_time` int NULL DEFAULT NULL COMMENT '周学时',
  `weeks` int NULL DEFAULT NULL COMMENT '周数',
  `is_fix` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '上课时间是否固定',
  `course_time` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '上课时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `class_no`(`class_no` ASC) USING BTREE,
  INDEX `course_no`(`course_no` ASC) USING BTREE,
  CONSTRAINT `course_plan_ibfk_1` FOREIGN KEY (`class_no`) REFERENCES `class_info` (`class_no`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `course_plan_ibfk_2` FOREIGN KEY (`course_no`) REFERENCES `course_info` (`course_no`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 75 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of course_plan
-- ----------------------------
INSERT INTO `course_plan` VALUES (4, '2020-2021-1', '22', '20', '20221101', '221001', 4, 12, '0', NULL);
INSERT INTO `course_plan` VALUES (5, '2020-2021-1', '22', '20', '20221101', '223001', 2, 12, '0', NULL);
INSERT INTO `course_plan` VALUES (6, '2020-2021-1', '22', '20', '20221101', '194001', 2, 12, '0', NULL);
INSERT INTO `course_plan` VALUES (7, '2020-2021-1', '22', '20', '20221101', '171001', 2, 12, '0', NULL);
INSERT INTO `course_plan` VALUES (10, '2020-2021-1', '22', '20', '20221201', '221020', 4, 12, '0', NULL);
INSERT INTO `course_plan` VALUES (12, '2020-2021-1', '11', '20', '20111701', '171001', 2, 12, '0', NULL);
INSERT INTO `course_plan` VALUES (13, '2020-2021-1', '11', '20', '20111701', '194002', 2, 12, '1', '02');
INSERT INTO `course_plan` VALUES (14, '2020-2021-1', '22', '20', '20221101', '251001', 2, 12, '1', '22');
INSERT INTO `course_plan` VALUES (15, '2020-2021-1', '22', '20', '20221101', '181003', 4, 12, '0', NULL);
INSERT INTO `course_plan` VALUES (16, '2020-2021-1', '22', '20', '20221101', '161001', 2, 12, '0', NULL);
INSERT INTO `course_plan` VALUES (17, '2020-2021-1', '22', '20', '20221201', '251002', 2, 12, '1', '22');
INSERT INTO `course_plan` VALUES (18, '2020-2021-1', '22', '20', '20221201', '194001', 2, 12, '0', NULL);
INSERT INTO `course_plan` VALUES (19, '2020-2021-1', '22', '20', '20221201', '181002', 4, 12, '0', NULL);
INSERT INTO `course_plan` VALUES (20, '2020-2021-1', '22', '20', '20221201', '221001', 4, 12, '0', NULL);
INSERT INTO `course_plan` VALUES (21, '2020-2021-1', '22', '20', '20221201', '223001', 2, 12, '0', NULL);
INSERT INTO `course_plan` VALUES (22, '2020-2021-1', '22', '20', '20221301', '181003', 4, 12, '0', NULL);
INSERT INTO `course_plan` VALUES (23, '2020-2021-1', '22', '20', '20221301', '221011', 4, 12, '0', NULL);
INSERT INTO `course_plan` VALUES (24, '2020-2021-1', '22', '20', '20221301', '221010', 4, 2, '0', NULL);
INSERT INTO `course_plan` VALUES (25, '2020-2021-1', '22', '20', '20221301', '221012', 2, 14, '0', NULL);
INSERT INTO `course_plan` VALUES (26, '2020-2021-1', '22', '20', '20221301', '194003', 2, 14, '0', NULL);
INSERT INTO `course_plan` VALUES (27, '2020-2021-1', '13', '22', '22132001', '194003', 2, 12, '0', NULL);
INSERT INTO `course_plan` VALUES (28, '2020-2021-1', '13', '22', '22132001', '171001', 2, 14, '0', NULL);
INSERT INTO `course_plan` VALUES (29, '2020-2021-1', '13', '22', '22132001', '181002', 4, 2, '0', NULL);
INSERT INTO `course_plan` VALUES (30, '2020-2021-1', '13', '22', '22132001', '111001', 4, 12, '0', NULL);
INSERT INTO `course_plan` VALUES (31, '2020-2021-1', '13', '22', '22132001', '111002', 4, 14, '0', NULL);
INSERT INTO `course_plan` VALUES (32, '2020-2021-1', '13', '22', '22132001', '131001', 2, 14, '0', NULL);
INSERT INTO `course_plan` VALUES (33, '2020-2021-1', '13', '22', '22132001', '221018', 2, 14, '0', NULL);
INSERT INTO `course_plan` VALUES (34, '2020-2021-1', '13', '22', '22132001', '111004', 4, 12, '0', NULL);
INSERT INTO `course_plan` VALUES (35, '2020-2021-1', '22', '20', '20221301', '161001', 2, 14, '0', NULL);
INSERT INTO `course_plan` VALUES (36, '2020-2021-1', '22', '20', '20221401', '171002', 2, 14, '0', NULL);
INSERT INTO `course_plan` VALUES (37, '2020-2021-1', '22', '20', '20221401', '181005', 2, 14, '0', NULL);
INSERT INTO `course_plan` VALUES (38, '2020-2021-1', '22', '20', '20221401', '181006', 2, 14, '0', NULL);
INSERT INTO `course_plan` VALUES (39, '2020-2021-1', '22', '20', '20221401', '221014', 4, 12, '0', NULL);
INSERT INTO `course_plan` VALUES (40, '2020-2021-1', '22', '20', '20221401', '221015', 4, 12, '0', NULL);
INSERT INTO `course_plan` VALUES (41, '2020-2021-1', '22', '20', '20221401', '221016', 4, 12, '0', NULL);
INSERT INTO `course_plan` VALUES (42, '2020-2021-1', '22', '20', '20221401', '221017', 2, 14, '0', NULL);
INSERT INTO `course_plan` VALUES (43, '2020-2021-1', '22', '20', '20221401', '241001', 2, 14, '0', NULL);
INSERT INTO `course_plan` VALUES (44, '2020-2021-1', '24', '20', '20243001', '181004', 4, 12, '0', NULL);
INSERT INTO `course_plan` VALUES (45, '2020-2021-1', '24', '20', '20243001', '241001', 2, 14, '0', NULL);
INSERT INTO `course_plan` VALUES (46, '2020-2021-1', '24', '20', '20243001', '241004', 4, 14, '0', NULL);
INSERT INTO `course_plan` VALUES (47, '2020-2021-1', '24', '20', '20243001', '241005', 4, 14, '0', NULL);
INSERT INTO `course_plan` VALUES (48, '2020-2021-1', '24', '20', '20243001', '241006', 4, 12, '0', NULL);
INSERT INTO `course_plan` VALUES (49, '2020-2021-1', '24', '20', '20243001', '221018', 2, 14, '0', NULL);
INSERT INTO `course_plan` VALUES (50, '2020-2021-1', '24', '20', '20243001', '194002', 2, 14, '0', NULL);
INSERT INTO `course_plan` VALUES (51, '2020-2021-1', '24', '20', '20243001', '172001', 2, 14, '0', NULL);
INSERT INTO `course_plan` VALUES (52, '2020-2021-1', '16', '20', '20162701', '171002', 2, 14, '0', NULL);
INSERT INTO `course_plan` VALUES (53, '2020-2021-1', '16', '20', '20162701', '181002', 4, 12, '0', NULL);
INSERT INTO `course_plan` VALUES (54, '2020-2021-1', '16', '20', '20162701', '251001', 2, 15, '0', NULL);
INSERT INTO `course_plan` VALUES (55, '2020-2021-1', '16', '20', '20162701', '161003', 4, 12, '0', NULL);
INSERT INTO `course_plan` VALUES (56, '2020-2021-1', '16', '20', '20162701', '161004', 4, 14, '0', NULL);
INSERT INTO `course_plan` VALUES (57, '2020-2021-1', '16', '20', '20162701', '161005', 4, 12, '0', NULL);
INSERT INTO `course_plan` VALUES (58, '2020-2021-1', '16', '20', '20162701', '221018', 2, 12, '0', NULL);
INSERT INTO `course_plan` VALUES (59, '2020-2021-1', '16', '20', '20162701', '194003', 2, 12, '0', NULL);
INSERT INTO `course_plan` VALUES (60, '2020-2021-1', '22', '20', '20221101', '221005', 2, 14, '0', NULL);
INSERT INTO `course_plan` VALUES (61, '2020-2021-1', '22', '20', '20221101', '223005', 2, 14, '0', NULL);
INSERT INTO `course_plan` VALUES (62, '2020-2021-1', '22', '20', '20221101', '221020', 4, 14, '0', NULL);
INSERT INTO `course_plan` VALUES (63, '2020-2021-1', '22', '20', '20221201', '221022', 2, 14, '0', NULL);
INSERT INTO `course_plan` VALUES (64, '2020-2021-1', '22', '20', '20221201', '221023', 2, 14, '0', NULL);
INSERT INTO `course_plan` VALUES (65, '2020-2021-1', '22', '20', '20221201', '221017', 2, 12, '0', NULL);
INSERT INTO `course_plan` VALUES (66, '2020-2021-1', '22', '20', '20221201', '181005', 2, 14, '0', NULL);
INSERT INTO `course_plan` VALUES (67, '2020-2021-1', '22', '20', '20221301', '221004', 2, 14, '0', NULL);
INSERT INTO `course_plan` VALUES (68, '2020-2021-1', '22', '20', '20221301', '223004', 2, 14, '0', NULL);
INSERT INTO `course_plan` VALUES (69, '2020-2021-1', '22', '20', '20221301', '171002', 2, 14, '0', NULL);
INSERT INTO `course_plan` VALUES (70, '2020-2021-1', '11', '20', '20111701', '181004', 2, 14, '0', NULL);
INSERT INTO `course_plan` VALUES (71, '2020-2021-1', '11', '20', '20111701', '111001', 2, 14, '0', NULL);
INSERT INTO `course_plan` VALUES (72, '2020-2021-1', '11', '20', '20111701', '111002', 4, 13, '0', NULL);
INSERT INTO `course_plan` VALUES (73, '2020-2021-1', '11', '20', '20111701', '111003', 4, 12, '0', NULL);
INSERT INTO `course_plan` VALUES (74, '2020-2021-1', '11', '20', '20111701', '221023', 2, 14, '0', NULL);
INSERT INTO `course_plan` VALUES (75, '2020-2021-1', '11', '20', '20111701', '251002', 2, 13, '0', NULL);

-- ----------------------------
-- Table structure for notification
-- ----------------------------
DROP TABLE IF EXISTS `notification`;
CREATE TABLE `notification`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `college_no` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '学院编号',
  `user` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '发布人',
  `time` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '发布时间',
  `title` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '标题',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '内容',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of notification
-- ----------------------------
INSERT INTO `notification` VALUES (7, '22', '22001', '2024-02-01 21:42:33', 'Hello World！', '![7.webp](http://localhost:8081/file/e2239cd25a3e4bc2ae896934a809d89c.webp)');

-- ----------------------------
-- Table structure for schedule
-- ----------------------------
DROP TABLE IF EXISTS `schedule`;
CREATE TABLE `schedule`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'id',
  `term` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '学期',
  `grade_no` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '年级编号',
  `class_no` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '班级编号',
  `course_no` varchar(6) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '课程编号',
  `teacher_no` varchar(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '教师编号',
  `classroom_no` varchar(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '教室编号',
  `course_time` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '上课时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `class_no`(`class_no` ASC) USING BTREE,
  INDEX `course_no`(`course_no` ASC) USING BTREE,
  INDEX `teacher_no`(`teacher_no` ASC) USING BTREE,
  INDEX `classroom_no`(`classroom_no` ASC) USING BTREE,
  CONSTRAINT `schedule_ibfk_1` FOREIGN KEY (`class_no`) REFERENCES `class_info` (`class_no`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `schedule_ibfk_2` FOREIGN KEY (`course_no`) REFERENCES `course_info` (`course_no`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `schedule_ibfk_3` FOREIGN KEY (`teacher_no`) REFERENCES `teacher` (`teacher_no`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `schedule_ibfk_4` FOREIGN KEY (`classroom_no`) REFERENCES `classroom` (`classroom_no`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 14226 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of schedule
-- ----------------------------
INSERT INTO `schedule` VALUES (14132, '2020-2021-1', '20', '20111701', '194002', '19002', '70902', '02');
INSERT INTO `schedule` VALUES (14133, '2020-2021-1', '20', '20221101', '251001', '25001', '60701', '22');
INSERT INTO `schedule` VALUES (14134, '2020-2021-1', '20', '20221201', '251002', '25002', '60702', '22');
INSERT INTO `schedule` VALUES (14135, '2020-2021-1', '20', '20221101', '221001', '22002', '10103', '21');
INSERT INTO `schedule` VALUES (14136, '2020-2021-1', '20', '20221101', '221001', '22002', '10104', '02');
INSERT INTO `schedule` VALUES (14137, '2020-2021-1', '20', '20221101', '223001', '22002', '40302', '05');
INSERT INTO `schedule` VALUES (14138, '2020-2021-1', '20', '20221101', '194001', '19001', '80801', '04');
INSERT INTO `schedule` VALUES (14139, '2020-2021-1', '20', '20221101', '171001', '17001', '10203', '25');
INSERT INTO `schedule` VALUES (14140, '2020-2021-1', '20', '20221201', '221020', '22010', '10108', '12');
INSERT INTO `schedule` VALUES (14141, '2020-2021-1', '20', '20221201', '221020', '22010', '20106', '11');
INSERT INTO `schedule` VALUES (14142, '2020-2021-1', '20', '20111701', '171001', '17001', '20202', '04');
INSERT INTO `schedule` VALUES (14143, '2020-2021-1', '20', '20221101', '181003', '18001', '10203', '07');
INSERT INTO `schedule` VALUES (14144, '2020-2021-1', '20', '20221101', '181003', '18001', '30201', '18');
INSERT INTO `schedule` VALUES (14145, '2020-2021-1', '20', '20221101', '161001', '16002', '20201', '16');
INSERT INTO `schedule` VALUES (14146, '2020-2021-1', '20', '20221201', '194001', '19001', '90802', '09');
INSERT INTO `schedule` VALUES (14147, '2020-2021-1', '20', '20221201', '181002', '18002', '10204', '06');
INSERT INTO `schedule` VALUES (14148, '2020-2021-1', '20', '20221201', '181002', '18002', '10202', '18');
INSERT INTO `schedule` VALUES (14149, '2020-2021-1', '20', '20221201', '221001', '22002', '10106', '08');
INSERT INTO `schedule` VALUES (14150, '2020-2021-1', '20', '20221201', '221001', '22002', '20105', '13');
INSERT INTO `schedule` VALUES (14151, '2020-2021-1', '20', '20221201', '223001', '22002', '40304', '20');
INSERT INTO `schedule` VALUES (14152, '2020-2021-1', '20', '20221301', '181003', '18001', '10203', '17');
INSERT INTO `schedule` VALUES (14153, '2020-2021-1', '20', '20221301', '181003', '18001', '10204', '09');
INSERT INTO `schedule` VALUES (14154, '2020-2021-1', '20', '20221301', '221011', '22011', '10103', '16');
INSERT INTO `schedule` VALUES (14155, '2020-2021-1', '20', '20221301', '221011', '22011', '10107', '03');
INSERT INTO `schedule` VALUES (14156, '2020-2021-1', '20', '20221301', '221010', '22010', '10102', '02');
INSERT INTO `schedule` VALUES (14157, '2020-2021-1', '20', '20221301', '221010', '22010', '20106', '15');
INSERT INTO `schedule` VALUES (14158, '2020-2021-1', '20', '20221301', '221012', '22012', '20101', '24');
INSERT INTO `schedule` VALUES (14159, '2020-2021-1', '20', '20221301', '194003', '19004', '70901', '12');
INSERT INTO `schedule` VALUES (14160, '2020-2021-1', '22', '22132001', '194003', '19004', '70902', '17');
INSERT INTO `schedule` VALUES (14161, '2020-2021-1', '22', '22132001', '171001', '17001', '20201', '03');
INSERT INTO `schedule` VALUES (14162, '2020-2021-1', '22', '22132001', '181002', '18002', '20201', '25');
INSERT INTO `schedule` VALUES (14163, '2020-2021-1', '22', '22132001', '181002', '18002', '10202', '15');
INSERT INTO `schedule` VALUES (14164, '2020-2021-1', '22', '22132001', '111001', '11001', '10105', '14');
INSERT INTO `schedule` VALUES (14165, '2020-2021-1', '22', '22132001', '111001', '11001', '20103', '04');
INSERT INTO `schedule` VALUES (14166, '2020-2021-1', '22', '22132001', '111002', '11002', '10203', '11');
INSERT INTO `schedule` VALUES (14167, '2020-2021-1', '22', '22132001', '111002', '11002', '20201', '22');
INSERT INTO `schedule` VALUES (14168, '2020-2021-1', '22', '22132001', '131001', '13001', '20101', '09');
INSERT INTO `schedule` VALUES (14169, '2020-2021-1', '22', '22132001', '221018', '22014', '10203', '20');
INSERT INTO `schedule` VALUES (14170, '2020-2021-1', '22', '22132001', '111004', '11005', '10107', '16');
INSERT INTO `schedule` VALUES (14171, '2020-2021-1', '22', '22132001', '111004', '11005', '20104', '02');
INSERT INTO `schedule` VALUES (14172, '2020-2021-1', '20', '20221301', '161001', '16002', '20201', '19');
INSERT INTO `schedule` VALUES (14173, '2020-2021-1', '20', '20221401', '171002', '17002', '10203', '22');
INSERT INTO `schedule` VALUES (14174, '2020-2021-1', '20', '20221401', '181005', '18004', '20201', '14');
INSERT INTO `schedule` VALUES (14175, '2020-2021-1', '20', '20221401', '181006', '18005', '30201', '12');
INSERT INTO `schedule` VALUES (14176, '2020-2021-1', '20', '20221401', '221014', '22014', '10101', '21');
INSERT INTO `schedule` VALUES (14177, '2020-2021-1', '20', '20221401', '221014', '22014', '20104', '11');
INSERT INTO `schedule` VALUES (14178, '2020-2021-1', '20', '20221401', '221015', '22015', '10103', '01');
INSERT INTO `schedule` VALUES (14179, '2020-2021-1', '20', '20221401', '221015', '22015', '10104', '18');
INSERT INTO `schedule` VALUES (14180, '2020-2021-1', '20', '20221401', '221016', '22017', '20106', '16');
INSERT INTO `schedule` VALUES (14181, '2020-2021-1', '20', '20221401', '221016', '22017', '20104', '08');
INSERT INTO `schedule` VALUES (14182, '2020-2021-1', '20', '20221401', '221017', '22016', '20106', '06');
INSERT INTO `schedule` VALUES (14183, '2020-2021-1', '20', '20221401', '241001', '24001', '20202', '23');
INSERT INTO `schedule` VALUES (14184, '2020-2021-1', '20', '20243001', '181004', '18004', '10202', '13');
INSERT INTO `schedule` VALUES (14185, '2020-2021-1', '20', '20243001', '181004', '18004', '10204', '04');
INSERT INTO `schedule` VALUES (14186, '2020-2021-1', '20', '20243001', '241001', '24001', '20201', '02');
INSERT INTO `schedule` VALUES (14187, '2020-2021-1', '20', '20243001', '241004', '24004', '20105', '20');
INSERT INTO `schedule` VALUES (14188, '2020-2021-1', '20', '20243001', '241004', '24004', '10106', '01');
INSERT INTO `schedule` VALUES (14189, '2020-2021-1', '20', '20243001', '241005', '24005', '30201', '19');
INSERT INTO `schedule` VALUES (14190, '2020-2021-1', '20', '20243001', '241005', '24005', '30201', '17');
INSERT INTO `schedule` VALUES (14191, '2020-2021-1', '20', '20243001', '241006', '24006', '10104', '15');
INSERT INTO `schedule` VALUES (14192, '2020-2021-1', '20', '20243001', '241006', '24006', '10102', '09');
INSERT INTO `schedule` VALUES (14193, '2020-2021-1', '20', '20243001', '221018', '22014', '10202', '03');
INSERT INTO `schedule` VALUES (14194, '2020-2021-1', '20', '20243001', '194002', '19002', '70902', '12');
INSERT INTO `schedule` VALUES (14195, '2020-2021-1', '20', '20243001', '172001', '17003', '10203', '23');
INSERT INTO `schedule` VALUES (14196, '2020-2021-1', '20', '20162701', '171002', '17002', '10203', '01');
INSERT INTO `schedule` VALUES (14197, '2020-2021-1', '20', '20162701', '181002', '18002', '10204', '07');
INSERT INTO `schedule` VALUES (14198, '2020-2021-1', '20', '20162701', '181002', '18002', '10201', '16');
INSERT INTO `schedule` VALUES (14199, '2020-2021-1', '20', '20162701', '251001', '25001', '60702', '20');
INSERT INTO `schedule` VALUES (14200, '2020-2021-1', '20', '20162701', '161003', '16004', '20103', '17');
INSERT INTO `schedule` VALUES (14201, '2020-2021-1', '20', '20162701', '161003', '16004', '20106', '08');
INSERT INTO `schedule` VALUES (14202, '2020-2021-1', '20', '20162701', '161004', '16003', '20105', '12');
INSERT INTO `schedule` VALUES (14203, '2020-2021-1', '20', '20162701', '161004', '16003', '20102', '22');
INSERT INTO `schedule` VALUES (14204, '2020-2021-1', '20', '20162701', '161005', '16002', '10107', '15');
INSERT INTO `schedule` VALUES (14205, '2020-2021-1', '20', '20162701', '161005', '16002', '20102', '14');
INSERT INTO `schedule` VALUES (14206, '2020-2021-1', '20', '20162701', '221018', '22014', '10204', '18');
INSERT INTO `schedule` VALUES (14207, '2020-2021-1', '20', '20162701', '194003', '19004', '70902', '19');
INSERT INTO `schedule` VALUES (14208, '2020-2021-1', '20', '20221101', '221005', '22004', '20105', '11');
INSERT INTO `schedule` VALUES (14209, '2020-2021-1', '20', '20221101', '223005', '22004', '40301', '09');
INSERT INTO `schedule` VALUES (14210, '2020-2021-1', '20', '20221101', '221020', '22010', '10109', '14');
INSERT INTO `schedule` VALUES (14211, '2020-2021-1', '20', '20221101', '221020', '22010', '10101', '17');
INSERT INTO `schedule` VALUES (14212, '2020-2021-1', '20', '20221201', '221022', '22002', '10107', '01');
INSERT INTO `schedule` VALUES (14213, '2020-2021-1', '20', '20221201', '221023', '22002', '30301', '25');
INSERT INTO `schedule` VALUES (14214, '2020-2021-1', '20', '20221201', '221017', '22016', '10103', '04');
INSERT INTO `schedule` VALUES (14215, '2020-2021-1', '20', '20221201', '181005', '18004', '10204', '21');
INSERT INTO `schedule` VALUES (14216, '2020-2021-1', '20', '20221301', '221004', '22006', '20105', '14');
INSERT INTO `schedule` VALUES (14217, '2020-2021-1', '20', '20221301', '223004', '22006', '40302', '21');
INSERT INTO `schedule` VALUES (14218, '2020-2021-1', '20', '20221301', '171002', '17002', '10201', '04');
INSERT INTO `schedule` VALUES (14219, '2020-2021-1', '20', '20111701', '181004', '18004', '10201', '09');
INSERT INTO `schedule` VALUES (14220, '2020-2021-1', '20', '20111701', '111001', '11001', '10108', '15');
INSERT INTO `schedule` VALUES (14221, '2020-2021-1', '20', '20111701', '111002', '11002', '10202', '12');
INSERT INTO `schedule` VALUES (14222, '2020-2021-1', '20', '20111701', '111002', '11002', '10203', '24');
INSERT INTO `schedule` VALUES (14223, '2020-2021-1', '20', '20111701', '111003', '11004', '10106', '21');
INSERT INTO `schedule` VALUES (14224, '2020-2021-1', '20', '20111701', '111003', '11004', '10104', '13');
INSERT INTO `schedule` VALUES (14225, '2020-2021-1', '20', '20111701', '221023', '22002', '40304', '23');
INSERT INTO `schedule` VALUES (14226, '2020-2021-1', '20', '20111701', '251002', '25002', '60702', '17');

-- ----------------------------
-- Table structure for student
-- ----------------------------
DROP TABLE IF EXISTS `student`;
CREATE TABLE `student`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'id',
  `grade_no` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '年级编号',
  `college_no` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '学院编号',
  `class_no` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '班级编号',
  `student_no` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '学号',
  `student_name` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '姓名',
  `telephone` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '电话',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `class_no`(`class_no` ASC) USING BTREE,
  CONSTRAINT `student_ibfk_1` FOREIGN KEY (`class_no`) REFERENCES `class_info` (`class_no`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 329 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of student
-- ----------------------------
INSERT INTO `student` VALUES (176, '20', '22', '20221101', '2022110101', '李明', '123');
INSERT INTO `student` VALUES (177, '20', '22', '20221101', '2022110102', '王华', '181');
INSERT INTO `student` VALUES (201, '21', '22', '21221101', '2122110101', '张三', NULL);
INSERT INTO `student` VALUES (204, '20', '11', '20111701', '2011170101', '王翔', NULL);
INSERT INTO `student` VALUES (205, '20', '11', '20111701', '2011170102', '李文', NULL);
INSERT INTO `student` VALUES (206, '20', '11', '20111701', '2011170103', '张明', NULL);
INSERT INTO `student` VALUES (207, '20', '11', '20111701', '2011170104', '刘辉', NULL);
INSERT INTO `student` VALUES (208, '20', '11', '20111701', '2011170105', '陈芳', NULL);
INSERT INTO `student` VALUES (209, '20', '11', '20111701', '2011170106', '杨婷', NULL);
INSERT INTO `student` VALUES (210, '20', '11', '20111701', '2011170107', '赵强', NULL);
INSERT INTO `student` VALUES (211, '20', '11', '20111701', '2011170108', '黄伟', NULL);
INSERT INTO `student` VALUES (212, '20', '11', '20111701', '2011170109', '周霞', NULL);
INSERT INTO `student` VALUES (213, '20', '11', '20111701', '2011170110', '吴秀华', NULL);
INSERT INTO `student` VALUES (214, '20', '11', '20111701', '2011170111', '徐梅', NULL);
INSERT INTO `student` VALUES (215, '20', '11', '20111701', '2011170112', '孙华娜', NULL);
INSERT INTO `student` VALUES (216, '20', '22', '20221101', '2022110103', '李文博', NULL);
INSERT INTO `student` VALUES (217, '20', '22', '20221101', '2022110104', '王诗涵', NULL);
INSERT INTO `student` VALUES (218, '20', '22', '20221101', '2022110105', '张宇辰', NULL);
INSERT INTO `student` VALUES (219, '20', '22', '20221101', '2022110106', '陈晓彤', NULL);
INSERT INTO `student` VALUES (220, '20', '11', '20111701', '2011170113', '李佳怡', NULL);
INSERT INTO `student` VALUES (221, '20', '11', '20111701', '2011170114', '王子轩', NULL);
INSERT INTO `student` VALUES (222, '20', '11', '20111701', '2011170115', '张梦婷', NULL);
INSERT INTO `student` VALUES (223, '20', '11', '20111701', '2011170116', '刘志强', NULL);
INSERT INTO `student` VALUES (224, '20', '11', '20111701', '2011170117', '陈思涵', NULL);
INSERT INTO `student` VALUES (225, '20', '11', '20111701', '2011170118', '赵雅静', NULL);
INSERT INTO `student` VALUES (226, '20', '11', '20111701', '2011170119', '杨晨曦', NULL);
INSERT INTO `student` VALUES (227, '20', '11', '20111701', '2011170120', '黄子杰', NULL);
INSERT INTO `student` VALUES (228, '20', '11', '20111701', '2011170121', '吴欣怡', NULL);
INSERT INTO `student` VALUES (229, '20', '11', '20111701', '2011170122', '周浩宇', NULL);
INSERT INTO `student` VALUES (230, '20', '11', '20111701', '2011170123', '徐嘉宁', NULL);
INSERT INTO `student` VALUES (231, '20', '11', '20111701', '2011170124', '孙子涵', NULL);
INSERT INTO `student` VALUES (232, '20', '11', '20111701', '2011170125', '马瑞琪', NULL);
INSERT INTO `student` VALUES (233, '20', '11', '20111701', '2011170126', '朱文静', NULL);
INSERT INTO `student` VALUES (234, '20', '11', '20111701', '2011170127', '胡志远', NULL);
INSERT INTO `student` VALUES (235, '20', '11', '20111701', '2011170128', '郭晓彤', NULL);
INSERT INTO `student` VALUES (236, '20', '11', '20111701', '2011170129', '林雨欣', NULL);
INSERT INTO `student` VALUES (237, '20', '11', '20111701', '2011170130', '何俊杰', NULL);
INSERT INTO `student` VALUES (238, '20', '11', '20111701', '2011170131', '高雪婷', NULL);
INSERT INTO `student` VALUES (239, '20', '11', '20111701', '2011170132', '罗志豪', NULL);
INSERT INTO `student` VALUES (240, '20', '11', '20111701', '2011170133', '梁文婷', NULL);
INSERT INTO `student` VALUES (241, '20', '11', '20111701', '2011170134', '宋子豪', NULL);
INSERT INTO `student` VALUES (242, '20', '11', '20111701', '2011170135', '郑嘉琪', NULL);
INSERT INTO `student` VALUES (243, '20', '11', '20111701', '2011170136', '谢思源', NULL);
INSERT INTO `student` VALUES (244, '20', '11', '20111701', '2011170137', '唐欣怡', NULL);
INSERT INTO `student` VALUES (245, '20', '11', '20111701', '2011170138', '韩志诚', NULL);
INSERT INTO `student` VALUES (246, '20', '11', '20111701', '2011170139', '曹梦瑶', NULL);
INSERT INTO `student` VALUES (247, '20', '11', '20111701', '2011170140', '冯子轩', NULL);
INSERT INTO `student` VALUES (248, '20', '11', '20111701', '2011170141', '薛雅楠', NULL);
INSERT INTO `student` VALUES (249, '20', '11', '20111701', '2011170142', '邓晨曦', NULL);
INSERT INTO `student` VALUES (310, '20', '22', '20221101', '2022110107', '沈佳琪', NULL);
INSERT INTO `student` VALUES (311, '20', '22', '20221101', '2022110108', '钱子涵', NULL);
INSERT INTO `student` VALUES (312, '20', '22', '20221101', '2022110109', '秦梦瑶', NULL);
INSERT INTO `student` VALUES (313, '20', '22', '20221101', '2022110110', '彭俊杰', NULL);
INSERT INTO `student` VALUES (314, '20', '22', '20221101', '2022110111', '赖雅静', NULL);
INSERT INTO `student` VALUES (315, '20', '22', '20221101', '2022110112', '谭志豪', NULL);
INSERT INTO `student` VALUES (316, '20', '22', '20221101', '2022110113', '丁思涵', NULL);
INSERT INTO `student` VALUES (317, '20', '22', '20221101', '2022110114', '段晨曦', NULL);
INSERT INTO `student` VALUES (318, '20', '22', '20221101', '2022110115', '程文博', NULL);
INSERT INTO `student` VALUES (319, '20', '22', '20221101', '2022110116', '蔡子轩', NULL);
INSERT INTO `student` VALUES (320, '20', '22', '20221101', '2022110117', '廖嘉宁', NULL);
INSERT INTO `student` VALUES (321, '20', '22', '20221101', '2022110118', '魏欣怡', NULL);
INSERT INTO `student` VALUES (322, '20', '22', '20221101', '2022110119', '薛浩宇', NULL);
INSERT INTO `student` VALUES (323, '20', '22', '20221101', '2022110120', '尹雪婷', NULL);
INSERT INTO `student` VALUES (324, '20', '22', '20221101', '2022110121', '白志远', NULL);
INSERT INTO `student` VALUES (325, '20', '22', '20221101', '2022110122', '孟瑞琪', NULL);
INSERT INTO `student` VALUES (326, '20', '22', '20221101', '2022110123', '武晓彤', NULL);
INSERT INTO `student` VALUES (327, '20', '22', '20221101', '2022110124', '康子杰', NULL);
INSERT INTO `student` VALUES (328, '20', '22', '20221101', '2022110125', '章文静', NULL);
INSERT INTO `student` VALUES (329, '20', '22', '20221101', '2022110126', '莫俊杰', NULL);

-- ----------------------------
-- Table structure for sys_dict
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict`;
CREATE TABLE `sys_dict`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '名称',
  `value` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '值',
  `type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '类型',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `value`(`value` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 138 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_dict
-- ----------------------------
INSERT INTO `sys_dict` VALUES (1, 'user', 'el-icon-user', 'icon');
INSERT INTO `sys_dict` VALUES (2, 'house', 'el-icon-house', 'icon');
INSERT INTO `sys_dict` VALUES (3, 'menu', 'el-icon-menu', 'icon');
INSERT INTO `sys_dict` VALUES (4, 'custom', 'el-icon-s-custom', 'icon');
INSERT INTO `sys_dict` VALUES (5, 'grid', 'el-icon-s-grid', 'icon');
INSERT INTO `sys_dict` VALUES (6, 'document', 'el-icon-document', 'icon');
INSERT INTO `sys_dict` VALUES (7, 'setting', 'el-icon-setting', 'icon');
INSERT INTO `sys_dict` VALUES (8, 'data', 'el-icon-data-board', 'icon');
INSERT INTO `sys_dict` VALUES (9, 'notebook', 'el-icon-notebook-1', 'icon');
INSERT INTO `sys_dict` VALUES (10, 'building', 'el-icon-office-building', 'icon');
INSERT INTO `sys_dict` VALUES (11, '2020-2021-1', '2020-2021-1', 'term');
INSERT INTO `sys_dict` VALUES (12, '2020-2021-2', '2020-2021-2', 'term');
INSERT INTO `sys_dict` VALUES (13, '2021-2022-1', '2021-2022-1', 'term');
INSERT INTO `sys_dict` VALUES (14, '2021-2022-2', '2021-2022-2', 'term');
INSERT INTO `sys_dict` VALUES (15, '2022-2023-1', '2022-2023-1', 'term');
INSERT INTO `sys_dict` VALUES (16, '2022-2023-2', '2022-2023-2', 'term');
INSERT INTO `sys_dict` VALUES (17, '2023-2024-1', '2023-2024-1', 'term');
INSERT INTO `sys_dict` VALUES (18, '2023-2024-2', '2023-2024-2', 'term');
INSERT INTO `sys_dict` VALUES (19, '2020级', '20', 'grade');
INSERT INTO `sys_dict` VALUES (20, '2021级', '21', 'grade');
INSERT INTO `sys_dict` VALUES (21, '2022级', '22', 'grade');
INSERT INTO `sys_dict` VALUES (22, '2023级', '23', 'grade');
INSERT INTO `sys_dict` VALUES (25, '是', '1', 'isFix');
INSERT INTO `sys_dict` VALUES (26, '否', '0', 'isFix');
INSERT INTO `sys_dict` VALUES (31, '教授', '1', 'profession');
INSERT INTO `sys_dict` VALUES (32, '副教授', '2', 'profession');
INSERT INTO `sys_dict` VALUES (33, '讲师', '3', 'profession');
INSERT INTO `sys_dict` VALUES (34, '助教', '4', 'profession');
INSERT INTO `sys_dict` VALUES (35, '超级管理员', 'ROLE_ADMINISTRATOR', 'role');
INSERT INTO `sys_dict` VALUES (36, '管理员', 'ROLE_ADMIN', 'role');
INSERT INTO `sys_dict` VALUES (37, '教师', 'ROLE_TEACHER', 'role');
INSERT INTO `sys_dict` VALUES (38, '学生', 'ROLE_STUDENT', 'role');
INSERT INTO `sys_dict` VALUES (39, '基础医学院', '11', 'college');
INSERT INTO `sys_dict` VALUES (40, '公共卫生学院', '12', 'college');
INSERT INTO `sys_dict` VALUES (41, '护理学院', '13', 'college');
INSERT INTO `sys_dict` VALUES (42, '药学院', '14', 'college');
INSERT INTO `sys_dict` VALUES (43, '农学与生物学院', '15', 'college');
INSERT INTO `sys_dict` VALUES (44, '文学院', '16', 'college');
INSERT INTO `sys_dict` VALUES (45, '外国语学院', '17', 'college');
INSERT INTO `sys_dict` VALUES (46, '马克思主义学院', '18', 'college');
INSERT INTO `sys_dict` VALUES (47, '体育科学学院', '19', 'college');
INSERT INTO `sys_dict` VALUES (48, '法学院', '20', 'college');
INSERT INTO `sys_dict` VALUES (49, '经济与管理学院', '21', 'college');
INSERT INTO `sys_dict` VALUES (50, '数学与计算机学院', '22', 'college');
INSERT INTO `sys_dict` VALUES (51, '工程学院', '23', 'college');
INSERT INTO `sys_dict` VALUES (52, '教师教育学院', '24', 'college');
INSERT INTO `sys_dict` VALUES (53, '艺术学院', '25', 'college');
INSERT INTO `sys_dict` VALUES (60, '第一教学楼', '1', 'teachingBuilding');
INSERT INTO `sys_dict` VALUES (61, '第二教学楼', '2', 'teachingBuilding');
INSERT INTO `sys_dict` VALUES (62, '第三教学楼', '3', 'teachingBuilding');
INSERT INTO `sys_dict` VALUES (63, '工科楼', '4', 'teachingBuilding');
INSERT INTO `sys_dict` VALUES (64, '理科楼', '5', 'teachingBuilding');
INSERT INTO `sys_dict` VALUES (65, '艺术楼', '6', 'teachingBuilding');
INSERT INTO `sys_dict` VALUES (66, '体育馆', '7', 'teachingBuilding');
INSERT INTO `sys_dict` VALUES (67, '南操场', '8', 'teachingBuilding');
INSERT INTO `sys_dict` VALUES (68, '北操场', '9', 'teachingBuilding');
INSERT INTO `sys_dict` VALUES (71, '必修', '1', 'courseAttribute');
INSERT INTO `sys_dict` VALUES (72, '选修', '2', 'courseAttribute');
INSERT INTO `sys_dict` VALUES (73, '实验课', '3', 'courseAttribute');
INSERT INTO `sys_dict` VALUES (74, '体育课', '4', 'courseAttribute');
INSERT INTO `sys_dict` VALUES (76, '小教室', '01', 'classroom');
INSERT INTO `sys_dict` VALUES (77, '大教室', '02', 'classroom');
INSERT INTO `sys_dict` VALUES (78, '计算机机房', '03', 'classroom');
INSERT INTO `sys_dict` VALUES (79, '生物实验室', '04', 'classroom');
INSERT INTO `sys_dict` VALUES (80, '化学实验室', '05', 'classroom');
INSERT INTO `sys_dict` VALUES (81, '舞蹈教室', '06', 'classroom');
INSERT INTO `sys_dict` VALUES (82, '音乐教室', '07', 'classroom');
INSERT INTO `sys_dict` VALUES (83, '体育室外', '08', 'classroom');
INSERT INTO `sys_dict` VALUES (84, '体育室内', '09', 'classroom');
INSERT INTO `sys_dict` VALUES (85, '琴房', '10', 'classroom');
INSERT INTO `sys_dict` VALUES (86, '校外实践', '11', 'classroom');
INSERT INTO `sys_dict` VALUES (91, '计算机科学与技术', '11', 'major');
INSERT INTO `sys_dict` VALUES (92, '信息安全', '12', 'major');
INSERT INTO `sys_dict` VALUES (93, '通信工程', '13', 'major');
INSERT INTO `sys_dict` VALUES (94, '数学与应用数学', '14', 'major');
INSERT INTO `sys_dict` VALUES (95, '电气工程及其自动化', '15', 'major');
INSERT INTO `sys_dict` VALUES (96, '建筑学', '16', 'major');
INSERT INTO `sys_dict` VALUES (97, '临床医学', '17', 'major');
INSERT INTO `sys_dict` VALUES (98, '口腔医学', '18', 'major');
INSERT INTO `sys_dict` VALUES (99, '医学影像学', '19', 'major');
INSERT INTO `sys_dict` VALUES (100, '护理学', '20', 'major');
INSERT INTO `sys_dict` VALUES (101, '药学', '21', 'major');
INSERT INTO `sys_dict` VALUES (102, '预防医学', '22', 'major');
INSERT INTO `sys_dict` VALUES (103, '法学', '23', 'major');
INSERT INTO `sys_dict` VALUES (104, '思想政治教育', '24', 'major');
INSERT INTO `sys_dict` VALUES (105, '财务管理', '25', 'major');
INSERT INTO `sys_dict` VALUES (106, '生物科学', '26', 'major');
INSERT INTO `sys_dict` VALUES (107, '汉语言文学', '27', 'major');
INSERT INTO `sys_dict` VALUES (108, '音乐学', '28', 'major');
INSERT INTO `sys_dict` VALUES (109, '英语', '29', 'major');
INSERT INTO `sys_dict` VALUES (110, '学前教育', '30', 'major');
INSERT INTO `sys_dict` VALUES (120, '所有人', '1', 'scope');
INSERT INTO `sys_dict` VALUES (121, '管理员', '0', 'scope');
INSERT INTO `sys_dict` VALUES (131, 'operation', 'el-icon-s-operation', 'icon');
INSERT INTO `sys_dict` VALUES (132, 'files', 'el-icon-files', 'icon');
INSERT INTO `sys_dict` VALUES (133, 'claim', 'el-icon-s-claim', 'icon');
INSERT INTO `sys_dict` VALUES (134, 'message', 'el-icon-message', 'icon');
INSERT INTO `sys_dict` VALUES (135, 'reading', 'el-icon-reading', 'icon');
INSERT INTO `sys_dict` VALUES (136, 'monitor', 'el-icon-monitor', 'icon');
INSERT INTO `sys_dict` VALUES (137, 'crop', 'el-icon-crop\r\n', 'icon');
INSERT INTO `sys_dict` VALUES (138, 'edit', 'el-icon-edit', 'icon');

-- ----------------------------
-- Table structure for sys_file
-- ----------------------------
DROP TABLE IF EXISTS `sys_file`;
CREATE TABLE `sys_file`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'id',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '文件名',
  `type` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '类型',
  `size` bigint NULL DEFAULT NULL COMMENT '大小',
  `url` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '文件位置',
  `is_delete` tinyint(1) NULL DEFAULT 0 COMMENT '是否删除',
  `md5` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'md5',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 78 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_file
-- ----------------------------

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'id',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '名称',
  `path` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '路径',
  `icon` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '图标',
  `description` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '描述',
  `pid` int NULL DEFAULT NULL COMMENT '父级id',
  `sort_num` int NULL DEFAULT NULL COMMENT '顺序',
  `page_path` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '页面路径',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES (1, '数据概览', '/systemData', 'el-icon-data-board', '', NULL, 2, 'SystemData');
INSERT INTO `sys_menu` VALUES (3, '信息管理', '', 'el-icon-s-operation', NULL, NULL, 4, NULL);
INSERT INTO `sys_menu` VALUES (4, '菜单管理', '/menu', 'el-icon-menu', '', 10, 1, 'Menu');
INSERT INTO `sys_menu` VALUES (5, '学生管理', '/student', 'el-icon-user', '', 3, 1, 'Student');
INSERT INTO `sys_menu` VALUES (8, '教师管理', '/teacher', 'el-icon-s-custom', NULL, 3, 2, 'Teacher');
INSERT INTO `sys_menu` VALUES (9, '角色管理', '/role', 'el-icon-crop\r\n', NULL, 10, 2, 'Role');
INSERT INTO `sys_menu` VALUES (10, '系统管理', NULL, 'el-icon-setting', NULL, NULL, 6, NULL);
INSERT INTO `sys_menu` VALUES (11, '教学管理', NULL, 'el-icon-monitor', NULL, NULL, 5, NULL);
INSERT INTO `sys_menu` VALUES (12, '班级管理', '/classInfo', 'el-icon-s-claim', NULL, 11, 1, 'ClassInfo');
INSERT INTO `sys_menu` VALUES (13, '教室管理', '/classroom', 'el-icon-office-building', NULL, 11, 2, 'Classroom');
INSERT INTO `sys_menu` VALUES (14, '课程计划', '/coursePlan', 'el-icon-s-grid', NULL, 11, 4, 'CoursePlan');
INSERT INTO `sys_menu` VALUES (16, '字典管理', '/dict', 'el-icon-notebook-1', NULL, 10, 3, 'Dict');
INSERT INTO `sys_menu` VALUES (23, '通知公告', '/notification', 'el-icon-message', NULL, 11, 5, 'Notification');
INSERT INTO `sys_menu` VALUES (24, '通知公告', '/Home', 'el-icon-house', NULL, NULL, 1, 'Home');
INSERT INTO `sys_menu` VALUES (25, '课表查询', '/schedule', 'el-icon-files', NULL, NULL, 3, 'Schedule');
INSERT INTO `sys_menu` VALUES (26, '课程管理', '/courseInfo', 'el-icon-reading', NULL, 11, 3, 'CourseInfo');
INSERT INTO `sys_menu` VALUES (28, '用户管理', '/user', 'el-icon-edit', NULL, 10, 4, 'User');
INSERT INTO `sys_menu` VALUES (29, '学生名单查询', '/studentInfo', 'el-icon-document', NULL, NULL, 7, 'StudentInfo');

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'id',
  `name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '名称',
  `flag` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '唯一标识',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES (1, '管理员', 'ROLE_ADMIN');
INSERT INTO `sys_role` VALUES (2, '教师', 'ROLE_TEACHER');
INSERT INTO `sys_role` VALUES (3, '学生', 'ROLE_STUDENT');
INSERT INTO `sys_role` VALUES (4, '超级管理员', 'ROLE_ADMINISTRATOR');

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu`  (
  `role_id` int NOT NULL COMMENT '角色id',
  `menu_id` int NOT NULL COMMENT '菜单id',
  PRIMARY KEY (`role_id`, `menu_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
INSERT INTO `sys_role_menu` VALUES (1, 3);
INSERT INTO `sys_role_menu` VALUES (1, 5);
INSERT INTO `sys_role_menu` VALUES (1, 8);
INSERT INTO `sys_role_menu` VALUES (1, 11);
INSERT INTO `sys_role_menu` VALUES (1, 12);
INSERT INTO `sys_role_menu` VALUES (1, 14);
INSERT INTO `sys_role_menu` VALUES (1, 23);
INSERT INTO `sys_role_menu` VALUES (1, 24);
INSERT INTO `sys_role_menu` VALUES (1, 25);
INSERT INTO `sys_role_menu` VALUES (1, 26);
INSERT INTO `sys_role_menu` VALUES (2, 24);
INSERT INTO `sys_role_menu` VALUES (2, 25);
INSERT INTO `sys_role_menu` VALUES (2, 29);
INSERT INTO `sys_role_menu` VALUES (3, 24);
INSERT INTO `sys_role_menu` VALUES (3, 25);
INSERT INTO `sys_role_menu` VALUES (4, 1);
INSERT INTO `sys_role_menu` VALUES (4, 3);
INSERT INTO `sys_role_menu` VALUES (4, 4);
INSERT INTO `sys_role_menu` VALUES (4, 5);
INSERT INTO `sys_role_menu` VALUES (4, 8);
INSERT INTO `sys_role_menu` VALUES (4, 9);
INSERT INTO `sys_role_menu` VALUES (4, 10);
INSERT INTO `sys_role_menu` VALUES (4, 11);
INSERT INTO `sys_role_menu` VALUES (4, 12);
INSERT INTO `sys_role_menu` VALUES (4, 13);
INSERT INTO `sys_role_menu` VALUES (4, 14);
INSERT INTO `sys_role_menu` VALUES (4, 15);
INSERT INTO `sys_role_menu` VALUES (4, 16);
INSERT INTO `sys_role_menu` VALUES (4, 23);
INSERT INTO `sys_role_menu` VALUES (4, 24);
INSERT INTO `sys_role_menu` VALUES (4, 25);
INSERT INTO `sys_role_menu` VALUES (4, 26);
INSERT INTO `sys_role_menu` VALUES (4, 28);

-- ----------------------------
-- Table structure for teacher
-- ----------------------------
DROP TABLE IF EXISTS `teacher`;
CREATE TABLE `teacher`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'id',
  `college_no` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '学院编号',
  `teacher_no` varchar(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '工号',
  `teacher_name` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '姓名',
  `profession` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '职称 ',
  `telephone` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '电话',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `teacher_no`(`teacher_no` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 71 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of teacher
-- ----------------------------
INSERT INTO `teacher` VALUES (18, '22', '22001', '程敏', '1', '');
INSERT INTO `teacher` VALUES (19, '22', '22002', '赵军强', '1', '');
INSERT INTO `teacher` VALUES (20, '22', '22003', '周杰伦', '1', '');
INSERT INTO `teacher` VALUES (21, '22', '22004', '赵洋', '2', '');
INSERT INTO `teacher` VALUES (22, '22', '22005', '梁军', '3', '');
INSERT INTO `teacher` VALUES (23, '22', '22006', '王明杰', '4', '');
INSERT INTO `teacher` VALUES (24, '11', '11001', '黄静', '1', '');
INSERT INTO `teacher` VALUES (25, '24', '24001', '李霞', '1', '');
INSERT INTO `teacher` VALUES (26, '19', '19001', '周明辉', '1', '');
INSERT INTO `teacher` VALUES (27, '19', '19002', '陈思宇', '2', '');
INSERT INTO `teacher` VALUES (28, '17', '17001', '李梦洁', '1', '');
INSERT INTO `teacher` VALUES (29, '17', '17002', '王雨萌', '2', '');
INSERT INTO `teacher` VALUES (30, '17', '17003', '杨佳琪', '2', '');
INSERT INTO `teacher` VALUES (31, '16', '16001', '王梅', '1', '');
INSERT INTO `teacher` VALUES (32, '18', '18001', '李雨萱', '1', NULL);
INSERT INTO `teacher` VALUES (33, '18', '18002', '张宇轩', '1', NULL);
INSERT INTO `teacher` VALUES (34, '18', '18003', '王梓涵', '2', NULL);
INSERT INTO `teacher` VALUES (35, '18', '18004', '陈思源', '2', NULL);
INSERT INTO `teacher` VALUES (36, '18', '18005', '刘晨露', '3', NULL);
INSERT INTO `teacher` VALUES (37, '16', '16002', '赵雅琪', '1', NULL);
INSERT INTO `teacher` VALUES (38, '22', '22007', '刘天宇', '3', NULL);
INSERT INTO `teacher` VALUES (39, '25', '25001', '张晨轩', '1', NULL);
INSERT INTO `teacher` VALUES (40, '25', '25002', '张宇', '1', NULL);
INSERT INTO `teacher` VALUES (41, '13', '13001', '张雨薇', '1', NULL);
INSERT INTO `teacher` VALUES (42, '13', '13002', '赵婉婷', '1', NULL);
INSERT INTO `teacher` VALUES (43, '13', '13003', '朱文杰', '2', NULL);
INSERT INTO `teacher` VALUES (44, '24', '24002', '吴美玲', '1', NULL);
INSERT INTO `teacher` VALUES (45, '16', '16003', '周建国', '2', NULL);
INSERT INTO `teacher` VALUES (46, '16', '16004', '曹雨欣', '2', NULL);
INSERT INTO `teacher` VALUES (47, '16', '16005', '许佳琳', '3', NULL);
INSERT INTO `teacher` VALUES (48, '24', '24003', '刘建军', '2', NULL);
INSERT INTO `teacher` VALUES (49, '24', '24004', '蒋雨欣', '3', NULL);
INSERT INTO `teacher` VALUES (50, '24', '24005', '刘美玲', '2', NULL);
INSERT INTO `teacher` VALUES (51, '24', '24006', '卢怡婷', '2', NULL);
INSERT INTO `teacher` VALUES (52, '24', '24007', '崔雨萱', '3', NULL);
INSERT INTO `teacher` VALUES (53, '11', '11002', '施美玲', '1', NULL);
INSERT INTO `teacher` VALUES (54, '11', '11003', '张佳', '2', NULL);
INSERT INTO `teacher` VALUES (55, '11', '11004', '刘伟', '3', NULL);
INSERT INTO `teacher` VALUES (56, '11', '11005', '王文杰', '2', NULL);
INSERT INTO `teacher` VALUES (57, '11', '11006', '华文杰', '2', NULL);
INSERT INTO `teacher` VALUES (58, '11', '11007', '叶杰', '3', NULL);
INSERT INTO `teacher` VALUES (59, '22', '22008', '方思', '1', NULL);
INSERT INTO `teacher` VALUES (60, '22', '22009', '江奕云', '2', NULL);
INSERT INTO `teacher` VALUES (61, '22', '22010', '赵雪婷', '3', NULL);
INSERT INTO `teacher` VALUES (62, '22', '22011', '王建华', '3', '');
INSERT INTO `teacher` VALUES (63, '22', '22012', '李佳琦', '2', NULL);
INSERT INTO `teacher` VALUES (64, '22', '22013', '杨晨阳', '2', NULL);
INSERT INTO `teacher` VALUES (65, '22', '22014', '赵婉婷', '1', NULL);
INSERT INTO `teacher` VALUES (66, '22', '22015', '吴美玲', '2', NULL);
INSERT INTO `teacher` VALUES (67, '22', '22016', '梁明宇', '2', NULL);
INSERT INTO `teacher` VALUES (68, '22', '22017', '方思婷', '2', NULL);
INSERT INTO `teacher` VALUES (69, '19', '19003', '肖雨欣', '2', NULL);
INSERT INTO `teacher` VALUES (70, '19', '19004', '乔鹏飞', '2', NULL);
INSERT INTO `teacher` VALUES (71, '19', '19005', '易佳琳', '1', NULL);

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'id',
  `user_no` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户账号',
  `username` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户名',
  `password` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户密码',
  `role` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户角色',
  `avatar_url` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户头像',
  `signature` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '个性签名',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 245 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, 'ffff0x', 'Jack Chen', 'Xw3bk+9yPTpawMRPgSrr0g==', 'ROLE_ADMINISTRATOR', 'http://localhost:8081/file/ad5ae58e8399474aaa3a1b5172781144.jpeg', '追风赶月莫停留 平芜尽处是春山');
INSERT INTO `user` VALUES (88, '22001', '程敏', 'J7H+IVbO4RNIazZ9elny5w==', 'ROLE_ADMIN', 'http://localhost:8081/file/6d3b5510448549a09c084c076548009c.jpeg', '好好工作！');
INSERT INTO `user` VALUES (89, '22002', '赵军强', 'YXavcdcsLQqjtHlGW1EeBA==', 'ROLE_TEACHER', 'http://localhost:8081/file/5d2ed4c47ddd4e78b8b3f286204154c4.jpeg', NULL);
INSERT INTO `user` VALUES (90, '22003', '周杰伦', 'RHvI0yHxOimVrfk+VPE54Q==', 'ROLE_TEACHER', 'http://localhost:8081/file/5d2ed4c47ddd4e78b8b3f286204154c4.jpeg', NULL);
INSERT INTO `user` VALUES (91, '22004', '赵洋', 'haCOJbvIigDc8ZuerDjXpw==', 'ROLE_TEACHER', 'http://localhost:8081/file/5d2ed4c47ddd4e78b8b3f286204154c4.jpeg', NULL);
INSERT INTO `user` VALUES (92, '22005', '梁军', 'hykxBNgJbA5vGQBBlt/IQg==', 'ROLE_TEACHER', 'http://localhost:8081/file/5d2ed4c47ddd4e78b8b3f286204154c4.jpeg', NULL);
INSERT INTO `user` VALUES (93, '22006', '王明杰', 'c+RU6m5Nuq+HmoVQeqUKBA==', 'ROLE_TEACHER', 'http://localhost:8081/file/5d2ed4c47ddd4e78b8b3f286204154c4.jpeg', NULL);
INSERT INTO `user` VALUES (94, '11001', '黄静', '7rfnRrgC2wbDO+itt38iZA==', 'ROLE_TEACHER', 'http://localhost:8081/file/5d2ed4c47ddd4e78b8b3f286204154c4.jpeg', NULL);
INSERT INTO `user` VALUES (95, '24001', '李霞', 'Qnwf5ENOZzfJPFseqdKbOw==', 'ROLE_TEACHER', 'http://localhost:8081/file/5d2ed4c47ddd4e78b8b3f286204154c4.jpeg', NULL);
INSERT INTO `user` VALUES (96, '19001', '周明辉', 'u7h7jwByQY2jkuLLX8bWxw==', 'ROLE_TEACHER', 'http://localhost:8081/file/5d2ed4c47ddd4e78b8b3f286204154c4.jpeg', NULL);
INSERT INTO `user` VALUES (98, '19002', '陈思宇', 'qoR/+7jH5tgz2nL8jymcFA==', 'ROLE_TEACHER', 'http://localhost:8081/file/5d2ed4c47ddd4e78b8b3f286204154c4.jpeg', NULL);
INSERT INTO `user` VALUES (99, '17001', '李梦洁', 'h4L0j5PplCqZhuPPAdJIAw==', 'ROLE_TEACHER', 'http://localhost:8081/file/5d2ed4c47ddd4e78b8b3f286204154c4.jpeg', NULL);
INSERT INTO `user` VALUES (100, '17002', '王雨萌', 'agePqTD2Xk0xocHjAuyWQA==', 'ROLE_TEACHER', 'http://localhost:8081/file/5d2ed4c47ddd4e78b8b3f286204154c4.jpeg', NULL);
INSERT INTO `user` VALUES (101, '17003', '杨佳琪', 'RMh518sq9PXFCRLG4Ws9WA==', 'ROLE_TEACHER', 'http://localhost:8081/file/5d2ed4c47ddd4e78b8b3f286204154c4.jpeg', NULL);
INSERT INTO `user` VALUES (114, '2022110101', '李明', 'flOCxKwt6Swj7tWdIeL9xw==', 'ROLE_STUDENT', 'http://localhost:8081/file/144ebc903e484932b863637cbdbcaae8.jpeg', '一战上岸');
INSERT INTO `user` VALUES (115, '2022110102', '	\r\n王华', 'MLa0XVi4KL6ULma9W+Ba7w==', 'ROLE_STUDENT', 'http://localhost:8081/file/5d2ed4c47ddd4e78b8b3f286204154c4.jpeg', NULL);
INSERT INTO `user` VALUES (117, '2122110101', '张三', 'RePmOxt1pvtLpO38XeUzuw==', 'ROLE_STUDENT', 'http://localhost:8081/file/5d2ed4c47ddd4e78b8b3f286204154c4.jpeg', '努力学习！');
INSERT INTO `user` VALUES (120, '2011170101', '王翔', '7NGR8BApVlwG3k8UmnXAfg==', 'ROLE_STUDENT', 'http://localhost:8081/file/5d2ed4c47ddd4e78b8b3f286204154c4.jpeg', '努力学习！');
INSERT INTO `user` VALUES (121, '2011170102', '李文', '/MXm67qDYszVwduayk/HoQ==', 'ROLE_STUDENT', 'http://localhost:8081/file/5d2ed4c47ddd4e78b8b3f286204154c4.jpeg', '努力学习！');
INSERT INTO `user` VALUES (122, '2011170103', '张明', '1mhCXl+foa5Mbqb5I7VaVw==', 'ROLE_STUDENT', 'http://localhost:8081/file/5d2ed4c47ddd4e78b8b3f286204154c4.jpeg', '努力学习！');
INSERT INTO `user` VALUES (123, '2011170104', '刘辉', 'F4S/dVv6/8XLnsjWDeoq+Q==', 'ROLE_STUDENT', 'http://localhost:8081/file/5d2ed4c47ddd4e78b8b3f286204154c4.jpeg', '努力学习！');
INSERT INTO `user` VALUES (124, '2011170105', '陈芳', 'joqulDItoWhbwlLuCF7pIg==', 'ROLE_STUDENT', 'http://localhost:8081/file/5d2ed4c47ddd4e78b8b3f286204154c4.jpeg', '努力学习！');
INSERT INTO `user` VALUES (125, '2011170106', '杨婷', 'JUtTJWuVuq07tx85mQtv3g==', 'ROLE_STUDENT', 'http://localhost:8081/file/5d2ed4c47ddd4e78b8b3f286204154c4.jpeg', '努力学习！');
INSERT INTO `user` VALUES (126, '2011170107', '赵强', 'DBDa57zU8IxHtF7ruj9rgA==', 'ROLE_STUDENT', 'http://localhost:8081/file/5d2ed4c47ddd4e78b8b3f286204154c4.jpeg', '努力学习！');
INSERT INTO `user` VALUES (127, '2011170108', '黄伟', 'LAknc0o3rXPYL9XgI9MjiA==', 'ROLE_STUDENT', 'http://localhost:8081/file/5d2ed4c47ddd4e78b8b3f286204154c4.jpeg', '努力学习！');
INSERT INTO `user` VALUES (128, '2011170109', '周霞', 'Or4KV9AXqK9KslWlmjjL7w==', 'ROLE_STUDENT', 'http://localhost:8081/file/5d2ed4c47ddd4e78b8b3f286204154c4.jpeg', '努力学习！');
INSERT INTO `user` VALUES (129, '2011170110', '吴秀华', '9WVDWRn/HxieEUuV5SLd7Q==', 'ROLE_STUDENT', 'http://localhost:8081/file/5d2ed4c47ddd4e78b8b3f286204154c4.jpeg', '努力学习！');
INSERT INTO `user` VALUES (130, '2011170111', '徐梅', 'Qp/QDHjAD0hn7jftIR6jjA==', 'ROLE_STUDENT', 'http://localhost:8081/file/5d2ed4c47ddd4e78b8b3f286204154c4.jpeg', '努力学习！');
INSERT INTO `user` VALUES (131, '2011170112', '孙华娜', 'USeRBQk27P8I7W9RiYBvxw==', 'ROLE_STUDENT', 'http://localhost:8081/file/5d2ed4c47ddd4e78b8b3f286204154c4.jpeg', '努力学习！');
INSERT INTO `user` VALUES (132, '2022110103', '李文博', 'wp/zShSTcqHRB7UTQC/ozQ==', 'ROLE_STUDENT', 'http://localhost:8081/file/5d2ed4c47ddd4e78b8b3f286204154c4.jpeg', '努力学习！');
INSERT INTO `user` VALUES (133, '2022110104', '王诗涵', 'IwXRX/q2WAChpLyUrqw+Zg==', 'ROLE_STUDENT', 'http://localhost:8081/file/5d2ed4c47ddd4e78b8b3f286204154c4.jpeg', '努力学习！');
INSERT INTO `user` VALUES (134, '2022110105', '张宇辰', 'Py1XshYy9dz4vq9qY53KHg==', 'ROLE_STUDENT', 'http://localhost:8081/file/5d2ed4c47ddd4e78b8b3f286204154c4.jpeg', '努力学习！');
INSERT INTO `user` VALUES (135, '2022110106', '陈晓彤', 'sblSepLMIBUr/rbxnkBYJA==', 'ROLE_STUDENT', 'http://localhost:8081/file/5d2ed4c47ddd4e78b8b3f286204154c4.jpeg', '努力学习！');
INSERT INTO `user` VALUES (136, '2011170113', '李佳怡', '1HCsy/KjhHgGLOWgY9Vd4w==', 'ROLE_STUDENT', 'http://localhost:8081/file/5d2ed4c47ddd4e78b8b3f286204154c4.jpeg', '努力学习！');
INSERT INTO `user` VALUES (137, '2011170114', '王子轩', 'MYcq3LMNrDdPyaQgL5JHFg==', 'ROLE_STUDENT', 'http://localhost:8081/file/5d2ed4c47ddd4e78b8b3f286204154c4.jpeg', '努力学习！');
INSERT INTO `user` VALUES (138, '2011170115', '张梦婷', 'KzC8Ftlm7FV9nU0nFutTtw==', 'ROLE_STUDENT', 'http://localhost:8081/file/5d2ed4c47ddd4e78b8b3f286204154c4.jpeg', '努力学习！');
INSERT INTO `user` VALUES (139, '2011170116', '刘志强', 'vDgdKyZrRegeR66ci1x4sg==', 'ROLE_STUDENT', 'http://localhost:8081/file/5d2ed4c47ddd4e78b8b3f286204154c4.jpeg', '努力学习！');
INSERT INTO `user` VALUES (140, '2011170117', '陈思涵', 'R/QrINTSlSLfuykCyMHxbg==', 'ROLE_STUDENT', 'http://localhost:8081/file/5d2ed4c47ddd4e78b8b3f286204154c4.jpeg', '努力学习！');
INSERT INTO `user` VALUES (141, '2011170118', '赵雅静', 'jOwCDL6ZbyfBk5PsMUYD8w==', 'ROLE_STUDENT', 'http://localhost:8081/file/5d2ed4c47ddd4e78b8b3f286204154c4.jpeg', '努力学习！');
INSERT INTO `user` VALUES (142, '2011170119', '杨晨曦', 't3UlqkhwrnrN336BQGwWrw==', 'ROLE_STUDENT', 'http://localhost:8081/file/5d2ed4c47ddd4e78b8b3f286204154c4.jpeg', '努力学习！');
INSERT INTO `user` VALUES (143, '2011170120', '黄子杰', 'vShvXUOXJ6r1cs4gpOqcBg==', 'ROLE_STUDENT', 'http://localhost:8081/file/5d2ed4c47ddd4e78b8b3f286204154c4.jpeg', '努力学习！');
INSERT INTO `user` VALUES (144, '2011170121', '吴欣怡', 'cdFvVJJyanrkCNUhTBYp6Q==', 'ROLE_STUDENT', 'http://localhost:8081/file/5d2ed4c47ddd4e78b8b3f286204154c4.jpeg', '努力学习！');
INSERT INTO `user` VALUES (145, '2011170122', '周浩宇', 'k+tFm3LgFcjNVaQp5VQyjQ==', 'ROLE_STUDENT', 'http://localhost:8081/file/5d2ed4c47ddd4e78b8b3f286204154c4.jpeg', '努力学习！');
INSERT INTO `user` VALUES (146, '2011170123', '徐嘉宁', 'twimEE93AdanwV7QVVSEmg==', 'ROLE_STUDENT', 'http://localhost:8081/file/5d2ed4c47ddd4e78b8b3f286204154c4.jpeg', '努力学习！');
INSERT INTO `user` VALUES (147, '2011170124', '孙子涵', 'Sp10uo1SODYso39OEsa7Xg==', 'ROLE_STUDENT', 'http://localhost:8081/file/5d2ed4c47ddd4e78b8b3f286204154c4.jpeg', '努力学习！');
INSERT INTO `user` VALUES (148, '2011170125', '马瑞琪', 'Av2HHpk/O8DLbXbXHaeaNg==', 'ROLE_STUDENT', 'http://localhost:8081/file/5d2ed4c47ddd4e78b8b3f286204154c4.jpeg', '努力学习！');
INSERT INTO `user` VALUES (149, '2011170126', '朱文静', '/5D+ulGoRCpyeMYoPOE9Og==', 'ROLE_STUDENT', 'http://localhost:8081/file/5d2ed4c47ddd4e78b8b3f286204154c4.jpeg', '努力学习！');
INSERT INTO `user` VALUES (150, '2011170127', '胡志远', '1xXzaiTow4mgj0vVfv+zAw==', 'ROLE_STUDENT', 'http://localhost:8081/file/5d2ed4c47ddd4e78b8b3f286204154c4.jpeg', '努力学习！');
INSERT INTO `user` VALUES (151, '2011170128', '郭晓彤', 'cxzwWfLW80ydgu/g6Fu6ig==', 'ROLE_STUDENT', 'http://localhost:8081/file/5d2ed4c47ddd4e78b8b3f286204154c4.jpeg', '努力学习！');
INSERT INTO `user` VALUES (152, '2011170129', '林雨欣', 'G8qt31NlRrV1Iw74JzG2+Q==', 'ROLE_STUDENT', 'http://localhost:8081/file/5d2ed4c47ddd4e78b8b3f286204154c4.jpeg', '努力学习！');
INSERT INTO `user` VALUES (153, '2011170130', '何俊杰', 'QnUK6JUw2Fp2v+meImL/aQ==', 'ROLE_STUDENT', 'http://localhost:8081/file/5d2ed4c47ddd4e78b8b3f286204154c4.jpeg', '努力学习！');
INSERT INTO `user` VALUES (154, '2011170131', '高雪婷', 'jnrJRHiA3r2MeMQxyIb3mA==', 'ROLE_STUDENT', 'http://localhost:8081/file/5d2ed4c47ddd4e78b8b3f286204154c4.jpeg', '努力学习！');
INSERT INTO `user` VALUES (155, '2011170132', '罗志豪', 'xa1Qz+eEGRCJaG5mu8acbA==', 'ROLE_STUDENT', 'http://localhost:8081/file/5d2ed4c47ddd4e78b8b3f286204154c4.jpeg', '努力学习！');
INSERT INTO `user` VALUES (156, '2011170133', '梁文婷', 'IjraEf/dZB/zp3ZlwIesaQ==', 'ROLE_STUDENT', 'http://localhost:8081/file/5d2ed4c47ddd4e78b8b3f286204154c4.jpeg', '努力学习！');
INSERT INTO `user` VALUES (157, '2011170134', '宋子豪', 'kwtEJQOXvsE166Zujsr10g==', 'ROLE_STUDENT', 'http://localhost:8081/file/5d2ed4c47ddd4e78b8b3f286204154c4.jpeg', '努力学习！');
INSERT INTO `user` VALUES (158, '2011170135', '郑嘉琪', '1qVuscbrmm2MBwoX2/JCyA==', 'ROLE_STUDENT', 'http://localhost:8081/file/5d2ed4c47ddd4e78b8b3f286204154c4.jpeg', '努力学习！');
INSERT INTO `user` VALUES (159, '2011170136', '谢思源', 'osqLd7R0AnRRMzGNxVVXNw==', 'ROLE_STUDENT', 'http://localhost:8081/file/5d2ed4c47ddd4e78b8b3f286204154c4.jpeg', '努力学习！');
INSERT INTO `user` VALUES (160, '2011170137', '唐欣怡', '+97ZQ5WchTRXFFxg2PYc8Q==', 'ROLE_STUDENT', 'http://localhost:8081/file/5d2ed4c47ddd4e78b8b3f286204154c4.jpeg', '努力学习！');
INSERT INTO `user` VALUES (161, '2011170138', '韩志诚', '8JcsSUbT5MVrC8zzWm3Trg==', 'ROLE_STUDENT', 'http://localhost:8081/file/5d2ed4c47ddd4e78b8b3f286204154c4.jpeg', '努力学习！');
INSERT INTO `user` VALUES (162, '2011170139', '曹梦瑶', 'jDbSbLSVnM+C4VmiC/Px3A==', 'ROLE_STUDENT', 'http://localhost:8081/file/5d2ed4c47ddd4e78b8b3f286204154c4.jpeg', '努力学习！');
INSERT INTO `user` VALUES (163, '2011170140', '冯子轩', 'I9CoIvRC7n091lq+c9U8OA==', 'ROLE_STUDENT', 'http://localhost:8081/file/5d2ed4c47ddd4e78b8b3f286204154c4.jpeg', '努力学习！');
INSERT INTO `user` VALUES (164, '2011170141', '薛雅楠', '9LsQJt2pqnj9gIeoCXRPhA==', 'ROLE_STUDENT', 'http://localhost:8081/file/5d2ed4c47ddd4e78b8b3f286204154c4.jpeg', '努力学习！');
INSERT INTO `user` VALUES (165, '2011170142', '邓晨曦', 'gfyg7h/KUR9aTYsSKlefTg==', 'ROLE_STUDENT', 'http://localhost:8081/file/5d2ed4c47ddd4e78b8b3f286204154c4.jpeg', '努力学习！');
INSERT INTO `user` VALUES (226, '2022110107', '沈佳琪', 'GRGpdB835FOhwSUrlG/tEQ==', 'ROLE_STUDENT', 'http://localhost:8081/file/5d2ed4c47ddd4e78b8b3f286204154c4.jpeg', '努力学习！');
INSERT INTO `user` VALUES (227, '2022110108', '钱子涵', 'uiH48S8KmfCWhh9UXzbfDA==', 'ROLE_STUDENT', 'http://localhost:8081/file/5d2ed4c47ddd4e78b8b3f286204154c4.jpeg', '努力学习！');
INSERT INTO `user` VALUES (228, '2022110109', '秦梦瑶', 'A304oOFoMgtEwZW8p1Ubxw==', 'ROLE_STUDENT', 'http://localhost:8081/file/5d2ed4c47ddd4e78b8b3f286204154c4.jpeg', '努力学习！');
INSERT INTO `user` VALUES (229, '2022110110', '彭俊杰', 'h6HdbtzXXs+b5gwQkzzZrQ==', 'ROLE_STUDENT', 'http://localhost:8081/file/5d2ed4c47ddd4e78b8b3f286204154c4.jpeg', '努力学习！');
INSERT INTO `user` VALUES (230, '2022110111', '赖雅静', 'ESQSPXlG5rywGWRng17WRg==', 'ROLE_STUDENT', 'http://localhost:8081/file/5d2ed4c47ddd4e78b8b3f286204154c4.jpeg', '努力学习！');
INSERT INTO `user` VALUES (231, '2022110112', '谭志豪', 'hAmc3o+qWCw5xpJvTC4t1g==', 'ROLE_STUDENT', 'http://localhost:8081/file/5d2ed4c47ddd4e78b8b3f286204154c4.jpeg', '努力学习！');
INSERT INTO `user` VALUES (232, '2022110113', '丁思涵', 'd6EMkYPq4i/C8XesKQL87w==', 'ROLE_STUDENT', 'http://localhost:8081/file/5d2ed4c47ddd4e78b8b3f286204154c4.jpeg', '努力学习！');
INSERT INTO `user` VALUES (233, '2022110114', '段晨曦', 'MQNjBDpNojOScNu3n+nYlA==', 'ROLE_STUDENT', 'http://localhost:8081/file/5d2ed4c47ddd4e78b8b3f286204154c4.jpeg', '努力学习！');
INSERT INTO `user` VALUES (234, '2022110115', '程文博', 'RaojT+N4z1y9tOEUdMa5rw==', 'ROLE_STUDENT', 'http://localhost:8081/file/5d2ed4c47ddd4e78b8b3f286204154c4.jpeg', '努力学习！');
INSERT INTO `user` VALUES (235, '2022110116', '蔡子轩', '1s2nZR7hDIxrgAeSrArOew==', 'ROLE_STUDENT', 'http://localhost:8081/file/5d2ed4c47ddd4e78b8b3f286204154c4.jpeg', '努力学习！');
INSERT INTO `user` VALUES (236, '2022110117', '廖嘉宁', 'SraC2XXzrji9LzO9E1skeQ==', 'ROLE_STUDENT', 'http://localhost:8081/file/5d2ed4c47ddd4e78b8b3f286204154c4.jpeg', '努力学习！');
INSERT INTO `user` VALUES (237, '2022110118', '魏欣怡', 'ZcjV9p9/IffznDBEdJu5tQ==', 'ROLE_STUDENT', 'http://localhost:8081/file/5d2ed4c47ddd4e78b8b3f286204154c4.jpeg', '努力学习！');
INSERT INTO `user` VALUES (238, '2022110119', '薛浩宇', 'CL9tOsDJWNxxXJV/Wef1NQ==', 'ROLE_STUDENT', 'http://localhost:8081/file/5d2ed4c47ddd4e78b8b3f286204154c4.jpeg', '努力学习！');
INSERT INTO `user` VALUES (239, '2022110120', '尹雪婷', 'cfuCUjuQJXa101OgibajWQ==', 'ROLE_STUDENT', 'http://localhost:8081/file/5d2ed4c47ddd4e78b8b3f286204154c4.jpeg', '努力学习！');
INSERT INTO `user` VALUES (240, '2022110121', '白志远', 'puHnFnm3z1FPbV5RT8AXQg==', 'ROLE_STUDENT', 'http://localhost:8081/file/5d2ed4c47ddd4e78b8b3f286204154c4.jpeg', '努力学习！');
INSERT INTO `user` VALUES (241, '2022110122', '孟瑞琪', 'eD8w5S98hJcNadNPoQjIvA==', 'ROLE_STUDENT', 'http://localhost:8081/file/5d2ed4c47ddd4e78b8b3f286204154c4.jpeg', '努力学习！');
INSERT INTO `user` VALUES (242, '2022110123', '武晓彤', 'jCZesD4lsnbLsq6KlELDTg==', 'ROLE_STUDENT', 'http://localhost:8081/file/5d2ed4c47ddd4e78b8b3f286204154c4.jpeg', '努力学习！');
INSERT INTO `user` VALUES (243, '2022110124', '康子杰', 'xkcwMpW2pDBfpkZFYLImNw==', 'ROLE_STUDENT', 'http://localhost:8081/file/5d2ed4c47ddd4e78b8b3f286204154c4.jpeg', '努力学习！');
INSERT INTO `user` VALUES (244, '2022110125', '章文静', 'p3OV48Abvh/KvtUrTDMD+g==', 'ROLE_STUDENT', 'http://localhost:8081/file/5d2ed4c47ddd4e78b8b3f286204154c4.jpeg', '努力学习！');
INSERT INTO `user` VALUES (245, '2022110126', '莫俊杰', 'lMz4d4jIrep8I+OlyUlKWw==', 'ROLE_STUDENT', 'http://localhost:8081/file/5d2ed4c47ddd4e78b8b3f286204154c4.jpeg', '努力学习！');

SET FOREIGN_KEY_CHECKS = 1;
