package com.example.lawnMower.component.writer;

import com.example.lawnMower.entity.LawnMower;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.core.io.FileSystemResource;

public class LawnMowerWriter extends FlatFileItemWriter<LawnMower> {
    public LawnMowerWriter(FileSystemResource resource) {
        setResource(resource);
        DelimitedLineAggregator<LawnMower> delimitedLineAggregator = new DelimitedLineAggregator<>();
        delimitedLineAggregator.setDelimiter(" ");
        delimitedLineAggregator.setFieldExtractor(lawnMower -> new Object[]{lawnMower.getX(), lawnMower.getY(), lawnMower.getOrientation()});
        setLineAggregator(delimitedLineAggregator);
    }
}


