package com.example.lawnMower.component.processor;

import com.example.lawnMower.entity.LawnMower;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemStream;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.stereotype.Component;

@Component
public class LawnMowerProcessor implements ItemProcessor<LawnMower, LawnMower>, ItemStream {
    private ExecutionContext executionContext;

    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        ItemStream.super.open(executionContext);
        this.executionContext = executionContext;
    }

    @Override
    public void update(ExecutionContext executionContext) throws ItemStreamException {
        ItemStream.super.update(executionContext);
        this.executionContext = executionContext;
    }
    private void handleAdvance(LawnMower lawnMower) {
        switch (lawnMower.getOrientation()) {
            case 'N' -> {
                if (lawnMower.getY() < executionContext.getInt("endY"))
                    lawnMower.setY(lawnMower.getY() + 1);
            }
            case 'S' -> {
                if (lawnMower.getY() > 0)
                    lawnMower.setY(lawnMower.getY() - 1);
            }
            case 'E' -> {
                if (lawnMower.getX() < executionContext.getInt("endX"))
                    lawnMower.setX(lawnMower.getX() + 1);
            }
            case 'W' -> {
                if (lawnMower.getX() < executionContext.getInt("endY"))
                    lawnMower.setX(lawnMower.getX() - 1);
            }
            default -> {
            }
        }
    }

    private void handleRotateRight(LawnMower lawnMower) {
        switch (lawnMower.getOrientation()) {
            case 'N' -> lawnMower.setOrientation('E');
            case 'S' -> lawnMower.setOrientation('W');
            case 'E' -> lawnMower.setOrientation('S');
            case 'W' -> lawnMower.setOrientation('N');
            default -> {
            }
        }
    }

    private void handleRotateLeft(LawnMower lawnMower) {
        switch (lawnMower.getOrientation()) {
            case 'N' -> lawnMower.setOrientation('W');
            case 'S' -> lawnMower.setOrientation('E');
            case 'E' -> lawnMower.setOrientation('N');
            case 'W' -> lawnMower.setOrientation('S');
            default -> {
            }
        }
    }

    @Override
    public LawnMower process(LawnMower lawnMower) throws Exception {
        String instructions = lawnMower.getInstructions();
        if (instructions != null) {
            for (int i = 0; i != instructions.length(); i++) {
                switch (instructions.charAt(i)) {
                    case 'A' -> handleAdvance(lawnMower);
                    case 'D' -> handleRotateRight(lawnMower);
                    case 'G' -> handleRotateLeft(lawnMower);
                    default -> {
                    }
                }
            }
        }
        return lawnMower;
    }
}
