package com.tridevmc.architecture.client.render.model.builder;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraftforge.client.model.pipeline.QuadBakingVertexConsumer;

public class BakedQuadBuilderVertexConsumer implements VertexConsumer {

    private final QuadBakingVertexConsumer realConsumer;
    private BakedQuad out = null;

    public BakedQuadBuilderVertexConsumer() {
        this.realConsumer = new QuadBakingVertexConsumer(q -> this.out = q);
    }

    public BakedQuadBuilderVertexConsumer setTintIndex(int tintIndex) {
        this.realConsumer.setTintIndex(tintIndex);
        return this;
    }

    public BakedQuadBuilderVertexConsumer setDirection(Direction direction) {
        this.realConsumer.setDirection(direction);
        return this;
    }

    public BakedQuadBuilderVertexConsumer setSprite(TextureAtlasSprite sprite) {
        this.realConsumer.setSprite(sprite);
        return this;
    }

    public BakedQuadBuilderVertexConsumer setShade(boolean shade) {
        this.realConsumer.setShade(shade);
        return this;
    }

    public BakedQuadBuilderVertexConsumer setHasAmbientOcclusion(boolean hasAmbientOcclusion) {
        realConsumer.setHasAmbientOcclusion(hasAmbientOcclusion);
        return this;
    }

    @Override
    public VertexConsumer vertex(double x, double y, double z) {
        return this.realConsumer.vertex(x, y, z);
    }

    @Override
    public VertexConsumer color(int r, int g, int b, int a) {
        return this.realConsumer.color(r, g, b, a);
    }

    @Override
    public VertexConsumer uv(float u, float v) {
        return this.realConsumer.uv(u, v);
    }

    @Override
    public VertexConsumer overlayCoords(int u, int v) {
        return this.realConsumer.overlayCoords(u, v);
    }

    @Override
    public VertexConsumer uv2(int u2, int v2) {
        return this.realConsumer.uv2(u2, v2);
    }

    @Override
    public VertexConsumer normal(float x, float y, float z) {
        return this.realConsumer.normal(x, y, z);
    }

    @Override
    public void endVertex() {
        this.realConsumer.endVertex();
    }

    @Override
    public void defaultColor(int r, int g, int b, int a) {
        this.realConsumer.defaultColor(r, g, b, a);
    }

    @Override
    public void unsetDefaultColor() {
        this.realConsumer.unsetDefaultColor();
    }

    public BakedQuad getBakedQuad() {
        return this.out;
    }
}
