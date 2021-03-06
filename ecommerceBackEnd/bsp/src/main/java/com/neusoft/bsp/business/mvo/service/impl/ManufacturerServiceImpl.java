package com.neusoft.bsp.business.mvo.service.impl;

import com.neusoft.bsp.business.po.Manufacturer;
import com.neusoft.bsp.business.mvo.mapper.ManufacturerMapper;
import com.neusoft.bsp.business.mvo.service.ManufacturerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("manufacturerService")
public class ManufacturerServiceImpl implements ManufacturerService {

    @Autowired
    ManufacturerMapper manufacturerMapper;

    @Override
    public int insert(Manufacturer manufacturer) {
        return manufacturerMapper.insert(manufacturer);
    }

    @Override
    public int update(Manufacturer manufacturer) {
        return manufacturerMapper.update(manufacturer);
    }

    @Override
    public int delete(int man_id) {
        return manufacturerMapper.delete(man_id);
    }

    @Override
    public Manufacturer getById(int man_id) {
        return manufacturerMapper.getById(man_id);
    }

    @Override
    public int updateUserWithLastManu(int user_id) {
        return manufacturerMapper.updateUserWithLastManu(user_id);
    }

    @Override
    public int updateUserWithManuID(int man_id) { return manufacturerMapper.updateUserWithManuID(man_id); }
}
