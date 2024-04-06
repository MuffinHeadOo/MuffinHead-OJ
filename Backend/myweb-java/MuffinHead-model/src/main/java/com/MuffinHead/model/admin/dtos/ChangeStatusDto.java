package com.MuffinHead.model.admin.dtos;

import lombok.Data;

import java.util.Date;
@Data
public class ChangeStatusDto {

        //id
        private Integer id;
        //修改为目标状态
        private Integer status;

}
