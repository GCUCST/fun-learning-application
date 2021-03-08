package cn.cst.springbootmybatis.mapper;

import cn.cst.springbootmybatis.domain.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper  //这个注解和mapperScan作用一样，mapperScan可以以包范围为接口生成实现类
public interface UserMapper {
    User Sel(int id);
}
