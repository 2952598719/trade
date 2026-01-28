package com.orosirian.trade.coupon.db.mappers;

import com.orosirian.trade.coupon.db.model.Task;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TaskMapper {

    boolean insertTask(Task task);

    int updateTask(Task task);

    Task queryTaskById(Long id);

    List<Task> queryFailedTaskList();

}
