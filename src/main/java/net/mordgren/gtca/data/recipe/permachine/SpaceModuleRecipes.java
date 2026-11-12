package net.mordgren.gtca.data.recipe.permachine;

import com.gregtechceu.gtceu.api.machine.MultiblockMachineDefinition;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.data.GTMachines;
import com.gregtechceu.gtceu.data.recipe.CustomTags;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.mordgren.gtca.common.data.GTCABlocks;
import net.mordgren.gtca.common.data.GTCAItems;

import java.util.List;
import java.util.function.Consumer;

import static com.gregtechceu.gtceu.api.GTValues.*;
import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.frameGt;
import static com.gregtechceu.gtceu.common.data.GTMaterials.*;
import static com.gregtechceu.gtceu.common.data.GTRecipeTypes.ASSEMBLY_LINE_RECIPES;
import static net.mordgren.gtca.common.data.machines.GTCAMachines.*;

public final class SpaceModuleRecipes {

    private SpaceModuleRecipes() {}

    public static void init(Consumer<FinishedRecipe> provider) {
        registerFamily(provider, "miner",
                new MultiblockMachineDefinition[]{SPACE_MINER_MKI, SPACE_MINER_MKII, SPACE_MINER_MKIII},
                new ItemStack[]{GTItems.SENSOR_LuV.asStack(), GTItems.SENSOR_ZPM.asStack(), GTItems.SENSOR_UV.asStack()});
        registerFamily(provider, "pump",
                new MultiblockMachineDefinition[]{SPACE_PUMP_MKI, SPACE_PUMP_MKII, SPACE_PUMP_MKIII},
                new ItemStack[]{GTItems.ELECTRIC_PUMP_LuV.asStack(), GTItems.ELECTRIC_PUMP_ZPM.asStack(), GTItems.ELECTRIC_PUMP_UV.asStack()});
        registerFamily(provider, "assembler",
                new MultiblockMachineDefinition[]{SPACE_ASSEMBLER_MKI, SPACE_ASSEMBLER_MKII, SPACE_ASSEMBLER_MKIII},
                new ItemStack[]{GTItems.ROBOT_ARM_LuV.asStack(), GTItems.ROBOT_ARM_ZPM.asStack(), GTItems.ROBOT_ARM_UV.asStack()});
    }

    private static void registerFamily(Consumer<FinishedRecipe> provider, String kind,
                                       MultiblockMachineDefinition[] modules, ItemStack[] components) {
        int[] tiers = {LuV, ZPM, UV};
        String[] marks = {"mki", "mkii", "mkiii"};
        List<TagKey<Item>> circuits = List.of(CustomTags.LuV_CIRCUITS, CustomTags.ZPM_CIRCUITS, CustomTags.UV_CIRCUITS);
        ItemStack[] motors = {
                GTItems.ELECTRIC_MOTOR_LuV.asStack(4),
                GTItems.ELECTRIC_MOTOR_ZPM.asStack(4),
                GTItems.ELECTRIC_MOTOR_UV.asStack(4)
        };

        for (int i = 0; i < modules.length; i++) {
            int mk = i + 1;
            int tier = tiers[i];
            String id = "space_" + kind + "_" + marks[i] + "_controller";
            ItemStack sample = i == 0 ? components[0] : modules[i - 1].asStack();

            var builder = ASSEMBLY_LINE_RECIPES.recipeBuilder(id)
                    .EUt(VA[tier]).duration(1200 * mk)
                    .inputItems(GTMachines.HULL[tier].asStack(4))
                    .inputItems(GTCABlocks.SPACE_ELEVATOR_CASING, 8)
                    .inputItems(GTCAItems.PIKO_WAFER, 8 * mk)
                    .inputItems(components[i].copyWithCount(8))
                    .inputItems(motors[i])
                    .inputItems(circuits.get(i), 16)
                    .inputItems(frameGt, NaquadahAlloy, 4)
                    .inputFluids(SolderingAlloy.getFluid(576 * mk))
                    .inputFluids(Lubricant.getFluid(8000 * mk))
                    .inputFluids(Polybenzimidazole.getFluid(576 * mk))
                    .outputItems(modules[i].asStack())
                    .stationResearch(b -> b
                            .researchStack(sample)
                            .researchId("gtca_" + id)
                            .CWUt(32 * mk)
                            .EUt(VA[tier]));

            if (i > 0) {
                builder.inputItems(modules[i - 1].asStack());
            }
            builder.save(provider);
        }
    }
}
