package com.neusoft.bsp.dao;

import com.neusoft.bsp.model.storeOrder;

public interface storeOrderMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sto_store_order
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Integer stoId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sto_store_order
     *
     * @mbg.generated
     */
    int insert(storeOrder record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sto_store_order
     *
     * @mbg.generated
     */
    int insertSelective(storeOrder record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sto_store_order
     *
     * @mbg.generated
     */
    storeOrder selectByPrimaryKey(Integer stoId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sto_store_order
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(storeOrder record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sto_store_order
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(storeOrder record);
}