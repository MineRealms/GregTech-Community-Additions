package net.mordgren.gtca.data.recipe.permachine;

import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.common.data.GTRecipeTypes;
import com.gregtechceu.gtceu.data.recipe.CustomTags;
import net.minecraft.data.recipes.FinishedRecipe;
import net.mordgren.gtca.common.data.GTCAItems;
import net.mordgren.gtca.common.data.GTCAMaterials;
import net.mordgren.gtca.common.util.GTCAHelper;

import java.util.function.Consumer;

import static com.gregtechceu.gtceu.api.GTValues.VA;
import static com.gregtechceu.gtceu.api.GTValues.UHV;
import static com.gregtechceu.gtceu.api.GTValues.UV;
import static com.gregtechceu.gtceu.common.data.GTItems.FIELD_GENERATOR_UV;
import static com.gregtechceu.gtceu.common.data.GTItems.ROBOT_ARM_UV;
import static com.gregtechceu.gtceu.common.data.GTRecipeTypes.ASSEMBLY_LINE_RECIPES;
import static net.mordgren.gtca.common.data.GTCAMaterials.*;

/**
 * Reachability recipes for the GTCA space elevator research chain.
 *
 * These items used to be registered without any producer, which made the
 * existing UHV elevator recipe impossible to complete in a normal world.
 */
public final class SpaceChainRecipes {

    private SpaceChainRecipes() {
    }

    public static void init(Consumer<FinishedRecipe> provider) {
        pikoWafer(provider);
        nanotubeSpool(provider);
    }

    private static void pikoWafer(Consumer<FinishedRecipe> provider) {
        ASSEMBLY_LINE_RECIPES.recipeBuilder("piko_wafer_ic")
                .duration(6400)
                .EUt(VA[UHV])
                .inputItems(GTItems.SYSTEM_ON_CHIP, 16)
                .inputItems(GTItems.QUANTUM_STAR, 4)
                .inputItems(GTCAHelper.getItem("plate", Neutronex, 4))
                .inputItems(CustomTags.UHV_CIRCUITS, 4)
                .inputItems(FIELD_GENERATOR_UV, 2)
                .inputFluids(GTMaterials.UUMatter.getFluid(16_000))
                .inputFluids(GTMaterials.Neutronium.getFluid(1_440))
                .outputItems(GTCAItems.PIKO_WAFER.asStack(64))
                .stationResearch(b -> b
                        .researchStack(GTCAItems.QuantumAnomaly.asStack())
                        .researchId("gtca_space_piko_wafer")
                        .CWUt(128)
                        .EUt(VA[UV]))
                .save(provider);
    }

    private static void nanotubeSpool(Consumer<FinishedRecipe> provider) {
        ASSEMBLY_LINE_RECIPES.recipeBuilder("nanotube_spool")
                .duration(4800)
                .EUt(VA[UHV])
                .inputItems(GTCAItems.CarbonNanites, 16)
                .inputItems(ROBOT_ARM_UV, 8)
                .inputItems(GTItems.QUANTUM_STAR, 2)
                // GTCEu 7.5.3 does not generate a neutronium ring item. Use
                // the registered plate form so this recipe cannot serialize an
                // empty ingredient.
                .inputItems(GTCAHelper.getItem("plate", GTMaterials.Neutronium, 8))
                .inputItems(CustomTags.UHV_CIRCUITS, 4)
                .inputFluids(GTMaterials.UUMatter.getFluid(8_000))
                .inputFluids(GTMaterials.Neutronium.getFluid(2_880))
                .outputItems(GTCAItems.NANOTUBE_SPOOL.asStack())
                .stationResearch(b -> b
                        .researchStack(GTCAItems.CarbonNanites.asStack())
                        .researchId("gtca_space_nanotube_spool")
                        .CWUt(128)
                        .EUt(VA[UV]))
                .save(provider);
    }
}
