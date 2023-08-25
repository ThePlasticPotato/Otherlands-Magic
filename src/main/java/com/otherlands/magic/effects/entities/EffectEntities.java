//package com.otherlands.magic.effects.entities;
//
//import com.otherlands.magic.effects.entities.fireball.FireballEntity;
//import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
//import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
//import net.minecraft.entity.EntityDimensions;
//import net.minecraft.entity.EntityType;
//import net.minecraft.entity.SpawnGroup;
//import net.minecraft.util.Identifier;
//import net.minecraft.util.registry.Registry;
//
//public class EffectEntities {
//
//    public static final EntityType<FireballEntity> FX_FIREBALL = Registry.register(
//            Registry.ENTITY_TYPE,
//            new Identifier("otherlands", "fx_fireball"),
//            FabricEntityTypeBuilder.<FireballEntity>create(SpawnGroup.MISC, FireballEntity::new).dimensions(EntityDimensions.fixed(1F, 1F)).build()
//    );
//
//
//
//    public static void register() {}
//
//}
