package com.leclowndu93150.advanced_hoppers.client;

import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

import java.util.function.IntConsumer;
import java.util.function.IntFunction;

public class ValueSlider extends AbstractSliderButton {
    private final int min;
    private final int max;
    private final IntFunction<Component> label;
    private final IntConsumer onChange;
    private int current;
    private int pending = -1;

    public ValueSlider(int x, int y, int width, int min, int max, int initial, IntFunction<Component> label, IntConsumer onChange) {
        super(x, y, width, 20, label.apply(initial), max == min ? 0 : (double) (initial - min) / (max - min));
        this.min = min;
        this.max = max;
        this.label = label;
        this.onChange = onChange;
        this.current = initial;
    }

    private int valueOf(double position) {
        return Mth.clamp(min + (int) Math.round(position * (max - min)), min, max);
    }

    @Override
    protected void updateMessage() {
        setMessage(label.apply(valueOf(this.value)));
    }

    @Override
    protected void applyValue() {
        int next = valueOf(this.value);
        if (next != current) {
            current = next;
            pending = next;
            onChange.accept(next);
        }
    }

    public void sync(int serverValue) {
        if (pending != -1) {
            if (serverValue == pending) {
                pending = -1;
            }
            return;
        }
        if (serverValue != current) {
            current = Mth.clamp(serverValue, min, max);
            this.value = max == min ? 0 : (double) (current - min) / (max - min);
            updateMessage();
        }
    }
}
