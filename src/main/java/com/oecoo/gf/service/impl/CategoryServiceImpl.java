package com.oecoo.gf.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.oecoo.gf.common.Const;
import com.oecoo.gf.common.ServerResponse;
import com.oecoo.gf.dao.CategoryMapper;
import com.oecoo.gf.pojo.Category;
import com.oecoo.gf.service.ICategoryService;
import com.oecoo.gf.vo.CategoryVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

/**
 * Created by gf on 2018/4/29.
 */
@Service("iCategoryService")
@Transactional(readOnly = true)
@Slf4j
public class CategoryServiceImpl implements ICategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    //get全部分类节点
    public ServerResponse<List<CategoryVo>> getCategoryList() {

        List<Category> paterCategoryList = categoryMapper.selectPaterCategory();
        //用于创建返回父节点Vo集合
        List<CategoryVo> categoryVos = Lists.newArrayList();

        if (!CollectionUtils.isEmpty(paterCategoryList)) {
            //父节点不为空

            for (Category paterCategory : paterCategoryList) {
                CategoryVo categoryVo = new CategoryVo();
                //创建父类分类vo
                categoryVo.setId(paterCategory.getId());
                categoryVo.setParentId(null);
                categoryVo.setName(paterCategory.getName());
                categoryVo.setStatus(paterCategory.getStatus());
                categoryVo.setSortOrder(paterCategory.getSortOrder());
                categoryVo.setUpdateTime(paterCategory.getUpdateTime());
                categoryVo.setCreateTime(paterCategory.getCreateTime());
                //通过父节点查询子节点
                List<Category> childCategoryList = categoryMapper.selectCategoryChildByCategoryId(paterCategory.getId());
                if (!CollectionUtils.isEmpty(childCategoryList)) {
                    List<CategoryVo> childCategoryVoList = Lists.newArrayList();
                    for (Category childCategory : childCategoryList) {
                        CategoryVo childCategoryVo = new CategoryVo();
                        childCategoryVo.setId(childCategory.getId());
                        childCategoryVo.setName(childCategory.getName());
                        childCategoryVo.setParentId(childCategory.getParentId());
                        childCategoryVo.setStatus(childCategory.getStatus());
                        childCategoryVo.setSortOrder(childCategory.getSortOrder());
                        childCategoryVo.setUpdateTime(childCategory.getUpdateTime());
                        childCategoryVo.setCreateTime(childCategory.getCreateTime());
                        childCategoryVoList.add(childCategoryVo);
                    }
                    categoryVo.setChildCategoryVo(childCategoryVoList);
                }
                categoryVos.add(categoryVo);
            }
        }
        if (CollectionUtils.isEmpty(paterCategoryList) || paterCategoryList == null) {
            return ServerResponse.createByErrorMessage("查询父类品类失败或者为空");
        }
        return ServerResponse.createBySuccessData(categoryVos);
    }


    //添加品类
    @Transactional()
    public ServerResponse<String> addCategory(String categoryName, Integer parentId) {
        if (parentId == null || StringUtils.isBlank(categoryName)) {
            return ServerResponse.createByErrorMessage("添加品类的参数错误或者是空");
        }
        int resultCount = categoryMapper.checkByCategoryName(categoryName);
        if (resultCount > 0) {
            //品类名存在无法插入
            return ServerResponse.createByErrorMessage("您要添加的品类名字已存在");
        }
        //品类名字不存在
        //定义一个插入的中间交互数据的对象
        Category category = new Category();
        category.setName(categoryName);
        category.setParentId(parentId);
        category.setStatus(true);//新建的这个分类是可用的

        resultCount = categoryMapper.insert(category);
        if (resultCount > 0) {
            return ServerResponse.createBySuccessMessage("添加品类成功");
        }
        return ServerResponse.createByErrorMessage("添加品类失败");
    }

    //修改品类名称
    public ServerResponse<String> updateCategoryName(Integer categoryId, String categoryName) {
        if (categoryId == null || StringUtils.isBlank(categoryName)) {
            return ServerResponse.createByErrorMessage("更新品类的参数错误或者是空");
        }
        int resultCount = categoryMapper.checkByCategoryName(categoryName);
        if (resultCount > 0) {
            //品类名存在无法修改
            return ServerResponse.createByErrorMessage("您要更新的品类名字已存在，请换个名字重新尝试更新");
        }
        resultCount = categoryMapper.checkByCategoryId(categoryId);
        if (resultCount > 0) {
            //品类名存在可以修改状态
            //品类名不存在可以修改
            Category category = new Category();
            category.setId(categoryId);
            category.setName(categoryName);

            int rowCount = categoryMapper.updateCategoryNameByPrimaryKey(category);
            if (rowCount > 0) {
                return ServerResponse.createBySuccessMessage("更新品类名字成功");
            }
            return ServerResponse.createByErrorMessage("更新品类名字失败");
        }
        return ServerResponse.createByErrorMessage("要修改的品类的不存在");

    }

    //修改品类状态
    public ServerResponse<String> updateCategoryStatus(Integer categoryId, Integer status) {
        if (categoryId == null || status == null) {
            return ServerResponse.createByErrorMessage("更新品类的参数错误或者是空");
        }
        if (status != Const.CategoryStatus.ON && status != Const.CategoryStatus.LOWER) {
            return ServerResponse.createByErrorMessage("状态参数错误");
        }
        int resultCount = categoryMapper.checkByCategoryId(categoryId);
        if (resultCount > 0) {
            //品类名存在可以修改状态
            if (status == Const.CategoryStatus.ON) {
                //执行可用操作
                Category category = new Category();
                category.setId(categoryId);
                category.setStatus(true);
                int rowCount = categoryMapper.updateByPrimaryKeySelective(category);
                if (rowCount > 0) {
                    return ServerResponse.createBySuccessMessage("更新品类状态成功");
                }
                return ServerResponse.createByErrorMessage("更新品类状态失败");

            } else {
                //执行废弃操作
                Category category = new Category();
                category.setId(categoryId);
                category.setStatus(false);
                int rowCount = categoryMapper.updateCategoryStatusByPrimaryKey(category);
                if (rowCount > 0) {
                    return ServerResponse.createBySuccessMessage("更新品类状态成功");
                }
                return ServerResponse.createByErrorMessage("更新品类状态失败");
            }
        }
        return ServerResponse.createByErrorMessage("要修改的品类的不存在");
    }

    //删除品类
    public ServerResponse removeCategoryById(Integer categoryId) {
        Category category = categoryMapper.selectByCategoryId(categoryId);
        if (category != null) {
            //品类存在 再删除
            if (category.getParentId() != 0) {// 是子节点
                int resultCount = categoryMapper.deleteByPrimaryKey(categoryId);
                if (resultCount > 0) {
                    return ServerResponse.createBySuccessMessage("删除品类成功");
                }
                return ServerResponse.createByErrorMessage("删除品类失败");
            }
            return ServerResponse.createByErrorMessage("父品类不可删除，可修改为废弃状态");
        }
        return ServerResponse.createByErrorMessage("要删除的品类的不存在");
    }

    //获取子节点集合
    public ServerResponse<List<Category>> getChildParallelCategory(Integer categoryId) {
        List<Category> categoryList = categoryMapper.selectCategoryChildByCategoryId(categoryId);
        if (CollectionUtils.isEmpty(categoryList)) {
            //获取失败的信息不应该反馈给前端 ，因此在这里使用日志打印方式
            log.info("getChildParallelCategory Method 未找到当前分类的子分类");
        }
        return ServerResponse.createBySuccessData(categoryList);
    }

    //递归查询本节点ID 以及子节点 ID
    public ServerResponse<List<Integer>> selectCategoryAndChildrenById(Integer categoryId) {

        Set<Category> categorySet = Sets.newHashSet();
        findChildCategory(categorySet, categoryId);

        List<Integer> categoryList = Lists.newArrayList();
        if (categoryId != null) {
            for (Category categoryItem : categorySet) {
                categoryList.add(categoryItem.getId());
            }
        }
        return ServerResponse.createBySuccessData(categoryList);
    }


    /**
     * 递归算法 ， 算出子节点 ,使用 Set 不会有重复对象
     */
    private Set<Category> findChildCategory(Set<Category> categorySet, Integer categoryId) {
        //先获取父节点
        Category category = categoryMapper.selectByPrimaryKey(categoryId);
        if (category != null) {
            //节点不为空 ，将它放入集合
            categorySet.add(category);
        }
        //再获取子节点
        List<Category> categoryList = categoryMapper.selectCategoryChildByCategoryId(categoryId);
        //遍历子节点
        //递归算法一定要有一个退出的条件
        // 这里的条件就是子节点category是否为空 ，为空则跳出for循环
        for (Category categoryItem : categoryList) {
            // 进行递归查询
            findChildCategory(categorySet, categoryItem.getId());
        }
        return categorySet;
    }
}
