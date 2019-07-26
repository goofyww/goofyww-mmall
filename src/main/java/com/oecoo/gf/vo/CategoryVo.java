package com.oecoo.gf.vo;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Date;
import java.util.List;

/**
 * Created by gf on 2018/6/9.
 * 类别 vo
 */
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

    public CategoryVo() {
    }

    @Override
    public String toString() {
        return "CategoryVo{" +
                "id=" + id +
                ", parentId=" + parentId +
                ", childCategoryVo=" + childCategoryVo +
                ", name='" + name + '\'' +
                ", status=" + status +
                ", sortOrder=" + sortOrder +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public List<CategoryVo> getChildCategoryVo() {
        return childCategoryVo;
    }

    public void setChildCategoryVo(List<CategoryVo> childCategoryVo) {
        this.childCategoryVo = childCategoryVo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
