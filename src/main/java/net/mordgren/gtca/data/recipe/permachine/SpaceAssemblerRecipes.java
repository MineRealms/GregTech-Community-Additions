package net.mordgren.gtca.data.recipe.permachine;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.mordgren.gtca.GTCA;
import net.mordgren.gtca.common.data.GTCARecipeTypes;
import net.mordgren.gtca.common.machine.multiblock.electric.SpaceAssemblerMachine;
import net.mordgren.gtca.common.machine.multiblock.electric.miner.data.DrillMaterialTier;
import net.mordgren.gtca.common.machine.multiblock.electric.miner.registry.MiningParts;
import net.mordgren.gtca.common.util.GTCAHelper;

import java.util.function.Consumer;

public final class SpaceAssemblerRecipes {

    private SpaceAssemblerRecipes() {}

    public static void init(Consumer<FinishedRecipe> provider) {
        for (DrillMaterialTier drill : DrillMaterialTier.values()) {
            int mk = switch (drill) {
                case STEEL, TITANIUM, TUNGSTEN_STEEL -> 1;
                case NAQUADAH -> 2;
                case NAQUADAH_ALLOY, NEUTRONIUM -> 3;
            };
            String material = drill.material().getName();

            // Match the vanilla drill-head recipe: four material and four steel plates per head.
            add(provider, material + "_drill_heads", mk, 320,
                    MiningParts.drillTip(drill, 4),
                    new ItemStack[]{GTCAHelper.getItem("plate", drill.material(), 16),
                            GTCAHelper.getItem("plate", GTMaterials.Steel, 16)},
                    new FluidStack[0]);

            // A long rod contains one ingot, unlike a short rod which contains half an ingot.
            add(provider, material + "_drill_rods", mk, 160,
                    MiningParts.drillRod(drill, 16),
                    new ItemStack[]{GTCAHelper.getItem("ingot", drill.material(), 16)},
                    new FluidStack[0]);
        }
    }

    public static void add(Consumer<FinishedRecipe> provider,
                           String id,
                           int requiredMk,
                           int duration,
                           ItemStack output,
                           ItemStack[] itemInputs,
                           FluidStack[] fluidInputs) {
        add(provider, id, requiredMk, duration, GTValues.V[tierForMk(requiredMk)], output, itemInputs, fluidInputs);
    }

    public static void add(Consumer<FinishedRecipe> provider,
                           String id,
                           int requiredMk,
                           int duration,
                           long eut,
                           ItemStack output,
                           ItemStack[] itemInputs,
                           FluidStack[] fluidInputs) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Space Assembler recipe id is empty");
        }
        if (output == null || output.isEmpty()) {
            GTCA.LOGGER.warn("[SpaceAssembler] Skipped recipe {} because output is empty", id);
            return;
        }
        if (itemInputs != null) {
            for (ItemStack input : itemInputs) {
                if (input == null || input.isEmpty()) {
                    GTCA.LOGGER.warn("[SpaceAssembler] Skipped recipe {} because an item ingredient is empty", id);
                    return;
                }
            }
        }
        if (fluidInputs != null) {
            for (FluidStack input : fluidInputs) {
                if (input == null || input.isEmpty()) {
                    GTCA.LOGGER.warn("[SpaceAssembler] Skipped recipe {} because a fluid ingredient is empty", id);
                    return;
                }
            }
        }
        if (duration <= 0) {
            throw new IllegalArgumentException("Space Assembler duration must be > 0 for " + id);
        }
        if (eut <= 0) {
            throw new IllegalArgumentException("Space Assembler EU/t must be > 0 for " + id);
        }

        var builder = GTCARecipeTypes.SPACE_ASSEMBLER.recipeBuilder("space_assembler_" + id)
                .EUt(eut)
                .duration(duration)
                .outputItems(output)
                .addData(SpaceAssemblerMachine.REQUIRED_MODULE_MK_KEY, requiredMk);

        if (itemInputs != null && itemInputs.length > 0) {
            builder.inputItems(itemInputs);
        }

        if (fluidInputs != null && fluidInputs.length > 0) {
            builder.inputFluids(fluidInputs);
        }

        builder.save(provider);
    }

    private static int tierForMk(int mk) {
        return switch (mk) {
            case 1 -> GTValues.LuV;
            case 2 -> GTValues.ZPM;
            case 3 -> GTValues.UV;
            default -> throw new IllegalArgumentException("Unsupported Space Assembler MK: " + mk);
        };
    }
}
