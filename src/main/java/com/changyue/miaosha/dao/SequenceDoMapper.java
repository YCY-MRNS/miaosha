package com.changyue.miaosha.dao;

import com.changyue.miaosha.bean.SequenceDo;
import org.springframework.stereotype.Repository;

@Repository
public interface SequenceDoMapper {
    int deleteByPrimaryKey(String name);

    int insert(SequenceDo record);

    int insertSelective(SequenceDo record);

    SequenceDo selectByPrimaryKey(String name);

    int updateByPrimaryKeySelective(SequenceDo record);

    int updateByPrimaryKey(SequenceDo record);

    SequenceDo getSequenceByName(String name);
}