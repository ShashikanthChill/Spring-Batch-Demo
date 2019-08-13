/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thehumblefool.springbatchdemo;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

/**
 *
 * @author shash
 */
@Component
public class JokerItemProcessor implements ItemProcessor<JokerModel, JokerModel> {

    @Override
    public JokerModel process(JokerModel item) throws Exception {
        return new JokerModel(item.getFirstName().toUpperCase(), item.getLastName().toUpperCase());
    }

}
