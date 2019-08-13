/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thehumblefool.springbatchdemo;

import java.util.List;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

/**
 *
 * @author shash
 */
@Configuration
@EnableBatchProcessing
public class BatchConfig {

    @Autowired
    JobBuilderFactory jobBuilderFactory;

    @Autowired
    StepBuilderFactory stepBuilderFactory;

    @Autowired
    JokerItemProcessor itemProcessor;

    @Autowired
    JokersRepo jokersRepo;

    @Bean
    public FlatFileItemReader<JokerModel> itemReader() {
//        return new FlatFileItemReaderBuilder<JokerModel>()
//                .name("Joker-item-Reader")
//                .resource(new ClassPathResource("jokers.csv"))
//                .delimited()
//                .names(new String[]{"First Name", "Last Name"})
//                //                .fieldSetMapper(new BeanWrapperFieldSetMapper<>() {
//                //                    {
//                //                        setTargetType(JokerModel.class);
//                //                    }
//                //                })
//                .linesToSkip(1)
//                .lineMapper(lineMapper())
//                .build();
        FlatFileItemReader<JokerModel> itemReader = new FlatFileItemReader<>();
        itemReader.setName("Joker-Item-Reader");
        itemReader.setResource(new ClassPathResource("jokers.csv"));
        itemReader.setLinesToSkip(1);
        itemReader.setLineMapper(lineMapper());
        return itemReader;
    }

    @Bean
    public LineMapper<JokerModel> lineMapper() {
        DefaultLineMapper<JokerModel> lineMapper = new DefaultLineMapper<>();
        lineMapper.setLineTokenizer(tokenizer());
        lineMapper.setFieldSetMapper(fieldSetMapper());
        return lineMapper;
    }

    @Bean
    public FieldSetMapper<JokerModel> fieldSetMapper() {
        return new BeanWrapperFieldSetMapper<>() {
            {
                setTargetType(JokerModel.class);
            }
        };
    }

    @Bean
    public DelimitedLineTokenizer tokenizer() {
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer(",");
        tokenizer.setStrict(false);
        tokenizer.setNames("First Name", "Last Name");
        return tokenizer;
    }
//

    @Bean
    public ItemWriter<JokerModel> itemWriter() {
        return jokersRepo::saveAll;
    }
//    
//    @Bean
//    public Job importUserJob(JobExecutionListener listener,Step step1)
//    {
//        
//    }
//    
//    @Bean
//    public Step step1(JdbcBatchItemWriter<JokerModel> itemWriter)
//    {
//        return stepBuilderFactory.get("step1")
//                .chunk(10)
//                .reader(itemReader())
//                .processor(itemProcessor())
//                .wri
//    }

    @Bean
    public Job JokersJob() {
        return jobBuilderFactory.get("Joker-Job")
                .incrementer(new RunIdIncrementer())
                .start(step())
                .build();
    }

    @Bean
    public Step step() {
        return stepBuilderFactory.get("joker-step")
                .<JokerModel, JokerModel>chunk(10)
                .reader(itemReader())
                .processor(itemProcessor)
                .writer(itemWriter())
                .build();
    }

}
