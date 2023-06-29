package com.github.maoxp.service.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * <p>
 * 提醒事件
 * </p>
 *
 * @author maoxp
 * @since 2023-06-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("bw_remind_event")
public class BwRemindEvent implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * pk
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 提醒标题
     */
    private String title;

    /**
     * 备注
     */
    private String remark;

    /**
     * 状态：1日期+时间、2日期、3时间
     */
    private String expiredFlag;

    /**
     * 提醒日期
     */
//    @JsonSerialize(using = LocalDateSerializer.class)
//    @JsonDeserialize(using = LocalDateDeserializer.class)
//    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "Asia/Shanghai")
    private LocalDate expiredDate;

    /**
     * 提醒时间
     */
//    @JsonSerialize(using = LocalTimeSerializer.class)
//    @JsonDeserialize(using = LocalTimeDeserializer.class)

    private LocalTime expiredTime;

    /**
     * 提醒次数
     */
    private Integer remindCount;

    /**
     * 提前 n day，触发提醒
     */
    private Integer beforeDay;

    /**
     * 提前 n hour，触发提醒
     */
    private Integer beforeHour;

    /**
     * 提前 n min，触发提醒
     */
    private Integer beforeMin;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;


}
