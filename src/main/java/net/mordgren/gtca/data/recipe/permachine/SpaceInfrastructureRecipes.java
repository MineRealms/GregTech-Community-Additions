package net.mordgren.gtca.data.recipe.permachine;

import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.data.recipe.CustomTags;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.mordgren.gtca.common.data.GTCABlocks;
import net.mordgren.gtca.common.data.GTCAItems;

import java.util.function.Consumer;

import static com.gregtechceu.gtceu.api.GTValues.*;
import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.*;
import static com.gregtechceu.gtceu.common.data.GTMaterials.*;
import static com.gregtechceu.gtceu.common.data.GTRecipeTypes.ASSEMBLER_RECIPES;
import static com.gregtechceu.gtceu.common.data.GTRecipeTypes.ASSEMBLY_LINE_RECIPES;

public final class SpaceInfrastructureRecipes {

    private SpaceInfrastructureRecipes() {}

    public static void init(Consumer<FinishedRecipe> provider) {
        casings(provider);
        motors(provider);
    }

    private static void casings(Consumer<FinishedRecipe> provider) {
        ASSEMBLER_RECIPES.recipeBuilder("space_elevator_concrete_floor")
                .EUt(VA[EV]).duration(200)
                .inputItems(Blocks.SMOOTH_STONE, 8)
                .inputItems(plate, Neutronium)
                .inputFluids(Polybenzimidazole.getFluid(144))
                .outputItems(GTCABlocks.ULTRA_HIGH_STRENGTH_CONCRETE_FLOOR, 8)
                .save(provider);

        ASSEMBLER_RECIPES.recipeBuilder("space_elevator_reinforced_stone")
                .EUt(VA[EV]).duration(200)
                .inputItems(Blocks.STONE_BRICKS, 8)
                .inputItems(plate, Neutronium, 8)
                .inputFluids(Polybenzimidazole.getFluid(144))
                .outputItems(GTCABlocks.NEUTRONIUM_PLATED_REINFORCED_STONE, 8)
                .save(provider);

        ASSEMBLER_RECIPES.recipeBuilder("space_elevator_base_casing")
                .EUt(VA[UV]).duration(200)
                .inputItems(frameGt, NaquadahAlloy)
                .inputItems(plate, Neutronium, 6)
                .inputFluids(Polybenzimidazole.getFluid(144))
                .outputItems(GTCABlocks.SPACE_ELEVATOR_CASING)
                .save(provider);

        ASSEMBLER_RECIPES.recipeBuilder("space_elevator_internal_structure")
                .EUt(VA[ZPM]).duration(200)
                .inputItems(frameGt, NaquadahAlloy)
                .inputItems(rodLong, NaquadahAlloy, 4)
                .inputItems(plate, Neutronium, 2)
                .inputFluids(Polybenzimidazole.getFluid(144))
                .outputItems(GTCABlocks.SPACE_ELEVATOR_INTERNAL_STRUCTURE)
                .save(provider);

        ASSEMBLER_RECIPES.recipeBuilder("space_elevator_support_structure")
                .EUt(VA[UV]).duration(200)
                .inputItems(frameGt, Neutronium)
                .inputItems(rodLong, NaquadahAlloy, 4)
                .inputItems(plate, Neutronium, 4)
                .inputFluids(Polybenzimidazole.getFluid(288))
                .outputItems(GTCABlocks.SPACE_ELEVATOR_SUPPORT_STRUCTURE)
                .save(provider);

        ASSEMBLER_RECIPES.recipeBuilder("space_elevator_cable")
                .EUt(VA[UV]).duration(800)
                .inputItems(GTCAItems.NANOTUBE_SPOOL)
                .inputItems(rodLong, Neutronium, 16)
                .inputFluids(Polybenzimidazole.getFluid(2304))
                .outputItems(GTCABlocks.SPACE_ELEVATOR_CABLE, 16)
                .save(provider);
    }

    private static void motors(Consumer<FinishedRecipe> provider) {
        ItemStack[] motors = {
                GTCABlocks.SPACE_ELEVATOR_MOTOR_MK1.asStack(),
                GTCABlocks.SPACE_ELEVATOR_MOTOR_MK2.asStack(),
                GTCABlocks.SPACE_ELEVATOR_MOTOR_MK3.asStack(),
                GTCABlocks.SPACE_ELEVATOR_MOTOR_MK4.asStack(),
                GTCABlocks.SPACE_ELEVATOR_MOTOR_MK5.asStack()
        };

        for (int i = 0; i < motors.length; i++) {
            int mk = i + 1;
            int tier = i == 0 ? UV : UHV;
            ItemStack previous = i == 0 ? GTCABlocks.SPACE_ELEVATOR_CASING.asStack() : motors[i - 1];
            ItemStack researchSample = i == 0 ? GTItems.ELECTRIC_MOTOR_UV.asStack() : previous;

            ASSEMBLY_LINE_RECIPES.recipeBuilder("space_elevator_motor_mk_" + mk)
                    .EUt(VA[tier]).duration(1200 * mk)
                    .inputItems(previous)
                    .inputItems(GTItems.ELECTRIC_MOTOR_UV, 4 * mk)
                    .inputItems(GTItems.ELECTRIC_PISTON_UV, 2 * mk)
                    .inputItems(gear, Neutronium, 4 * mk)
                    .inputItems(i == 0 ? CustomTags.UV_CIRCUITS : CustomTags.UHV_CIRCUITS, 4 * mk)
                    .inputFluids(SolderingAlloy.getFluid(576 * mk))
                    .inputFluids(Lubricant.getFluid(4000 * mk))
                    .outputItems(motors[i])
                    .stationResearch(b -> b
                            .researchStack(researchSample)
                            .researchId("gtca_space_elevator_motor_mk_" + mk)
                            .CWUt(32 * mk)
                            .EUt(VA[tier]))
                    .save(provider);
        }
    }
}
