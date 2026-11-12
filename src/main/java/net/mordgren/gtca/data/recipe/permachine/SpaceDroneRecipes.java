package net.mordgren.gtca.data.recipe.permachine;

import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.data.recipe.CustomTags;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.mordgren.gtca.common.data.GTCAItems;
import net.mordgren.gtca.common.machine.multiblock.electric.miner.data.DroneTier;

import java.util.List;
import java.util.function.Consumer;

import static com.gregtechceu.gtceu.api.GTValues.*;
import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.*;
import static com.gregtechceu.gtceu.common.data.GTMaterials.*;
import static com.gregtechceu.gtceu.common.data.GTRecipeTypes.ASSEMBLER_RECIPES;
import static com.gregtechceu.gtceu.common.data.GTRecipeTypes.ASSEMBLY_LINE_RECIPES;

public final class SpaceDroneRecipes {

    private SpaceDroneRecipes() {}

    public static void init(Consumer<FinishedRecipe> provider) {
        groundDrones(provider);
        advancedDrones(provider);
    }

    private static void groundDrones(Consumer<FinishedRecipe> provider) {
        ItemStack[] motors = {
                GTItems.ELECTRIC_MOTOR_LV.asStack(2), GTItems.ELECTRIC_MOTOR_MV.asStack(2),
                GTItems.ELECTRIC_MOTOR_HV.asStack(2), GTItems.ELECTRIC_MOTOR_EV.asStack(2),
                GTItems.ELECTRIC_MOTOR_IV.asStack(2), GTItems.ELECTRIC_MOTOR_LuV.asStack(2),
                GTItems.ELECTRIC_MOTOR_ZPM.asStack(2), GTItems.ELECTRIC_MOTOR_UV.asStack(2)
        };
        ItemStack[] sensors = {
                GTItems.SENSOR_LV.asStack(), GTItems.SENSOR_MV.asStack(),
                GTItems.SENSOR_HV.asStack(), GTItems.SENSOR_EV.asStack(),
                GTItems.SENSOR_IV.asStack(), GTItems.SENSOR_LuV.asStack(),
                GTItems.SENSOR_ZPM.asStack(), GTItems.SENSOR_UV.asStack()
        };
        ItemStack[] arms = {
                GTItems.ROBOT_ARM_LV.asStack(), GTItems.ROBOT_ARM_MV.asStack(),
                GTItems.ROBOT_ARM_HV.asStack(), GTItems.ROBOT_ARM_EV.asStack(),
                GTItems.ROBOT_ARM_IV.asStack(), GTItems.ROBOT_ARM_LuV.asStack(),
                GTItems.ROBOT_ARM_ZPM.asStack(), GTItems.ROBOT_ARM_UV.asStack()
        };
        List<TagKey<Item>> circuits = List.of(CustomTags.LV_CIRCUITS, CustomTags.MV_CIRCUITS,
                CustomTags.HV_CIRCUITS, CustomTags.EV_CIRCUITS, CustomTags.IV_CIRCUITS,
                CustomTags.LuV_CIRCUITS, CustomTags.ZPM_CIRCUITS, CustomTags.UV_CIRCUITS);
        Material[] shellMaterials = {Steel, Aluminium, StainlessSteel, Titanium, TungstenSteel,
                RhodiumPlatedPalladium, NaquadahAlloy, Neutronium};
        String[] names = {"lv", "mv", "hv", "ev", "iv", "luv", "zpm", "uv"};
        DroneTier[] drones = DroneTier.values();

        for (int i = 0; i < names.length; i++) {
            var builder = ASSEMBLER_RECIPES.recipeBuilder("space_mining_drone_" + names[i])
                    .EUt(VA[i + 1]).duration(400)
                    .inputItems(plate, shellMaterials[i], 4)
                    .inputItems(motors[i])
                    .inputItems(sensors[i])
                    .inputItems(arms[i])
                    .inputItems(circuits.get(i), 4)
                    .inputFluids(SolderingAlloy.getFluid(144))
                    .outputItems(drones[i].droneStack(1));

            if (i == 0) {
                builder.inputItems(frameGt, Steel);
            } else {
                builder.inputItems(drones[i - 1].droneStack(1));
            }
            builder.save(provider);
        }
    }

    private static void advancedDrones(Consumer<FinishedRecipe> provider) {
        DroneTier[] drones = {DroneTier.MK9_UHV, DroneTier.MK10_UEV};
        ItemStack[] previous = {GTCAItems.MinerDroneUV.asStack(), GTCAItems.MinerDroneUHV.asStack()};
        String[] names = {"uhv", "uev"};

        // UV components remain registered even when GTCEu high-tier components are disabled.
        for (int i = 0; i < drones.length; i++) {
            int multiplier = 1 << i;
            String id = "space_mining_drone_" + names[i];
            ItemStack sample = previous[i];

            ASSEMBLY_LINE_RECIPES.recipeBuilder(id)
                    .EUt(VA[UHV]).duration(1600 * multiplier)
                    .inputItems(sample)
                    .inputItems(plate, Neutronium, 16 * multiplier)
                    .inputItems(GTItems.ELECTRIC_MOTOR_UV, 8 * multiplier)
                    .inputItems(GTItems.SENSOR_UV, 4 * multiplier)
                    .inputItems(GTItems.ROBOT_ARM_UV, 4 * multiplier)
                    .inputItems(CustomTags.UHV_CIRCUITS, 8 * multiplier)
                    .inputItems(GTCAItems.PIKO_WAFER, 4 * multiplier)
                    .inputFluids(SolderingAlloy.getFluid(1152 * multiplier))
                    .inputFluids(Lubricant.getFluid(8000 * multiplier))
                    .outputItems(drones[i].droneStack(1))
                    .stationResearch(b -> b
                            .researchStack(sample)
                            .researchId("gtca_" + id)
                            .CWUt(128 * multiplier)
                            .EUt(VA[UV]))
                    .save(provider);
        }
    }
}
