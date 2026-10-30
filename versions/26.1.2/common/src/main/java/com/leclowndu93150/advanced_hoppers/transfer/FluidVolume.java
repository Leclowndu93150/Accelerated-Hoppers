package com.leclowndu93150.advanced_hoppers.transfer;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;

public final class FluidVolume {
    private static final Codec<Fluid> FLUID_NON_EMPTY_CODEC = BuiltInRegistries.FLUID.byNameCodec().validate(fluid -> fluid == Fluids.EMPTY
            ? DataResult.error(() -> "Fluid must not be minecraft:empty")
            : DataResult.success(fluid));
    public static final Codec<FluidVolume> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            FLUID_NON_EMPTY_CODEC.fieldOf("id").forGetter(FluidVolume::getFluid),
            ExtraCodecs.POSITIVE_INT.fieldOf("amount").forGetter(FluidVolume::getAmount),
            DataComponentPatch.CODEC.optionalFieldOf("components", DataComponentPatch.EMPTY).forGetter(FluidVolume::getComponents)
    ).apply(instance, FluidVolume::new));
    public static final FluidVolume EMPTY = new FluidVolume(Fluids.EMPTY, 0, DataComponentPatch.EMPTY);

    private final Fluid fluid;
    private final DataComponentPatch components;
    private int amount;

    public FluidVolume(Fluid fluid, int amount) {
        this(fluid, amount, DataComponentPatch.EMPTY);
    }

    public FluidVolume(Fluid fluid, int amount, DataComponentPatch components) {
        this.fluid = fluid;
        this.amount = amount;
        this.components = components;
    }

    public boolean isEmpty() {
        return this == EMPTY || this.fluid == Fluids.EMPTY || this.amount <= 0;
    }

    public Fluid getFluid() {
        return this.isEmpty() ? Fluids.EMPTY : this.fluid;
    }

    public int getAmount() {
        return this.isEmpty() ? 0 : this.amount;
    }

    public DataComponentPatch getComponents() {
        return this.isEmpty() ? DataComponentPatch.EMPTY : this.components;
    }

    public void setAmount(int amount) {
        if (this != EMPTY) {
            this.amount = amount;
        }
    }

    public void grow(int addedAmount) {
        this.setAmount(this.getAmount() + addedAmount);
    }

    public void shrink(int removedAmount) {
        this.grow(-removedAmount);
    }

    public FluidVolume copy() {
        return this.isEmpty() ? EMPTY : new FluidVolume(this.fluid, this.amount, this.components);
    }

    public FluidVolume copyWithAmount(int amount) {
        return this.isEmpty() ? EMPTY : new FluidVolume(this.fluid, amount, this.components);
    }

    public static boolean isSameFluidSameComponents(FluidVolume first, FluidVolume second) {
        if (first.getFluid() != second.getFluid()) {
            return false;
        }
        return first.isEmpty() && second.isEmpty() || first.getComponents().equals(second.getComponents());
    }
}
