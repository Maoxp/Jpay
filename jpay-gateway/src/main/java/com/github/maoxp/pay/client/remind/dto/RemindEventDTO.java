package com.github.maoxp.pay.client.remind.dto;


import com.github.maoxp.pay.client.group.Delete;
import com.github.maoxp.pay.client.group.Insert;
import com.github.maoxp.pay.client.group.Update;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * RemindEventDTO
 *
 * @author mxp
 * @date 2023/6/1年06月01日 11:19
 * @since JDK 1.8
 */
@Data
public class RemindEventDTO implements Serializable {
    @NotNull(groups = {Update.class, Delete.class}, message = "提醒事项ID不能为空")
    private Long id;

    @NotBlank(groups = {Insert.class, Update.class}, message = "提醒标题不能为空")
    private String title;

    @NotBlank(groups = {Insert.class, Update.class}, message = "提醒内容不能为空")
    private String remark;

    @NotNull(groups = {Insert.class, Update.class}, message = "日期能为空")
    @FutureOrPresent(groups = {Insert.class, Update.class}, message = "限制日期为当前或将来的时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd") // 日期格式化转换
    private LocalDate expiredDate;

    private LocalTime expiredTime;

    private Integer remindCount;

    private Integer beforeDay;

    private Integer beforeHour;

    private Integer beforeMin;
}
