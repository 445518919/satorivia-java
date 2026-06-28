-- ----------------------------
-- 0、咖啡产品访问跟踪日志表
-- ----------------------------
DROP TABLE IF EXISTS cafe_track_log;
CREATE TABLE cafe_track_log
(
    id           BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    prod_id      VARCHAR(64)  NOT NULL COMMENT '产品ID',
    prod_code    VARCHAR(64)  NOT NULL COMMENT '商品编码',
    prod_name    VARCHAR(255) NOT NULL COMMENT '产品名称 (如: 埃塞瑰夏精品咖啡豆)',
    event_name   VARCHAR(100) NOT NULL COMMENT '事件类型 (如: page_view)',
    ip           VARCHAR(45) COMMENT '访问者IP地址 (支持IPv6)',
    location     VARCHAR(255) COMMENT '访问者地理位置',
    user_agent   TEXT COMMENT '原始浏览器User-Agent字符串',
    device_type  VARCHAR(50) COMMENT '设备类型 (如: Mobile, Desktop, Tablet)',
    browser      VARCHAR(50) COMMENT '浏览器名称 (如: Chrome, WeChat, Safari)',
    os           VARCHAR(50) COMMENT '操作系统 (如: iOS, Android, Windows)',
    page_url     TEXT COMMENT '当前页面URL',
    referrer_url TEXT COMMENT '来源页面URL',
    created_at   DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间',
    -- 为常用查询字段建立索引
    INDEX `idx_prod_code` (`prod_code`),
    INDEX `idx_event_name` (`event_name`),
    INDEX `idx_created_at` (`created_at`)
) ENGINE = InnoDB COMMENT ='咖啡产品访问跟踪日志表';


-- ----------------------------
-- 1、产品表
-- ----------------------------
drop table if exists cafe_production;
create table cafe_production
(
    prod_id    bigint(20)  not null auto_increment comment '产品id',
    prod_name   varchar(30)    default '' comment '产品名称',
    prod_desc   varchar(255)   default '' comment '产品描述',
    prod_image varchar(255) default '' comment '产品图片',
    prod_code  varchar(64) not null comment '商品编码',
    prod_price  decimal(10, 2) default 0.00 comment '产品价格',
    prod_text   text comment '产品详情',
    order_num   int(4)         default 0 comment '显示顺序',
    prod_status char(1)        default '0' comment '产品状态（0正常 1停用）',
    create_by   varchar(64)    default '' comment '创建者',
    create_time datetime comment '创建时间',
    update_by   varchar(64)    default '' comment '更新者',
    update_time datetime comment '更新时间',
    delete_at   datetime       default NULL comment '删除时间',
    primary key (prod_id),
    unique key (prod_code)
) engine = innodb comment = '产品表';

-- ----------------------------
-- 2、品牌故事表
-- ----------------------------
drop table if exists cafe_brand_story;
create table cafe_brand_story
(
    story_id     bigint(20) not null auto_increment comment '品牌故事id',
    story_image varchar(255) default '' comment '品牌故事图片',
    story_text   text comment '品牌故事详情',
    story_status char(1)      default '0' comment '品牌故事状态（0正常 1停用）',
    create_by    varchar(64)  default '' comment '创建者',
    create_time  datetime comment '创建时间',
    update_by    varchar(64)  default '' comment '更新者',
    update_time  datetime comment '更新时间',
    delete_at    datetime     default NULL comment '删除时间',
    primary key (story_id)
) engine = innodb comment = '品牌故事表';

-- ----------------------------
-- 3、轮播图
-- ----------------------------
DROP TABLE IF EXISTS cafe_banner;
CREATE TABLE cafe_banner
(
    banner_id     BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '轮播图id',
    banner_title  VARCHAR(255) NOT NULL COMMENT '轮播图标题',
    banner_image VARCHAR(255) NOT NULL COMMENT '轮播图图片',
    banner_link   VARCHAR(255) NOT NULL COMMENT '轮播图链接',
    banner_desc   VARCHAR(255) DEFAULT '' COMMENT '轮播图描述',
    banner_status CHAR(1)      DEFAULT '0' COMMENT '轮播图状态（0正常 1停用）',
    create_by     VARCHAR(64)  DEFAULT '' COMMENT '创建者',
    create_time   DATETIME COMMENT '创建时间',
    update_by     VARCHAR(64)  DEFAULT '' COMMENT '更新者',
    update_time   DATETIME COMMENT '更新时间',
    delete_at    DATETIME DEFAULT NULL COMMENT '删除时间'
) engine = innodb comment = '轮播图表';

-- ----------------------------
-- 4、豆子笔记
-- ----------------------------
DROP TABLE IF EXISTS cafe_beans_note;
CREATE TABLE cafe_beans_note
(
    note_id        BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '豆子笔记id',
    note_title     VARCHAR(255) NOT NULL COMMENT '豆子笔记标题',
    note_sub_title VARCHAR(255) NOT NULL COMMENT '豆子笔记副标题',
    note_content   TEXT         NOT NULL COMMENT '豆子笔记内容',
    note_image VARCHAR(255) NOT NULL COMMENT '豆子笔记图片',
    brew_time      VARCHAR(255) NOT NULL COMMENT '冲泡时间',
    poder_quantity VARCHAR(255) NOT NULL COMMENT '粉量',
    water_volume   VARCHAR(255) NOT NULL COMMENT '水量',
    water_temp     VARCHAR(255) NOT NULL COMMENT '水温',
    grind_degree   VARCHAR(255) NOT NULL COMMENT '研磨度',
    target_time    VARCHAR(255) NOT NULL COMMENT '目标总时间',
    preparations   TEXT         NOT NULL COMMENT '准备工作',
    brew_steps     TEXT         NOT NULL COMMENT '冲泡步骤',
    note_status    CHAR(1)     DEFAULT '0' COMMENT '豆子笔记状态（0正常 1停用）',
    create_by      VARCHAR(64) DEFAULT '' COMMENT '创建者',
    create_time    DATETIME    DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_by      VARCHAR(64) DEFAULT '' COMMENT '更新者',
    update_time    DATETIME    DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    delete_at      DATETIME    DEFAULT NULL COMMENT '删除时间'
) engine = innodb comment = '豆子笔记表';


