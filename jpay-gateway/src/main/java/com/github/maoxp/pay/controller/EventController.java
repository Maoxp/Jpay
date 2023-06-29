/*
 * Copyright 2013-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.maoxp.pay.controller;


import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.maoxp.pay.convert.RemindEventConvert;
import com.github.maoxp.pay.client.group.Delete;
import com.github.maoxp.pay.client.group.Insert;
import com.github.maoxp.pay.client.group.Update;
import com.github.maoxp.pay.client.page.PageResult;
import com.github.maoxp.pay.client.remind.dto.RemindEventDTO;
import com.github.maoxp.service.entity.BwRemindEvent;
import com.github.maoxp.service.service.IBwRemindEventService;
import com.github.maoxp.wx.property.WxPayApiProperty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("/remind")
@RequiredArgsConstructor
@Slf4j
public class EventController {
    private final IBwRemindEventService bwRemindEventService;
    private final RemindEventConvert remindEventConvert;

    private final WxPayApiProperty wxPayApiProperty;


    @PostMapping(value = "/add")
    public ResponseEntity addRemind(@Validated({Insert.class}) @Valid @RequestBody RemindEventDTO remindEventDTO) {
        final BwRemindEvent bwRemindEvent = remindEventConvert.remindEventDTOTransFormBwRemindEvent(remindEventDTO);
        this.bwRemindEventService.save(bwRemindEvent);
        return ResponseEntity.ok().body(bwRemindEvent);
    }

    @PostMapping(value = "/edit")
    public String editRemind(@Validated({Update.class}) @RequestBody RemindEventDTO remindEventDTO) {
        final BwRemindEvent bwRemindEvent = remindEventConvert.remindEventDTOTransFormBwRemindEvent(remindEventDTO);
        this.bwRemindEventService.updateById(bwRemindEvent);
        return "edit ok";
    }

    @PostMapping(value = "/remove")
    public String delRemind(@Validated({Delete.class}) @RequestBody RemindEventDTO remindEventDTO) {
        this.bwRemindEventService.removeById(remindEventDTO.getId());
        return "remove ok";
    }

    @PostMapping(value = "/index")
    public ResponseEntity<?> getIndex() {
        final List<BwRemindEvent> bwRemindEvents = this.bwRemindEventService.list(Wrappers.<BwRemindEvent>lambdaQuery().eq(BwRemindEvent::getId, 1));

        final PageResult<BwRemindEvent> bwRemindEventPageResult = new PageResult<>();
        bwRemindEventPageResult.setRows(bwRemindEvents);
        return ResponseEntity.ok(bwRemindEventPageResult);
    }

    @GetMapping(value = "/findOne/{id}")
    public ResponseEntity<?> getIndex(@PathVariable(value = "id") Long id) {
        final BwRemindEvent bwRemindEvents = this.bwRemindEventService.getById(id);
        return ResponseEntity.ok(bwRemindEvents);
    }

}
