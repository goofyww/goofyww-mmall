package com.oecoo.gf.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Date;
import java.util.List;

/**
 * Created by gf on 2018/6/9.
 * 类别 vo
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)//空的属性前台不做显示
public class CategoryVo {

    private Integer id;

    private Integer parentId;

    private List<CategoryVo> childCategoryVo;

    private String name;

    private Boolean status;

    private Integer sortOrder;

    private Date createTime;

    private Date updateTime;

}
