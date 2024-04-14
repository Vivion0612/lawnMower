package com.example.lawnMower.config;

import com.example.lawnMower.entity.LawnMower;
import com.example.lawnMower.component.processor.LawnMowerProcessor;
import com.example.lawnMower.component.writer.LawnMowerWriter;
import com.example.lawnMower.component.reader.LawnMowerReader;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.PassThroughFieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.transaction.PlatformTransactionManager;
@Configuration
public class BatchConfig {
    private final JobRepository jobRepository;
    private final LawnMowerProcessor lawnMowerProcessor;
    private Resource resource;

    private PlatformTransactionManager transactionManager;
    public BatchConfig(LawnMowerProcessor lawnMowerProcessor,
                       JobRepository jobRepository,
                       @Value("classpath:test.txt") Resource resource,
                       PlatformTransactionManager transactionManager) {
        this.lawnMowerProcessor = lawnMowerProcessor;
        this.jobRepository = jobRepository;
        this.resource = resource;
        this.transactionManager = transactionManager;
    }

    @Bean
    public LawnMowerReader CreateItemReader() {
        FlatFileItemReader<FieldSet> reader = new FlatFileItemReaderBuilder<FieldSet>().name("FieldSetReader")
                .lineTokenizer(new DelimitedLineTokenizer())
                .fieldSetMapper(new PassThroughFieldSetMapper())
                .resource(resource)
                .build();
        LawnMowerReader lawnMowerReader = new LawnMowerReader();
        lawnMowerReader.setReader(reader);
        return lawnMowerReader;
    }

    @Bean()
    public ItemProcessor<LawnMower, LawnMower> createItemProcessor() {
        return new LawnMowerProcessor();
    }

    @Bean
    public Step lawnMowerStep() {
        return new StepBuilder("lawnMowerStep", jobRepository)
                .<LawnMower, LawnMower>chunk(1, transactionManager)
                .reader(CreateItemReader())
                .processor(createItemProcessor())
                .writer(new LawnMowerWriter(new FileSystemResource("target/result.txt")))
                .build();
    }

    @Bean
    public Job lawnMowerJob(Step lawnMowerStep) {
        return new JobBuilder("lawnMowerJob", jobRepository)
                .start(lawnMowerStep)
                .build();
    }

}
