package com.example.lawnMower.component.reader;

import com.example.lawnMower.entity.LawnMower;
import lombok.Setter;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemStream;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.stereotype.Component;

@Component
public class LawnMowerReader implements ItemReader<LawnMower>, ItemStream {
    private ExecutionContext executionContext;

    private boolean isFirstLineGone = false;

    @Setter
    private FlatFileItemReader<FieldSet> reader;

    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        reader.open(executionContext);
        this.executionContext = executionContext;
    }

    @Override
    public LawnMower read() throws Exception {
        int i = 1;
        LawnMower lawnMower = new LawnMower();
        reader.open(executionContext);
        for (FieldSet line; (line = reader.read()) != null; i++) {
            if (!isFirstLineGone) {
                executionContext.putInt("endX", Integer.parseInt(line.readString(0).split(" ")[0]));
                executionContext.putInt("endY", Integer.parseInt(line.readString(0).split(" ")[0]));
                isFirstLineGone = !isFirstLineGone;
                continue;
            } else if (line.readString(0).split(" ")[0].charAt(0) >= 48 && line.readString(0).split(" ")[0].charAt(0) <= 57) {
                lawnMower.setX(Integer.parseInt(line.readString(0).split(" ")[0]));
                lawnMower.setY(Integer.parseInt(line.readString(0).split(" ")[1]));
                lawnMower.setOrientation(line.readString(0).split(" ")[2].charAt(0));
            } else {
                lawnMower.setInstructions(line.readString(0));
                return lawnMower;
            }
        }
        return null;
    }
}
