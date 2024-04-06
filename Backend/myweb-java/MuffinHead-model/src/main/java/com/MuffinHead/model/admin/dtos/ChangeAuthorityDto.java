package com.MuffinHead.model.admin.dtos;

import lombok.Data;

@Data
public class ChangeAuthorityDto {

        //id
        private Integer id;
        //修改为目标状态
        private Integer authority;

}
